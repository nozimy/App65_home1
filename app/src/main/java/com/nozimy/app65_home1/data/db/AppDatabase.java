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

    public abstract ContactDao contactDao();
    public abstract PhoneDao phoneDao();
    public abstract EmailDao emailDao();

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