package com.nozimy.app65_home1;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.OnDetailsFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, detailsFragment).commit();
    }

    @Override
    public void onDetailsFragmentInteraction(Uri uri) {

    }
}
