package com.nozimy.app65_home1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.nozimy.app65_home1.db.AppDatabase;
import com.nozimy.app65_home1.db.dao.ContactDao;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.model.Contact;

import java.util.List;

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

//    public void insertContact(ContactEntity contactEntity){
//        new insertAsyncTask(mDatabase.contactDao()).execute(contactEntity);
//    }
//
//    private static class insertAsyncTask extends AsyncTask<ContactEntity, Void, Void> {
//        private ContactDao mAsyncTaskDao;
//
//        insertAsyncTask(ContactDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final ContactEntity... params) {
//            mAsyncTaskDao.insert(params[0]);
//            return null;
//        }
//    }

    public void importFromProvider(ImportService service){
        appExecutors.diskIO().execute(() -> {
            mDatabase.importContactsFromProvider(service);
        });
    }

}
