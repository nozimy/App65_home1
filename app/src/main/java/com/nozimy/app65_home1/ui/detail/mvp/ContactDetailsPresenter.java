package com.nozimy.app65_home1.ui.detail.mvp;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.nozimy.app65_home1.DataRepository;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.db.entity.EmailEntity;
import com.nozimy.app65_home1.db.entity.PhoneEntity;
import com.nozimy.app65_home1.ui.common.mvp.BasePresenter;
import com.nozimy.app65_home1.utils.CommonUtils;

import java.util.List;

import io.reactivex.Flowable;

public class ContactDetailsPresenter<V extends ContactDetailsContract.View> extends BasePresenter<V> implements ContactDetailsContract.Presenter<V> {

    private final String contactId;
    private final Flowable<ContactEntity> observableContact;
    private final Flowable<List<PhoneEntity>> observablePhones;
    private final Flowable<List<EmailEntity>> observableEmails;


    public ContactDetailsPresenter(DataRepository repository, String contactId) {
        super(repository);
        Log.d("CONTACT ID", contactId);
        this.contactId = contactId;
        observableContact = getDataRepository().getContact(this.contactId);
        observablePhones = getDataRepository().getPhones(this.contactId);
        observableEmails = getDataRepository().getEmails(this.contactId);
    }

    public Flowable<ContactEntity> getContact(){
        return observableContact;
    }

    public Flowable<List<PhoneEntity>> getPhones(){
        return observablePhones;
    }

    public Flowable<List<EmailEntity>> getEmails(){
        return observableEmails;
    }

    @Override
    public void loadDetails() {
        getMvpView().subscribeUi();
    }

}

