package com.nozimy.app65_home1.domain.interactor.route;

import android.support.annotation.NonNull;

import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.network.route.RouteApiClient;
import com.nozimy.app65_home1.network.route.model.RouteResponse;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class RouteInteractorуDefault implements RouteInteractor {

    private RouteApiClient routeApiClient;
    @NonNull
    private DataRepository dataRepository;

    public RouteInteractorуDefault(RouteApiClient routeApiClient, DataRepository dataRepository) {
        this.routeApiClient = routeApiClient;
        this.dataRepository = dataRepository;
    }

    @Override
    public Single<RouteResponse> getRoute(String origin, String destination) {
        return Single.defer(() -> routeApiClient.getRoute(origin, destination));
    }

    @NonNull
    @Override
    public Maybe<Contact> getContact(String contactId) {
        return Maybe.defer(() -> dataRepository.getContact(contactId));
    }
}
