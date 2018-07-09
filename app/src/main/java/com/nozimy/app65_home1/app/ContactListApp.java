package com.nozimy.app65_home1.app;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nozimy.app65_home1.di.app.AppComponent;
import com.nozimy.app65_home1.di.app.AppModule;
import com.nozimy.app65_home1.di.app.DaggerAppComponent;
import com.nozimy.app65_home1.di.app.DatabaseModule;

public class ContactListApp extends Application {

//    private AppExecutors appExecutors;

    @Nullable
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencies();
//        appExecutors = new AppExecutors();
    }

//    public AppDatabase getDatabase(){
//        return AppDatabase.getInstance(this);
//    }

//    public DataRepository getRepository() {
//        return DataRepository.getInstance(getDatabase(), getAppExecutors());
//    }

//    public AppExecutors getAppExecutors() {
//        return appExecutors;
//    }

    private void initDependencies(){
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this.getBaseContext()))
                .databaseModule(new DatabaseModule())
                .build();
        appComponent.inject(this);
    }

    @NonNull
    public AppComponent getAppComponent() {
        if(appComponent == null){
            initDependencies();
        }
        return appComponent;
    }

}
