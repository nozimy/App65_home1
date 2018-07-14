package com.nozimy.app65_home1.network.route.model;

import com.google.gson.annotations.SerializedName;

public class Step {
    @SerializedName("start_location")
    private Location startLocation;
    @SerializedName("end_location")
    private Location endLocation;

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }
}
