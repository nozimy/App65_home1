package com.nozimy.app65_home1.network.route.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {
    @SerializedName("summary")
    private String summary;
    @SerializedName("legs")
    private List<Leg> legs = null;

    public String getSummary() {
        return summary;
    }

    public List<Leg> getLegs() {
        return legs;
    }
}
