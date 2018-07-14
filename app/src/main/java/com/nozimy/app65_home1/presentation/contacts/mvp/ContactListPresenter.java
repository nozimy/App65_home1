package com.nozimy.app65_home1.ui.contacts.mvp;

import android.util.Log;

import com.nozimy.app65_home1.domain.interactor.contacts.ContactListInteractor;
import com.nozimy.app65_home1.utils.Settings;
import com.nozimy.app65_home1.ui.common.mvp.BasePresenter;
import com.nozimy.app65_home1.utils.CommonUtils;


import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactListPresenter<V extends ContactListContract.View> extends BasePresenter<V> 
        implements ContactListContract.Presenter<V> {

    public static final int REQUEST_CODE_READ_CONTACTS = 1;

    private ContactListInteractor contactListInteractor;
    private Settings settings;
    private final CompositeDisposable mDisposable;

    public ContactListPresenter(ContactListInteractor contactListInteractor, 
                                    Settings settings, 
                                    CompositeDisposable compositeDisposable) {
        this.contactListInteractor = contactListInteractor;
        this.settings = settings;
        this.mDisposable = compositeDisposable;
    }

    public void load(){
        Log.d("List PRESENTER", "load");

        if(!settings.getContactsImported()){

            getMvpView().showLoadingIndicator();

            Disposable disposable = contactListInteractor.importFromProvider()
                    .andThen(Completable.fromAction(() -> settings.setContactsImported(true)))
                    .subscribeOn(Schedulers.io())
                    .andThen(contactListInteractor.getContacts())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(__-> getMvpView().showLoadingIndicator())
                    .subscribe(contactEntities -> {
                        getMvpView().showContacts(contactEntities);
                        getMvpView().setGoneVisibility();
                    }, throwable  -> {
                        throwable.printStackTrace();
                        getMvpView().setGoneVisibility();
                    });

            mDisposable.add(disposable);

        } else {
            getContacts();
        }
    }

    @Override
    public void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean readContactsGranted = false;

        switch (requestCode){
            case REQUEST_CODE_READ_CONTACTS:
                if(grantResults.length > 0 && getMvpView().checkPermissionGranted(grantResults[0])){
                    readContactsGranted = true;
                }
        }

        if(readContactsGranted){
            load();
        }
        else{
            CommonUtils.showToast(getMvpView().getViewActivity(), "Требуется установить разрешения");
        }
    }

    @Override
    public void showDetails(String id) {

        if (getMvpView().isDualPane()) {
            if (
                getMvpView().getContactFragment() == null 
                || !id.equals(getMvpView().getContactFragment().getIdFromArguments())) {

                getMvpView().showContactDetailsFragment(id);
            }
        } else {
            getMvpView().openContactDetails(id);
        }
    }

    @Override
    public void clearDisposable() {
        mDisposable.clear();
    }

    private void getContacts(){
        Log.d("List PRESENTER", "getContacts()");
        Disposable disposable = contactListInteractor.getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__-> getMvpView().showLoadingIndicator())
                .subscribe(contactEntities -> {
                    Log.d("List PRESENTER", "getContacts() SUBSCRIBE");
                    getMvpView().showContacts(contactEntities);
                    getMvpView().setGoneVisibility();
                }, throwable  -> {
                    throwable.printStackTrace();
                    getMvpView().setGoneVisibility();
                });
        mDisposable.add(disposable);
    }

    @Override
    public void getContactsByDisplayName(String searchText) {
        searchText = "%"+searchText+"%";

        Disposable disposable = contactListInteractor.getByDisplayName(searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactEntities -> {
                    if (contactEntities != null) {
                        getMvpView().showContacts(contactEntities);
                    }
                });

        mDisposable.add(disposable);
    }

}
