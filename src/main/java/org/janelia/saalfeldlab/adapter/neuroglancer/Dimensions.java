package org.janelia.saalfeldlab.adapter.neuroglancer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Dimensions {

    @JsonProperty("x")
    private List<Object> x; // [number, "m"]

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
