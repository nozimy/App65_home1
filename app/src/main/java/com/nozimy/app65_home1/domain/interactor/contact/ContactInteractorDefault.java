package com.nozimy.app65_home1.domain.interactor.contact;

import android.support.annotation.NonNull;

import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.db.entity.EmailEntity;
import com.nozimy.app65_home1.db.entity.PhoneEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class ContactInteractorDefault implements ContactInteractor {

    @NonNull
    private DataRepository dataRepository;

    @Inject
    public ContactInteractorDefault(@NonNull DataRepository dataRepository){
        this.dataRepository = dataRepository;
    }

    @NonNull
    @Override
    public Maybe<Contact> getContact(String contactId) {
        return Maybe.defer(() -> dataRepository.getContact(contactId));
    }

    @NonNull
    @Override
    public Maybe<List<Phone>> getPhones(String contactId) {
        return Maybe.defer(() -> dataRepository.getPhones(contactId));
    }

    @NonNull
    @Override
    public Maybe<List<Email>> getEmails(String contactId) {
        return Maybe.defer(() -> dataRepository.getEmails(contactId));
    }

}
