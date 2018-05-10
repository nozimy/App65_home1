package com.nozimy.app65_home1;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity  implements ItemFragment.OnListFragmentInteractionListener{

    public static final String DETAILS_KEY = "com.nozimy.app65_home1.DETAILS_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    public void onListFragmentInteraction(String contactLookUpKey) {
        Intent mIntent = new Intent(this, DetailsActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(DETAILS_KEY, contactLookUpKey);

        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }
}
