package com.nozimy.app65_home1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.nozimy.app65_home1.db.AppDatabase;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.db.entity.EmailEntity;
import com.nozimy.app65_home1.db.entity.PhoneEntity;
import com.nozimy.app65_home1.utils.AppExecutors;

import java.util.List;

import io.reactivex.Flowable;

public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<ContactEntity>> mObservableContacts;

    private final AppExecutors appExecutors;

    private DataRepository(final AppDatabase database, final AppExecutors appExecutors) {
        this.mDatabase = database;
        this.appExecutors = appExecutors;
        mObservableContacts = new MediatorLiveData<>();
        mObservableContacts.addSource(mDatabase.contactDao().getAll(),
                contactEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableContacts.postValue(contactEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database, final AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database, appExecutors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<ContactEntity>> getContacts(){
        return mObservableContacts;
    }

    public LiveData<List<ContactEntity>> getByDisplayName(String searchText){
        return mDatabase.contactDao().getByDisplayName(searchText);
    }

    public Flowable<List<ContactEntity>> getByDisplayNameRx(String searchText){
        return mDatabase.contactDao().getByDisplayNameRx(searchText);
    }

    public void importFromProvider(ImportService service){
        appExecutors.diskIO().execute(() -> {
            mDatabase.importContactsFromProvider(service);
        });
    }

    public Flowable<List<ContactEntity>> getContactsRx(){
        return mDatabase.contactDao().getAllRx();
    }

    public Flowable<ContactEntity> getContact(String contactId){
        return mDatabase.contactDao().getById(contactId);
    }

    public Flowable<List<PhoneEntity>> getPhones(String contactId){
        return mDatabase.phoneDao().getPhones(contactId);
    }

    public Flowable<List<EmailEntity>> getEmails(String contactId){
        return mDatabase.emailDao().getEmails(contactId);
    }

}
