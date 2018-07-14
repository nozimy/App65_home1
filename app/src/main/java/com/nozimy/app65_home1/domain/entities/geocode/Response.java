package com.nozimy.app65_home1.network.geocode.model;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("GeoObjectCollection")
    private GeoObjectCollection geoObjectCollection;

    public GeoObjectCollection getGeoObjectCollection() {
        return geoObjectCollection;
    }

    public void setGeoObjectCollection(GeoObjectCollection geoObjectCollection) {
        this.geoObjectCollection = geoObjectCollection;
    }
}
