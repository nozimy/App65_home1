package com.nozimy.app65_home1.network.geocode.model;

import com.google.gson.annotations.SerializedName;

public class FeatureMember {

    @SerializedName("GeoObject")
    private GeoObject geoObject;

    public GeoObject getGeoObject() {
        return geoObject;
    }
}
