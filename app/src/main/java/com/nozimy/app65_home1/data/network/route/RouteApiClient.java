package com.nozimy.app65_home1.network.route;

import com.nozimy.app65_home1.network.route.model.RouteResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RouteApiClient {
    String DIRECTIONS_KEY = "AIzaSyBrHFDhgaD9vyWTNs3-Qw6Fu0GRb8Q_w88";

    @GET("/maps/api/directions/json?key="+DIRECTIONS_KEY)
    Single<RouteResponse> getRoute(@Query("origin") String origin,
                                   @Query("destination") String destination);
}
