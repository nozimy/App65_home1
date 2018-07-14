package com.nozimy.app65_home1.network.route.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
