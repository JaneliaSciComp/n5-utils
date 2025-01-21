package org.janelia.saalfeldlab.adapter.neuroglancer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore properties not defined in this class
public class NeuroglancerState {

    @JsonProperty("dimensions")
    private Dimensions dimensions;

    @JsonProperty("position")
    private List<Double> position;

    @JsonProperty("crossSectionScale")
    private double crossSectionScale;

    @JsonProperty("projectionOrientation")
    private List<Double> projectionOrientation;

    @JsonProperty("projectionScale")
    private double projectionScale;

    @JsonProperty("layers")
    private List<Layer> layers;

    @JsonProperty("selectedLayer")
    private SelectedLayer selectedLayer;

    @JsonProperty("layout")
    private String layout;

    // Getters and Setters

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public List<Double> getPosition() {
        return position;
    }

    public void setPosition(List<Double> position) {
        this.position = position;
    }

    public double getCrossSectionScale() {
        return crossSectionScale;
    }

    public void setCrossSectionScale(double crossSectionScale) {
        this.crossSectionScale = crossSectionScale;
    }

    public List<Double> getProjectionOrientation() {
        return projectionOrientation;
    }

    public void setProjectionOrientation(List<Double> projectionOrientation) {
        this.projectionOrientation = projectionOrientation;
    }

    public double getProjectionScale() {
        return projectionScale;
    }

    public void setProjectionScale(double projectionScale) {
        this.projectionScale = projectionScale;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public SelectedLayer getSelectedLayer() {
        return selectedLayer;
    }

    public void setSelectedLayer(SelectedLayer selectedLayer) {
        this.selectedLayer = selectedLayer;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
}
