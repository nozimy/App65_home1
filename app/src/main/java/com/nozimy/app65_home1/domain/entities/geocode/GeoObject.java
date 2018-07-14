package com.nozimy.app65_home1.network.geocode.model;

import com.google.gson.annotations.SerializedName;

public class GeoObject {

    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
