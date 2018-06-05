package com.nozimy.app65_home1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Settings {
    public static final String CONTACTS_IMPORTED = "com.nozimy.app65_home1.CONTACTS_IMPORTED";
    private final Context context;

    public Settings(Context context) {
        this.context = context;
    }

    private void set(Bundle bundle){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CONTACTS_IMPORTED, bundle.getBoolean(CONTACTS_IMPORTED));
        editor.apply();
    }

    private Bundle get(){
        Bundle bundle = new Bundle();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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
