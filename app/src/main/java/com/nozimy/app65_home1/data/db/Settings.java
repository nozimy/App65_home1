package com.nozimy.app65_home1.utils;

import android.content.SharedPreferences;
import android.os.Bundle;

import javax.inject.Inject;

public class Settings {
    public static final String CONTACTS_IMPORTED = "com.nozimy.app65_home1.CONTACTS_IMPORTED";
    private SharedPreferences sharedPreferences;

    @Inject
    public Settings(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private void set(Bundle bundle){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CONTACTS_IMPORTED, bundle.getBoolean(CONTACTS_IMPORTED));
        editor.apply();
    }

    private Bundle get(){
        Bundle bundle = new Bundle();
        bundle.putBoolean(CONTACTS_IMPORTED, sharedPreferences.getBoolean(CONTACTS_IMPORTED, false));
        return bundle;
    }

    public boolean getContactsImported(){
        return get().getBoolean(CONTACTS_IMPORTED);
    }

    public void setContactsImported(boolean imported){
        Bundle settings = get();
        settings.putBoolean(CONTACTS_IMPORTED, imported);
        set(settings);
    }
}
