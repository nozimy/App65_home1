package com.nozimy.app65_home1.domain.interactor.route;

import android.support.annotation.NonNull;

import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.network.route.model.RouteResponse;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface RouteInteractor {

    Single<RouteResponse> getRoute(String origin, String destination);

    @NonNull
    Maybe<Contact> getContact(String contactId);

}
