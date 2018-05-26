package com.nozimy.app65_home1.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.nozimy.app65_home1.db.entity.EmailEntity;

@Dao
public interface EmailDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(EmailEntity email);

    @Delete
    void delete(EmailEntity email);

    @Update
    void update(EmailEntity email);
}
