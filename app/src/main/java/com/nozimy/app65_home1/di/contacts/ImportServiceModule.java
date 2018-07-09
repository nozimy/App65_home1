package com.nozimy.app65_home1.di.contacts;

import android.content.ContentResolver;

import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ImportServiceModule {

    private ContentResolver contentResolver;

    public ImportServiceModule(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }

    @Provides
    @FragmentScope
    ImportService provideImportService(){
        return new ImportService(contentResolver);
    }

}
