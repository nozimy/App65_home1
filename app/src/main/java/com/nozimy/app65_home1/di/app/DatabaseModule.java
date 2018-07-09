package com.nozimy.app65_home1.di.app;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nozimy.app65_home1.BuildConfig;
import com.nozimy.app65_home1.db.AppDatabase;
import com.nozimy.app65_home1.utils.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public SharedPreferences provideShared(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    public AppDatabase provideDatabase(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, BuildConfig.DATA_BASE_NAME)
                .build();
    }

    @Provides
    @Singleton
    public Settings provideSettings(SharedPreferences sharedPreferences){
        return new Settings(sharedPreferences);
    }
}
