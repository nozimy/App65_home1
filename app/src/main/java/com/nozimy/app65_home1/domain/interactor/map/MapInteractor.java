package com.nozimy.app65_home1.domain.interactor.map;

import android.support.annotation.NonNull;

import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.network.geocode.model.GeoResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface MapInteractor {

    @NonNull
    Maybe<Contact> getContact(String contactId);

    @NonNull
    Single<List<Contact>> getContacts();

    Completable updateContactAddress(String id, double lat, double lng, String address);

    Single<GeoResponse> getGeoObjects(double lat, double lng);

}
