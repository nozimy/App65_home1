package com.nozimy.app65_home1.domain.interactor.contacts;

import android.support.annotation.NonNull;

import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.db.entity.ContactEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ContactListInteractorDefault implements ContactListInteractor {

    @NonNull
    private DataRepository dataRepository;

    @NonNull
    private ImportService importService;

    @Inject
    public ContactListInteractorDefault(@NonNull DataRepository dataRepository, @NonNull ImportService importService){
        this.dataRepository = dataRepository;
        this.importService = importService;
    }

    @NonNull
    @Override
    public Single<List<ContactEntity>> getContacts() {
        return Single.defer(() -> dataRepository.getContactsRx());
    }

    @NonNull
    @Override
    public Single<List<ContactEntity>> getByDisplayName(String searchText) {
        return Single.defer(() -> dataRepository.getByDisplayNameRx(searchText));
    }

    @Override
    public Completable importFromProvider() {
        return dataRepository.importFromProvider(importService);
    }


}
