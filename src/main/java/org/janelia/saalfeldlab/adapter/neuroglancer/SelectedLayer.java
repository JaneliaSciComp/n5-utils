package org.janelia.saalfeldlab.adapter.neuroglancer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectedLayer {

    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("layer")
    private String layer;

    // Getters and Setters

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }
}
