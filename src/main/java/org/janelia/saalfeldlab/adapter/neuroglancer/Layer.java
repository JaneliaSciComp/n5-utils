package org.janelia.saalfeldlab.adapter.neuroglancer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore properties not defined in this class
public class Layer {

    @JsonProperty("type")
    private String type;

    @JsonProperty("source")
    private String source;

    @JsonProperty("opacity")
    private Double opacity;

    @JsonProperty("tab")
    private String tab;

    @JsonProperty("name")
    private String name;

    @JsonProperty("segments")
    private List<String> segments;

    @JsonProperty("visible")
    private Boolean visible = true;

    @JsonProperty("crossSectionRenderScale")
    private Double crossSectionRenderScale;

    @JsonProperty("segmentQuery")
    private String segmentQuery;


    // Getters and Setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public List<String> getSegments() {
        return segments;
    }

    public void setSegments(List<String> segments) {
        this.segments = segments;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
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

    public Double getOpacity() {
        return opacity;
    }

    public void setOpacity(Double opacity) {
        this.opacity = opacity;
    }
}
