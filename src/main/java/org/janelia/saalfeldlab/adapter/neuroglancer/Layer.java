package org.janelia.saalfeldlab.adapter.neuroglancer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a Layer within a Neuroglancer instance.
 * This class aligns with the Layer schema defined in layer.yaml.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore properties not defined in this class
public class Layer {

    /**
     * Specifies the layer type.
     * This field is optional as per the schema.
     */
    @JsonProperty("type")
    private String type;

    /**
     * Specifies the layer name to show in the UI.
     * This field is optional as per the schema.
     */
    @JsonProperty("name")
    private String name;

    /**
     * Indicates whether the layer is displayed in 2D and 3D projections.
     * Defaults to true if not specified.
     */
    @JsonProperty("visible")
    private Boolean visible = true;

    /**
     * Specifies the data source for the layer.
     * Optional field to accommodate additional properties.
     */
    @JsonProperty("source")
    private String source;

    /**
     * Opacity of the layer (0.0 to 1.0).
     * Optional field.
     */
    @JsonProperty("opacity")
    private Double opacity;

    /**
     * Blend mode for the layer (e.g., "additive").
     * Optional field.
     */
    @JsonProperty("blend")
    private String blend;

    /**
     * Shader code for custom rendering.
     * Optional field.
     */
    @JsonProperty("shader")
    private String shader;

    /**
     * Tab in the UI where the layer is displayed.
     * Optional field.
     */
    @JsonProperty("tab")
    private String tab;

    /**
     * List of segments associated with the layer.
     * Optional field.
     */
    @JsonProperty("segments")
    private List<String> segments;

    /**
     * Scale factor for cross-section rendering.
     * Optional field.
     */
    @JsonProperty("crossSectionRenderScale")
    private Double crossSectionRenderScale;

    /**
     * Query string for segment filtering.
     * Optional field.
     */
    @JsonProperty("segmentQuery")
    private String segmentQuery;

    // Getters and Setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getOpacity() {
        return opacity;
    }

    public void setOpacity(Double opacity) {
        this.opacity = opacity;
    }

    public String getBlend() {
        return blend;
    }

    public void setBlend(String blend) {
        this.blend = blend;
    }

    public String getShader() {
        return shader;
    }

    public void setShader(String shader) {
        this.shader = shader;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public List<String> getSegments() {
        return segments;
    }

    public void setSegments(List<String> segments) {
        this.segments = segments;
    }

    public Double getCrossSectionRenderScale() {
        return crossSectionRenderScale;
    }

    public void setCrossSectionRenderScale(Double crossSectionRenderScale) {
        this.crossSectionRenderScale = crossSectionRenderScale;
    }

    public String getSegmentQuery() {
        return segmentQuery;
    }

    public void setSegmentQuery(String segmentQuery) {
        this.segmentQuery = segmentQuery;
    }
}
