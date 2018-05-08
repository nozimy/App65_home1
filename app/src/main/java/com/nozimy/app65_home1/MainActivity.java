package com.nozimy.app65_home1;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.nozimy.app65_home1.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

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
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

        // смотри dualPane https://developer.android.com/guide/components/fragments#Example

        Intent mIntent = new Intent(this, DetailsActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(DETAILS_KEY, item.details);

        mIntent.putExtras(mBundle);
        startActivity(mIntent);

    }
}
