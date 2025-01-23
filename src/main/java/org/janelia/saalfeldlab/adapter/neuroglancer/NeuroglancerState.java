package org.janelia.saalfeldlab.adapter.neuroglancer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the complete state of a Neuroglancer instance.
 * This class aligns with the ViewerState schema defined in viewer_state.yaml.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore properties not defined in this class
public class NeuroglancerState {

    @JsonProperty("dimensions")
    private Dimensions dimensions;

    @JsonProperty("relativeDisplayScales")
    private List<Double> relativeDisplayScales;

    @JsonProperty("displayDimensions")
    private List<String> displayDimensions;

    @JsonProperty("position")
    private List<Double> position;

    @JsonProperty("crossSectionOrientation")
    private List<Double> crossSectionOrientation;

    @JsonProperty("crossSectionScale")
    private Double crossSectionScale;

    @JsonProperty("crossSectionDepth")
    private Double crossSectionDepth;

    @JsonProperty("projectionOrientation")
    private List<Double> projectionOrientation;

    @JsonProperty("projectionScale")
    private Double projectionScale;

    @JsonProperty("projectionDepth")
    private Double projectionDepth;

    @JsonProperty("layers")
    private List<Layer> layers;

    @JsonProperty("showAxisLines")
    private Boolean showAxisLines = true;

    @JsonProperty("wireFrame")
    private Boolean wireFrame = false;

    @JsonProperty("showScaleBar")
    private Boolean showScaleBar = true;

    @JsonProperty("showDefaultAnnotations")
    private Boolean showDefaultAnnotations = true;

    @JsonProperty("showSlices")
    private Boolean showSlices = true;

    @JsonProperty("gpuMemoryLimit")
    private Long gpuMemoryLimit;

    @JsonProperty("systemMemoryLimit")
    private Long systemMemoryLimit;

    @JsonProperty("concurrentDownloads")
    private Integer concurrentDownloads;

    @JsonProperty("prefetch")
    private Boolean prefetch = true;

    @JsonProperty("title")
    private String title;

    @JsonProperty("layout")
    private String layout;

    @JsonProperty("selectedLayer")
    private SelectedLayer selectedLayer;

    // Getters and Setters

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public List<Double> getRelativeDisplayScales() {
        return relativeDisplayScales;
    }

    public void setRelativeDisplayScales(List<Double> relativeDisplayScales) {
        this.relativeDisplayScales = relativeDisplayScales;
    }

    public List<String> getDisplayDimensions() {
        return displayDimensions;
    }

    public void setDisplayDimensions(List<String> displayDimensions) {
        this.displayDimensions = displayDimensions;
    }

    public List<Double> getPosition() {
        return position;
    }

    public void setPosition(List<Double> position) {
        this.position = position;
    }

    public List<Double> getCrossSectionOrientation() {
        return crossSectionOrientation;
    }

    public void setCrossSectionOrientation(List<Double> crossSectionOrientation) {
        this.crossSectionOrientation = crossSectionOrientation;
    }

    public Double getCrossSectionScale() {
        return crossSectionScale;
    }

    public void setCrossSectionScale(Double crossSectionScale) {
        this.crossSectionScale = crossSectionScale;
    }

    public Double getCrossSectionDepth() {
        return crossSectionDepth;
    }

    public void setCrossSectionDepth(Double crossSectionDepth) {
        this.crossSectionDepth = crossSectionDepth;
    }

    public List<Double> getProjectionOrientation() {
        return projectionOrientation;
    }

    public void setProjectionOrientation(List<Double> projectionOrientation) {
        this.projectionOrientation = projectionOrientation;
    }

    public Double getProjectionScale() {
        return projectionScale;
    }

    public void setProjectionScale(Double projectionScale) {
        this.projectionScale = projectionScale;
    }

    public Double getProjectionDepth() {
        return projectionDepth;
    }

    public void setProjectionDepth(Double projectionDepth) {
        this.projectionDepth = projectionDepth;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public Boolean getShowAxisLines() {
        return showAxisLines;
    }

    public void setShowAxisLines(Boolean showAxisLines) {
        this.showAxisLines = showAxisLines;
    }

    public Boolean getWireFrame() {
        return wireFrame;
    }

    public void setWireFrame(Boolean wireFrame) {
        this.wireFrame = wireFrame;
    }

    public Boolean getShowScaleBar() {
        return showScaleBar;
    }

    public void setShowScaleBar(Boolean showScaleBar) {
        this.showScaleBar = showScaleBar;
    }

    public Boolean getShowDefaultAnnotations() {
        return showDefaultAnnotations;
    }

    public void setShowDefaultAnnotations(Boolean showDefaultAnnotations) {
        this.showDefaultAnnotations = showDefaultAnnotations;
    }

    public Boolean getShowSlices() {
        return showSlices;
    }

    public void setShowSlices(Boolean showSlices) {
        this.showSlices = showSlices;
    }

    public Long getGpuMemoryLimit() {
        return gpuMemoryLimit;
    }

    public void setGpuMemoryLimit(Long gpuMemoryLimit) {
        this.gpuMemoryLimit = gpuMemoryLimit;
    }

    public Long getSystemMemoryLimit() {
        return systemMemoryLimit;
    }

    public void setSystemMemoryLimit(Long systemMemoryLimit) {
        this.systemMemoryLimit = systemMemoryLimit;
    }

    public Integer getConcurrentDownloads() {
        return concurrentDownloads;
    }

    public void setConcurrentDownloads(Integer concurrentDownloads) {
        this.concurrentDownloads = concurrentDownloads;
    }

    public Boolean getPrefetch() {
        return prefetch;
    }

    public void setPrefetch(Boolean prefetch) {
        this.prefetch = prefetch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public SelectedLayer getSelectedLayer() {
        return selectedLayer;
    }

    public void setSelectedLayer(SelectedLayer selectedLayer) {
        this.selectedLayer = selectedLayer;
    }

    /**
     * Nested class representing the Dimensions object.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dimensions {
        // Assuming dimensions are dynamic with keys like "x", "y", "z"
        // Each key maps to a list containing scale and unit
        @JsonProperty("x")
        private List<Object> x;

        @JsonProperty("y")
        private List<Object> y;

        @JsonProperty("z")
        private List<Object> z;

        // Getters and Setters

        public List<Object> getX() {
            return x;
        }

        public void setX(List<Object> x) {
            this.x = x;
        }

        public List<Object> getY() {
            return y;
        }

        public void setY(List<Object> y) {
            this.y = y;
        }

        public List<Object> getZ() {
            return z;
        }

        public void setZ(List<Object> z) {
            this.z = z;
        }
    }

    /**
     * Nested class representing the SelectedLayer object.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SelectedLayer {
        @JsonProperty("visible")
        private Boolean visible;

        @JsonProperty("layer")
        private String layer;

        // Getters and Setters

        public Boolean getVisible() {
            return visible;
        }

        public void setVisible(Boolean visible) {
            this.visible = visible;
        }

        public String getLayer() {
            return layer;
        }

        public void setLayer(String layer) {
            this.layer = layer;
        }
    }


}
