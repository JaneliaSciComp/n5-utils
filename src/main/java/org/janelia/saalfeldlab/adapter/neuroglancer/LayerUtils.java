package org.janelia.saalfeldlab.adapter.neuroglancer;

import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.universe.N5Factory;

import java.util.Objects;

/**
 * Utility class for parsing Layer sources and handling N5Readers.
 */
public class LayerUtils {

    /**
     * Parses the source URL from a Layer to extract the container path and group name.
     *
     * @param source The source URL from the Layer (e.g., "zarr://<container_path>/<group_name>/").
     * @return A Pair containing the container path and group name.
     * @throws IllegalArgumentException If the source format is unsupported or invalid.
     */
    public static String[] parseSource(String source) {
        Objects.requireNonNull(source, "Source cannot be null");

        String containerPath;
        String groupName;

        if (source.startsWith("zarr://")) {
            String pathWithoutProtocol = source.substring("zarr://".length());
            int zarrIndex = pathWithoutProtocol.indexOf(".zarr");
            if (zarrIndex == -1) {
                throw new IllegalArgumentException("Source path does not contain '.zarr': " + source);
            }
            containerPath = pathWithoutProtocol.substring(0, zarrIndex + ".zarr".length());
            groupName = pathWithoutProtocol.substring(zarrIndex + ".zarr".length());
        } else if (source.startsWith("n5://")) {
            String pathWithoutProtocol = source.substring("n5://".length());
            int n5Index = pathWithoutProtocol.indexOf(".n5");
            if (n5Index == -1) {
                throw new IllegalArgumentException("Source path does not contain '.n5': " + source);
            }
            containerPath = pathWithoutProtocol.substring(0, n5Index + ".n5".length());
            groupName = pathWithoutProtocol.substring(n5Index + ".n5".length());
        } else {
            throw new IllegalArgumentException("Unsupported source protocol: " + source);
        }

        // Clean up groupName by removing leading slashes
        groupName = groupName.startsWith("/") ? groupName.substring(1) : groupName;

        return new String[]{containerPath, groupName};
    }

    public static N5Reader openN5Reader(String path) {
            return new N5Factory()
					.hdf5DefaultBlockSize(64)
					.openReader(path);
    }
}
