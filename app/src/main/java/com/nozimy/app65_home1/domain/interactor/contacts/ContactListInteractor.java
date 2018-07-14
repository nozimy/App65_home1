package com.nozimy.app65_home1.domain.interactor.contacts;

import android.support.annotation.NonNull;

import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.domain.interactor.BaseInteractor;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface ContactListInteractor extends BaseInteractor {

    @NonNull
    Single<List<Contact>> getContacts();
    @NonNull
    Single<List<Contact>> getByDisplayName(String searchText);

    Completable importFromProvider();
    
}
