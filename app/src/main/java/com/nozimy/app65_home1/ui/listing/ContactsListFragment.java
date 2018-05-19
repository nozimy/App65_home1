package com.nozimy.app65_home1.ui.listing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nozimy.app65_home1.data.DataManager;
import com.nozimy.app65_home1.data.entities.Contact;
import com.nozimy.app65_home1.ui.detail.DetailsActivity;
import com.nozimy.app65_home1.ui.detail.DetailsFragment;
import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.ui.listing.mvp.ContactsListMvpView;
import com.nozimy.app65_home1.ui.listing.mvp.ContactsListPresenter;

import java.util.List;

public class ContactsListFragment extends Fragment implements OnListFragmentInteractionListener, ContactsListMvpView{

    ContactsListPresenter contactsListPresenter;
    public static final String DETAILS_KEY = "com.nozimy.app65_home1.DETAILS_KEY";
    boolean mDualPane = false;
    int mCurCheckPosition;
    RecyclerView contactList;
    private DetailsFragment detailsFragment;

    public ContactsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        contactList = (RecyclerView) view.findViewById(R.id.list_unique);
        detailsFragment = (DetailsFragment)
                getFragmentManager().findFragmentById(R.id.fragment_container);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupPresenter();
        contactsListPresenter.requestReadContacts();

        if (checkSelfPermission()){
            contactsListPresenter.load();

            setDualPaneValue();
            if (savedInstanceState != null) {
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            }
            if (isDualPane()) {
                startShowingDetails(mCurCheckPosition);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contactList = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        contactsListPresenter.onPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupPresenter() {
        DataManager dataManager = new DataManager(getActivity());
        contactsListPresenter = new ContactsListPresenter(dataManager);
        contactsListPresenter.onAttach(this);
    }

    @Override
    public void onListFragmentInteraction(int position) {
        startShowingDetails(position);
    }

    void startShowingDetails(int index) {
        mCurCheckPosition = index;
        contactsListPresenter.showDetails(index);
    }

    @Override
    public boolean checkSelfPermission() {
        int hasReadContactPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
        return checkPermissionGranted(hasReadContactPermission);
    }

    @Override
    public boolean checkPermissionGranted(int permission) {
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }

    @Override
    public void setContacts(List<Contact> contacts) {
        contactList.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        contactList.setAdapter(new ContactsListAdapter(contacts, this));
    }

    @Override
    public void showContactDetailsFragment(String lookUpKey) {
        detailsFragment = DetailsFragment.newInstance(lookUpKey);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, detailsFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void openContactDetailsActivity(String lookUpKey) {
        Intent mIntent = new Intent(getActivity(), DetailsActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(DETAILS_KEY, lookUpKey);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    @Override
    public DetailsFragment getDetailsFragment() {
        return detailsFragment;
    }

    @Override
    public boolean isDualPane() {
        return mDualPane;
    }

    private void setDualPaneValue(){
        View detailsFrame = getActivity().findViewById(R.id.fragment_container);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
    }
}

