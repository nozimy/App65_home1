package com.nozimy.app65_home1.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nozimy.app65_home1.db.entity.ContactEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts")
    LiveData<List<ContactEntity>> getAll();

    @Query("SELECT * FROM contacts")
    Flowable<List<ContactEntity>> getAllRx();

//    @Query("SELECT * FROM contacts WHERE UPPER(displayName) LIKE UPPER('%' || :searchText || '%')")
    @Query("SELECT * FROM contacts WHERE LOWER(display_name) LIKE LOWER(:searchText)")
    LiveData<List<ContactEntity>> getByDisplayName(String searchText);

    @Query("SELECT * FROM contacts WHERE LOWER(display_name) LIKE LOWER(:searchText)")
    Flowable<List<ContactEntity>> getByDisplayNameRx(String searchText);

    @Query("SELECT * FROM contacts WHERE id = :id")
    Flowable<ContactEntity> getById(String id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(ContactEntity contact);

//    @Query("SELECT * FROM contacts WHERE id IN (:ids)")
//    List<ContactEntity> loadAllByIds(int[] ids);
//
//    @Query("SELECT * FROM contacts WHERE family_name LIKE :first AND "
//            + "family_name LIKE :last LIMIT 1")
//    ContactEntity findByName(String first, String last);

//    @Query("SELECT * FROM contacts WHERE id = :id")
//    LiveData<ContactEntity> getById(int id);

//    @Insert
//    void insertAll(ContactEntity... contacts);

//    @Delete
//    void delete(ContactEntity contact);
//
//    @Update
//    void update(ContactEntity contact);
}
