package com.nozimy.app65_home1.domain.interactor.contact;

import android.support.annotation.NonNull;

import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.db.entity.EmailEntity;
import com.nozimy.app65_home1.db.entity.PhoneEntity;
import com.nozimy.app65_home1.domain.interactor.BaseInteractor;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface ContactInteractor extends BaseInteractor{

    @NonNull
    Maybe<ContactEntity> getContact(String contactId);
    @NonNull
    Maybe<List<PhoneEntity>> getPhones(String contactId);
    @NonNull
    Maybe<List<EmailEntity>> getEmails(String contactId);

}
