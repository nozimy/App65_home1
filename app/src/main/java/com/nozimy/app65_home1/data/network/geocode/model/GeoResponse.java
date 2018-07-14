package com.nozimy.app65_home1.network.geocode.model;

import com.google.gson.annotations.SerializedName;

public class GeoResponse {
    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }
}
