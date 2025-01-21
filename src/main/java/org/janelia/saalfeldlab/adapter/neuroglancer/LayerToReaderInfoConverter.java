package org.janelia.saalfeldlab.adapter.neuroglancer;

import org.janelia.saalfeldlab.View.ReaderInfo;
import org.janelia.saalfeldlab.n5.N5Reader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Converter class to transform Layer objects into ReaderInfo instances.
 */
public class LayerToReaderInfoConverter {

    /**
     * Converts a list of Layer objects into a list of ReaderInfo instances, grouped by container path.
     *
     * @param layers The list of Layer objects from Neuroglancer state.
     * @return A list of ReaderInfo instances.
     * @throws IOException If an N5Reader cannot be opened.
     */
    public static List<ReaderInfo> convertLayersToReaderInfos(List<Layer> layers) throws IOException {
        if (layers == null || layers.isEmpty()) {
            return Collections.emptyList();
        }

        // Group layers by containerPath
        Map<String, List<Layer>> layersByContainer = layers.stream()
                .collect(Collectors.groupingBy(layer -> {
                    try {
                        return LayerUtils.parseSource(layer.getSource())[0];
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Error parsing layer source: " + layer.getSource(), e);
                    }
                }));

        List<ReaderInfo> readerInfos = new ArrayList<>();

        // Iterate over each container path group
        for (Map.Entry<String, List<Layer>> entry : layersByContainer.entrySet()) {
            String containerPath = entry.getKey();
            List<Layer> containerLayers = entry.getValue();

            // Open N5Reader for this container
            N5Reader n5Reader = LayerUtils.openN5Reader(containerPath);

            // Initialize arrays to hold group-specific information
            int numGroups = containerLayers.size();
            String[] groupNames = new String[numGroups];
            double[][] resolutions = new double[numGroups][];
            double[][] contrastRanges = new double[numGroups][];
            double[][] offsets = new double[numGroups][];
            int[][] axess = new int[numGroups][];

            // Populate arrays based on each Layer
            for (int i = 0; i < numGroups; i++) {
                Layer layer = containerLayers.get(i);
                String[] parsedSource = LayerUtils.parseSource(layer.getSource());
                String groupName = parsedSource[1];

                groupNames[i] = groupName;

                // Set default resolutions, can be customized based on Layer properties
                // For example, using crossSectionRenderScale if available
                double[] resolution = new double[]{1.0, 1.0, 1.0};
                if (layer.getCrossSectionRenderScale() != null) {
                    double scale = layer.getCrossSectionRenderScale();
                    resolution[0] *= scale;
                    resolution[1] *= scale;
                    resolution[2] *= scale;
                }
                resolutions[i] = resolution;

                // Set contrast ranges, default to [0, 255] unless specified
                double[] contrastRange = new double[]{0.0, 255.0};
                // You can extend this to read contrast ranges from Layer properties if available
                contrastRanges[i] = contrastRange;

                // Set offsets, default to [0, 0, 0]
                double[] offset = new double[]{0.0, 0.0, 0.0};
                // You can extend this to read offsets from Layer properties if available
                offsets[i] = offset;

                // Set axes, default to [0, 1, 2]
                int[] axes = new int[]{0, 1, 2};
                // You can extend this to read axes from Layer properties if available
                axess[i] = axes;
            }

            // Create ReaderInfo instance for this container
            ReaderInfo readerInfo = new ReaderInfo(
                    n5Reader,
                    groupNames,
                    resolutions,
                    contrastRanges,
                    offsets,
                    axess
            );

            readerInfos.add(readerInfo);
        }

        return readerInfos;
    }
}
