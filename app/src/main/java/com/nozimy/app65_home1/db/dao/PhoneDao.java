package com.nozimy.app65_home1.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nozimy.app65_home1.db.entity.PhoneEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface PhoneDao {
    @Query("SELECT * FROM phones WHERE contactId = :contactId")
    Maybe<List<PhoneEntity>> getPhones(String contactId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PhoneEntity phone);

    @Delete
    void delete(PhoneEntity phone);

    @Update
    void update(PhoneEntity phone);
}
