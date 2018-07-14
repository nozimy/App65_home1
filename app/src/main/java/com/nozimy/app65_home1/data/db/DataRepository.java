package com.nozimy.app65_home1.db;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.db.entity.EmailEntity;
import com.nozimy.app65_home1.db.entity.PhoneEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class DataRepository{

//    private static DataRepository sInstance;

    @NonNull
    private AppDatabase mDatabase;

//    private MediatorLiveData<List<ContactEntity>> mObservableContacts;

//    private final AppExecutors appExecutors;

    @Inject
    public DataRepository(@NonNull AppDatabase database) {
        this.mDatabase = database;

//        this.appExecutors = appExecutors;
//        mObservableContacts = new MediatorLiveData<>();
//        mObservableContacts.addSource(mDatabase.contactDao().getAll(),
//                contactEntities -> {
//                    if (mDatabase.getDatabaseCreated().getValue() != null) {
//                        mObservableContacts.postValue(contactEntities);
//                    }
//                });
    }

//    public static DataRepository getInstance(final AppDatabase database, final AppExecutors appExecutors) {
//        if (sInstance == null) {
//            synchronized (DataRepository.class) {
//                if (sInstance == null) {
//                    sInstance = new DataRepository(database, appExecutors);
//                }
//            }
//        }
//        return sInstance;
//    }

    public LiveData<List<ContactEntity>> getContacts(){
        return mDatabase.contactDao().getAll();
    }

    public LiveData<List<ContactEntity>> getByDisplayName(String searchText){
        return mDatabase.contactDao().getByDisplayName(searchText);
    }

    public Single<List<ContactEntity>> getByDisplayNameRx(String searchText){
        return mDatabase.contactDao().getByDisplayNameRx(searchText);
    }

//    public void importFromProvider(ImportService service){
//        appExecutors.diskIO().execute(() -> {
//            mDatabase.importContactsFromProvider(service);
//        });
//    }

    public Completable importFromProvider(ImportService service){
        return Completable.fromAction(() -> mDatabase.importContactsFromProvider(service));
    }

    public Single<List<ContactEntity>> getContactsRx(){
        return mDatabase.contactDao().getAllRx();
    }

    public Maybe<ContactEntity> getContact(String contactId){
        return mDatabase.contactDao().getById(contactId);
    }

    public Maybe<List<PhoneEntity>> getPhones(String contactId){
        return mDatabase.phoneDao().getPhones(contactId);
    }

    public Maybe<List<EmailEntity>> getEmails(String contactId){
        return mDatabase.emailDao().getEmails(contactId);
    }

    public void updateContactAddress(String id, double lat, double lng, String address){
        mDatabase.contactDao().updateAddress(id, lat, lng, address);
    }

}
