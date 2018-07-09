package com.nozimy.app65_home1.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.db.dao.ContactDao;
import com.nozimy.app65_home1.db.dao.EmailDao;
import com.nozimy.app65_home1.db.dao.PhoneDao;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.db.entity.EmailEntity;
import com.nozimy.app65_home1.db.entity.PhoneEntity;

import java.util.List;

@Database(entities = {ContactEntity.class, PhoneEntity.class, EmailEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

//    private static AppDatabase sInstance;
//    private static final String DATABASE_NAME = "contacts-db";
//    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public abstract ContactDao contactDao();
    public abstract PhoneDao phoneDao();
    public abstract EmailDao emailDao();

//    public static AppDatabase getInstance(final Context context){
//        if(sInstance == null){
//            synchronized (AppDatabase.class){
//                if(sInstance == null){
//                    sInstance = buildDatabase(context.getApplicationContext());
//                    sInstance.updateDatabaseCreated(context.getApplicationContext());
//                }
//            }
//        }
//        return sInstance;
//    }
//
//    private static AppDatabase buildDatabase(final Context appContext){
//        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME).build();
//    }

//    /**
//     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
//     */
//    private void updateDatabaseCreated(final Context context) {
//        if (context.getDatabasePath(DATABASE_NAME).exists()) {
//            setDatabaseCreated();
//        }
//    }

//    private void setDatabaseCreated(){
//        mIsDatabaseCreated.postValue(true);
//    }

//    public LiveData<Boolean> getDatabaseCreated() {
//        return mIsDatabaseCreated;
//    }

    public void importContactsFromProvider(ImportService importService){
        List<ImportService.ContactFromProvider> contactListFromProvider = importService.loadContacts();
        for (ImportService.ContactFromProvider contact: contactListFromProvider) {
            this.runInTransaction(() -> {
                this.contactDao().insert(new ContactEntity(
                        contact.getId(),
                        contact.getFamilyName(),
                        contact.getGivenName(),
                        contact.getMiddleName(),
                        contact.getDisplayName()
                ));
                for(String number: contact.getPhones()){
                    this.phoneDao().insert(new PhoneEntity(contact.getId(), number));
                }
                for(String email: contact.getEmails()){
                    this.emailDao().insert(new EmailEntity(contact.getId(), email));
                }
            });
        }
    }

}