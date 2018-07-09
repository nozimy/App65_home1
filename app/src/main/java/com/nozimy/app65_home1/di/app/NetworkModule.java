package com.nozimy.app65_home1.di.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nozimy.app65_home1.network.geocode.GeocodeApiClient;
import com.nozimy.app65_home1.network.route.RouteApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    static final String GEOCODE_BASE_URL = "https://geocode-maps.yandex.ru/";
    static final String ROUTE_BASE_URL = "https://maps.googleapis.com/";

    @Singleton
    @Provides
    public Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    @Singleton
    @Provides
    public GeocodeApiClient provideGeocodeApi(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(GEOCODE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(GeocodeApiClient.class);
    }

    @Singleton
    @Provides
    public RouteApiClient provideRouteApiClient(Gson gson){
        return new Retrofit.Builder()
                .baseUrl(ROUTE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(RouteApiClient.class);
    }


}
