package com.nozimy.app65_home1.network.route.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("error_message")
    private String errorMessage;
    @SerializedName("routes")
    private List<Route> routes = null;
    public String getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
