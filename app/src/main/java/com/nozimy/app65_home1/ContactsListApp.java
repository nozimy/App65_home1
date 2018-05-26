package com.nozimy.app65_home1;

import android.app.Application;
import com.nozimy.app65_home1.db.AppDatabase;

public class ContactsListApp extends Application {

    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        appExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase(){
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase(), getAppExecutors());
    }

    public AppExecutors getAppExecutors() {
        return appExecutors;
    }

}
