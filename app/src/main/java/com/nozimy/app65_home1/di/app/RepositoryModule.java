package com.nozimy.app65_home1.di.app;

import com.nozimy.app65_home1.db.AppDatabase;
import com.nozimy.app65_home1.db.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    DataRepository provideContactRepository(AppDatabase appDatabase){
        return new DataRepository(appDatabase);
    }

}
