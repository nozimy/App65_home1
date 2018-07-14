package com.nozimy.app65_home1.network.geocode;

import com.nozimy.app65_home1.network.geocode.model.GeoResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodeApiClient {

    @GET("/1.x/?format=json")
    Single<GeoResponse> getGeoObjects(@Query("geocode") String geocode);
}
