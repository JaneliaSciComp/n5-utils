package org.janelia.saalfeldlab.adapter.neuroglancer;

import com.fasterxml.jackson.databind.ObjectMapper;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import bdv.util.RandomAccessibleIntervalMipmapSource;
import bdv.util.AxisOrder;
import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.volatiles.VolatileViews;
import bdv.cache.SharedQueue;
import mpicbg.spim.data.sequence.FinalVoxelDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.cache.volatiles.CacheHints;
import net.imglib2.cache.volatiles.LoadingStrategy;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.volatiles.AbstractVolatileNativeRealType;
import net.imglib2.type.volatiles.VolatileDoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.imglib2.view.Views;

import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;
import org.janelia.saalfeldlab.View;
import org.janelia.saalfeldlab.control.mcu.MCUBDVControls;
import org.janelia.saalfeldlab.control.mcu.XTouchMiniMCUControlPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * NeuroglancerViewer: A class to convert NeuroglancerState into a BigDataViewer instance.
 *
 * @author Marwan Zouinkhi &lt;zouinkhim@janelia.hhmi.org&gt;
 */
public class NeuroglancerViewer extends View{

    private NeuroglancerState neuroglancerState;
    private BdvStackSource<?> bdv;
    private BdvOptions options;

    /**
     * Constructor that takes a NeuroglancerState object.
     *
     * @param state The NeuroglancerState to visualize.
          * @throws IOException 
          */
         public NeuroglancerViewer(NeuroglancerState state) throws IOException {
        this.neuroglancerState = state;
        initializeBdv();
    }

    /**
     * Initializes the BigDataViewer with the settings from NeuroglancerState.
          * @throws IOException 
          */
         private void initializeBdv() throws IOException {

        
	

        int maxN = 2;
        int numRenderingThreads = 3;
        double[] screenScales = new double[] {1.0, 0.5, 0.25, 0.125};
        
        options = Bdv.options();
        
		if (maxN == 2)
			options.is2D();
		else if (maxN == 4)
			options.axisOrder(AxisOrder.XYZT);

		options.numRenderingThreads(numRenderingThreads);
		options.screenScales(screenScales);


        // Add layers
        addLayers();

        // Set position and orientation
        setViewTransform();

        // Handle layout if needed (e.g., "4panel")
        setLayout();

    }
    /**
     * Adds layers to the BDV viewer based on the Neuroglancer state.
          * @throws IOException 
          */
         private void addLayers() throws IOException {
        List<Layer> layers = neuroglancerState.getLayers();

        List<ReaderInfo> readerInfos = LayerToReaderInfoConverter.convertLayersToReaderInfos(layers);
        int[] axes = new int[]{0, 1, 2, 3};

        final int numProc = Runtime.getRuntime().availableProcessors();

        final SharedQueue queue = new SharedQueue(Math.min(8, Math.max(1, numProc / 2)));

		int id = 0;
		for (final ReaderInfo entry : readerInfos) {

			final N5Reader n5 = entry.n5;
			for (int i = 0; i < entry.groupNames.length; ++i) {

				final String groupName = entry.groupNames[i];
				final double[] res = entry.resolutions[i];
				final double[] con = entry.contrastRanges[i];
				final boolean isLabel = con == null;
				final double[] off = entry.offsets[i];
				final int[] ax = entry.axess[i];

				System.out.println(n5 + " : " + groupName + ", " + Arrays.toString(res) + ", " + (isLabel ? " labels " : Arrays.toString(con)) + ", " + Arrays.toString(off) + ", Num axes: " + ax.length);

				@SuppressWarnings("rawtypes")
				final Pair<RandomAccessibleInterval<NativeType>[], double[][]> n5Sources;
				int n;
				if (n5.datasetExists(groupName)) {
					// this works for javac openjdk 8
					@SuppressWarnings({"rawtypes"})
					final RandomAccessibleInterval<NativeType> source = (RandomAccessibleInterval)N5Utils.openVolatile(n5, groupName);
					n = source.numDimensions();
					final double[] scale = new double[n];
					Arrays.fill(scale, 1);
					n5Sources = new ValuePair<>(new RandomAccessibleInterval[] {source}, new double[][]{scale});
				}
				else {
					n5Sources = N5Utils.openMipmaps(n5, groupName, true);
					n = n5Sources.getA()[0].numDimensions();
				}

				/* make volatile */
				@SuppressWarnings("rawtypes")
				final RandomAccessibleInterval<NativeType>[] ras = n5Sources.getA();
				@SuppressWarnings("rawtypes")
				final RandomAccessibleInterval[] vras = new RandomAccessibleInterval[ras.length];
				Arrays.setAll(vras, k ->
					VolatileViews.wrapAsVolatile(
							n5Sources.getA()[k],
							queue,
							new CacheHints(LoadingStrategy.VOLATILE, 0, true)));

				/* hyperslice and map axes */
				final int[] allAxes = allAxes(ax, n);

				Arrays.setAll(vras, k -> permuteAll(vras[k], allAxes));

				System.out.println("axes permutation: " + Arrays.toString(ax) + " -> " + Arrays.toString(allAxes));

				for (int d = axes.length; d < n; ++d) {
					for (int k = 0; k < vras.length; ++k)
						vras[k] = Views.hyperSlice(vras[k], ax.length, Math.round(off[allAxes[d]]));
				}
				for (int k = 0; k < vras.length; ++k)
					if (vras[k].numDimensions() < 3)
						vras[k] = Views.addDimension(vras[k], 0, 0);

				final double[] mappedOffset = new double[] {
						off[allAxes[0]],
						off[allAxes[1]],
						ax.length > 2 ? off[allAxes[2]] : 0
				};

				final RandomAccessibleInterval<VolatileDoubleType>[] convertedSources = new RandomAccessibleInterval[n5Sources.getA().length];
				for (int k = 0; k < vras.length; ++k) {
					final Converter<AbstractVolatileNativeRealType<?, ?>, VolatileDoubleType> converter;
					if (isLabel) {
						final int idHash = hash(id);
						converter = (a, b) -> {
							b.setValid(a.isValid());
							if (b.isValid()) {
								final int x = hash(Double.hashCode(a.get().getRealDouble()) ^ idHash);
								final double v = ((double)x / Integer.MAX_VALUE + 1) * 500.0;
								b.setReal(v);
							}
						};
					}
					else
						converter = (a, b) -> {
							b.setValid(a.isValid());
							if (b.isValid()) {
								double v = a.get().getRealDouble();
								v -= con[0];
								v /= con[1] - con[0];
								v *= 1000;
								b.setReal(v);
							}
						};
					convertedSources[k] = Converters.convert(
							(RandomAccessibleInterval<AbstractVolatileNativeRealType<?, ?>>)vras[k],
							converter,
							new VolatileDoubleType());
					final double[] scale = n5Sources.getB()[k];
					final double[] mappedScale = new double[] {
							scale[allAxes[0]] * res[0],
							scale[allAxes[1]] * res[1],
							ax.length > 2 ? scale[allAxes[2]] * res[2] : 1
					};
					n5Sources.getB()[k] = mappedScale;
				}

				/* offset transform */
				final AffineTransform3D sourceTransform = new AffineTransform3D();
				sourceTransform.setTranslation(mappedOffset);
				System.out.println(groupName + " " + sourceTransform.toString());

				final RandomAccessibleIntervalMipmapSource<VolatileDoubleType> mipmapSource =
						new RandomAccessibleIntervalMipmapSource<>(
								convertedSources,
								new VolatileDoubleType(),
								n5Sources.getB(),
								new FinalVoxelDimensions("px", res),
								sourceTransform,
								groupName);

				bdv = BdvFunctions.show(
						mipmapSource,
						bdv == null ? options : options.addTo(bdv));
				bdv.setDisplayRange(0, 1000);
				bdv.setColor(new ARGBType(argb(id++)));
			}

			if (id == 1)
				bdv.setColor(new ARGBType(0xffffffff));
		}

		/* create XTouchMini midi controller */
		try {
//			final XTouchMiniBDVControl controller = new XTouchMiniBDVControl(bdv.getBdvHandle().getViewerPanel());
			final MCUBDVControls controls = new MCUBDVControls(
					bdv.getBdvHandle().getViewerPanel(),
					XTouchMiniMCUControlPanel.build());

		} catch (final Exception e) {}

		((JFrame)SwingUtilities.getWindowAncestor(bdv.getBdvHandle().getViewerPanel())).setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        
    }

    /**
     * Sets the viewer's transform (position, orientation, scale) based on Neuroglancer state.
     */
    private void setViewTransform() {
        // Extract position, projection orientation, and projection scale
        List<Double> position = neuroglancerState.getPosition();
        List<Double> projectionOrientation = neuroglancerState.getProjectionOrientation();
        double projectionScale = neuroglancerState.getProjectionScale();

        // Create AffineTransform3D
        AffineTransform3D transform = new AffineTransform3D();

        // Set translation
        transform.setTranslation(new double[]{
                position.get(0),
                position.get(1),
                position.get(2)
        });

        // Set rotation based on projection orientation (quaternion to rotation matrix)
        double[] quaternion = new double[]{
                projectionOrientation.get(0),
                projectionOrientation.get(1),
                projectionOrientation.get(2),
                projectionOrientation.get(3)
        };
        AffineTransform3D rotation = quaternionToAffineTransform3D(quaternion);
        transform.preConcatenate(rotation);

        // Set scale
        transform.scale(projectionScale);

        // Apply transform to BDV
        // bdvHandle.getViewerTransform().set(transform);
    }

    /**
     * Converts a quaternion to AffineTransform3D.
     *
     * @param q The quaternion [x, y, z, w].
     * @return The AffineTransform3D representing the rotation.
     */
    private AffineTransform3D quaternionToAffineTransform3D(double[] q) {
        AffineTransform3D transform = new AffineTransform3D();
        double x = q[0];
        double y = q[1];
        double z = q[2];
        double w = q[3];

        // Normalize the quaternion
        double norm = Math.sqrt(x * x + y * y + z * z + w * w);
        x /= norm;
        y /= norm;
        z /= norm;
        w /= norm;

        // Compute rotation matrix
        double xx = x * x;
        double xy = x * y;
        double xz = x * z;
        double xw = x * w;

        double yy = y * y;
        double yz = y * z;
        double yw = y * w;

        double zz = z * z;
        double zw = z * w;

        double[][] rotationMatrix = new double[][]{
                {1 - 2 * (yy + zz), 2 * (xy - zw), 2 * (xz + yw)},
                {2 * (xy + zw), 1 - 2 * (xx + zz), 2 * (yz - xw)},
                {2 * (xz - yw), 2 * (yz + xw), 1 - 2 * (xx + yy)}
        };

        // Set rotation in AffineTransform3D
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                transform.set(row, col, rotationMatrix[row][col]);
            }
        }

        return transform;
    }

    /**
     * Sets the viewer's layout based on Neuroglancer state.
     * Currently supports "4panel" layout.
     */
    private void setLayout() {
        String layout = neuroglancerState.getLayout();
        if ("4panel".equalsIgnoreCase(layout)) {
            // Implement 4-panel layout if required
            // BDV does not natively support multiple views, but you can create multiple BDV instances
            // Or use BDV's built-in split view capabilities if available
            // For simplicity, this example does not implement multi-panel layouts
            System.out.println("4-panel layout is not implemented in this example.");
        }
    }



    /**
     * Example main method to demonstrate usage.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        // Example usage: java NeuroglancerViewer path/to/neuroglancer_state.json
        if (args.length != 1) {
            System.err.println("Usage: java NeuroglancerViewer <path_to_neuroglancer_state.json>");
            System.exit(1);
        }

        String jsonPath = args[0];
        ObjectMapper mapper = new ObjectMapper();
        try {
            NeuroglancerState state = mapper.readValue(new File(jsonPath), NeuroglancerState.class);
            new NeuroglancerViewer(state);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
