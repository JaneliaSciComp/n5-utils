package org.janelia.saalfeldlab.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.janelia.saalfeldlab.View.ReaderInfo;
import org.janelia.saalfeldlab.adapter.neuroglancer.Layer;
import org.janelia.saalfeldlab.adapter.neuroglancer.LayerToReaderInfoConverter;
import org.janelia.saalfeldlab.adapter.neuroglancer.NeuroglancerState;
import org.janelia.saalfeldlab.adapter.neuroglancer.NeuroglancerViewer;

public class NeuroglancerJsonAdapter {

    private ObjectMapper objectMapper;

    public NeuroglancerJsonAdapter() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Imports Neuroglancer state from a JSON file.
     *
     * @param filePath Path to the JSON file.
     * @return NeuroglancerState object.
     * @throws IOException If there's an issue reading the file.
     */
    public NeuroglancerState importFromJson(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), NeuroglancerState.class);
    }

    /**
     * Exports Neuroglancer state to a JSON file.
     *
     * @param state    NeuroglancerState object to export.
     * @param filePath Path to the output JSON file.
     * @throws IOException If there's an issue writing to the file.
     */
    public void exportToJson(NeuroglancerState state, String filePath) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), state);
    }

    /**
     * Imports Neuroglancer state from a JSON string.
     *
     * @param json JSON string.
     * @return NeuroglancerState object.
     * @throws IOException If there's an issue parsing the JSON.
     */
    public NeuroglancerState importFromJsonString(String json) throws IOException {
        return objectMapper.readValue(json, NeuroglancerState.class);
    }

    /**
     * Exports Neuroglancer state to a JSON string.
     *
     * @param state NeuroglancerState object to export.
     * @return JSON string.
     * @throws IOException If there's an issue generating the JSON.
     */
    public String exportToJsonString(NeuroglancerState state) throws IOException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(state);
    }

    public static void main(String[] args) {
        String filePath = "/Users/zouinkhim/Desktop/java/n5-utils/src/main/resources/neuroglancer_state.json";
        NeuroglancerJsonAdapter adapter = new NeuroglancerJsonAdapter();
        try {
            NeuroglancerState neuroglancerState = adapter.importFromJson(filePath);
            System.out.println(adapter.exportToJsonString(neuroglancerState));

            List<Layer> layers = neuroglancerState.getLayers();

            List<ReaderInfo> readerInfos = LayerToReaderInfoConverter.convertLayersToReaderInfos(layers);
            NeuroglancerViewer viewer = new NeuroglancerViewer(readerInfos);


            
        } catch (IOException e) {
            e.printStackTrace();
    }
}
}

