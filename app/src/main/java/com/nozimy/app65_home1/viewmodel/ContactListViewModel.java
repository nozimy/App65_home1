package com.nozimy.app65_home1.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.nozimy.app65_home1.ContactsListApp;
import com.nozimy.app65_home1.DataRepository;
import com.nozimy.app65_home1.db.entity.ContactEntity;

import java.util.List;

import io.reactivex.Flowable;

public class ContactListViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<ContactEntity>> mObservableContacts;

    private final DataRepository dataRepository;

    private final Flowable<List<ContactEntity>> contactList;

    public ContactListViewModel(Application application) {
        super(application);

        dataRepository = ((ContactsListApp) application).getRepository();

        mObservableContacts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableContacts.setValue(null);

        LiveData<List<ContactEntity>> contacts = ((ContactsListApp) application).getRepository()
                .getContacts();

        // observe the changes of the contacts from the database and forward them
        mObservableContacts.addSource(contacts, mObservableContacts::setValue);

        contactList = ((ContactsListApp) application).getRepository()
                .getContactsRx();
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<ContactEntity>> getContacts() {
        return mObservableContacts;
    }

    public LiveData<List<ContactEntity>> getByDisplayName(String searchText) {
        return dataRepository.getByDisplayName(searchText);
    }

    public Flowable<List<ContactEntity>> getByDisplayNameRx(String searchText) {
        return dataRepository.getByDisplayNameRx(searchText);
    }

    public Flowable<List<ContactEntity>> getContactsRx(){return contactList;}

}
