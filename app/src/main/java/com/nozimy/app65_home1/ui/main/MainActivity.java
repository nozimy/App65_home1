package com.nozimy.app65_home1.ui.main;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;


import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.ui.contact.ContactFragment;
import com.nozimy.app65_home1.ui.contacts.ContactListFragment;
import com.nozimy.app65_home1.ui.map.MyMapFragment;
import com.nozimy.app65_home1.ui.route.RouteFragment;


public class MainActivity extends AppCompatActivity
        implements MyMapFragment.OnMapFragmentInteractionListener, RouteFragment.OnInteractionListener, ContactListFragment.OnInteractionListener {

    private FragmentManager fragmentManager;
    ContactListFragment contactListFragment;
    RouteFragment routeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            contactListFragment = new ContactListFragment();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.main_fragment, contactListFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void popBackStack() {
        fragmentManager.popBackStack();
    }

    @Override
    public void openContactChooseScreen(int pointType) {
        contactListFragment = ContactListFragment.newInstance(ContactListFragment.CHOICE_MODE, pointType);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_fragment, contactListFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void itemSelected(int return_key, String contactId) {
        if (routeFragment != null) {
            routeFragment.setPoint(return_key, contactId);
            Log.d("itemSelected", "POINT SET");
        }
        Log.d("itemSelected", "NOT routeFragment");
    }

    @Override
    public void openRouteFragment() {
        routeFragment = new RouteFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_fragment, routeFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void openAllContactsMapFragment() {
        MyMapFragment myMapFragment = MyMapFragment.newInstance("", true);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_fragment, myMapFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void openContactDetails(String contactId) {
        ContactFragment contactFragment = ContactFragment.newInstance(contactId);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_fragment, contactFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }
}
