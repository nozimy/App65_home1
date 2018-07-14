package com.nozimy.app65_home1.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nozimy.app65_home1.db.entity.EmailEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface EmailDao {

    @Query("SELECT * FROM emails WHERE contactId = :contactId")
    Maybe<List<EmailEntity>> getEmails(String contactId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(EmailEntity email);

    @Delete
    void delete(EmailEntity email);

    @Update
    void update(EmailEntity email);
}
