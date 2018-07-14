package com.nozimy.app65_home1.ui.contact.mvp;

import android.util.Log;

import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.db.entity.EmailEntity;
import com.nozimy.app65_home1.db.entity.PhoneEntity;
import com.nozimy.app65_home1.domain.interactor.contact.ContactInteractor;
import com.nozimy.app65_home1.ui.common.mvp.BasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactPresenter<V extends ContactDetailsContract.View> extends BasePresenter<V> 
    implements ContactDetailsContract.Presenter<V> {

    private ContactEntity contactEntity;

    private ContactInteractor contactInteractor;
    private final CompositeDisposable mDisposable;


    public ContactPresenter(ContactInteractor contactInteractor, 
                            CompositeDisposable compositeDisposable) {
        this.contactInteractor = contactInteractor;
        this.mDisposable = compositeDisposable;

    }

    @Override
    public void getContact(String contactId) {
        Log.d("PRESENTER CONTACT ID", contactId);
        Disposable disposable = Maybe.zip(contactInteractor.getContact(contactId),
                contactInteractor.getPhones(contactId),
                contactInteractor.getEmails(contactId),
                Maybe.timer(1000, TimeUnit.MILLISECONDS),
                (contact,phones,emails, timerValue)-> new Object[]{contact,phones,emails})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__-> getMvpView().setProgressBarVisible())
                .subscribe(data -> {
                    if(data != null){
                        Object[] arr = (Object[])data;
                        contactEntity = (ContactEntity) arr[0];
                        List<PhoneEntity> phoneEntities = (List<PhoneEntity>) arr[1];
                        List<EmailEntity> emailEntities = (List<EmailEntity>) arr[2];
                        showContact(contactEntity, phoneEntities, emailEntities);
                    }
                },
                        Throwable::printStackTrace,
                        () -> getMvpView().setProgressBarGone());
        mDisposable.add(disposable);
    }

    @Override
    public void clearDisposable() {
        mDisposable.clear();
    }

    private void showContact(ContactEntity contactEntity, 
                                List<PhoneEntity> phoneEntities, 
                                List<EmailEntity> emailEntities){

        StringBuilder sb = new StringBuilder();
        for (PhoneEntity phone : phoneEntities) {
            sb.append(String.format("%s\n",
                    phone.getNumber()));
        }
        String phones = sb.toString();

        sb.setLength(0);
        for (EmailEntity email : emailEntities) {
            sb.append(String.format("%s\n",
                    email.getAddress()));
        }
        String emails = sb.toString();

        getMvpView().showContact(contactEntity.getDisplayName(), phones, emails);
    }
}

