package com.nozimy.app65_home1.domain.interactor.map;

import android.support.annotation.NonNull;

import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.network.geocode.GeocodeApiClient;
import com.nozimy.app65_home1.network.geocode.model.GeoResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class MapInteractorDefault implements MapInteractor {

    @NonNull
    private DataRepository dataRepository;
    @NonNull GeocodeApiClient geocodeApiClient;

    public MapInteractorDefault(@NonNull DataRepository dataRepository, GeocodeApiClient geocodeApiClient) {
        this.dataRepository = dataRepository;
        this.geocodeApiClient = geocodeApiClient;
    }

    @NonNull
    @Override
    public Maybe<ContactEntity> getContact(String contactId) {
        return Maybe.defer(() -> dataRepository.getContact(contactId));
    }

    @NonNull
    @Override
    public Single<List<ContactEntity>> getContacts() {
        return Single.defer(() -> dataRepository.getContactsRx());
    }

    @Override
    public Completable updateContactAddress(String id, double lat, double lng, String address) {
        return Completable.fromAction(() -> dataRepository.updateContactAddress(id, lat, lng, address));
    }

    @Override
    public Single<GeoResponse> getGeoObjects(double lat, double lng) {
        return Single.defer(() -> geocodeApiClient.getGeoObjects(lng+","+lat));
    }
}
