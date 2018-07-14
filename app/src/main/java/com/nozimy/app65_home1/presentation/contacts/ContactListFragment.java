package com.nozimy.app65_home1.ui.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.nozimy.app65_home1.app.ContactListApp;
import com.nozimy.app65_home1.di.contacts.ContactListComponent;
import com.nozimy.app65_home1.di.contacts.ImportServiceModule;
import com.nozimy.app65_home1.ui.route.RouteFragment;
import com.nozimy.app65_home1.ui.contacts.mvp.ContactListContract;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.ui.common.CustomItemDecoration;
import com.nozimy.app65_home1.ui.contact.ContactFragment;
import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.ui.contacts.mvp.ContactListPresenter;
import com.nozimy.app65_home1.ui.map.MyMapFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactListFragment extends Fragment 
            implements ContactClickCallback, ContactListContract.View {


    @Inject
    ContactListContract.Presenter contactListPresenter;

    public static final String DETAILS_KEY = "com.nozimy.app65_home1.DETAILS_KEY";
    public static final String MODE_KEY = "com.nozimy.app65_home1.MODE_KEY";
    public static final String RETURN_KEY = "com.nozimy.app65_home1.RETURN_KEY";
    public static final int CHOICE_MODE = 1;
    boolean isDualPane = false;

    @BindView(R.id.list_unique) RecyclerView contactsRecyclerView;
    @BindView(R.id.contactListProgressBarWrap) LinearLayout contactListProgressBarWarp;
    private Unbinder unbinder;

    private ContactListAdapter contactListAdapter;
    String selectedContactId;
    private ContactFragment contactFragment;

    OnInteractionListener interactionListener;

    public ContactListFragment() {
    }

    public static ContactListFragment newInstance(int mode, int pointType) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putInt(MODE_KEY, mode);
        args.putInt(RETURN_KEY, pointType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        ContactListApp contactListApp = ((ContactListApp) getActivity().getApplication());
        ContactListComponent contactListComponent = contactListApp.getAppComponent()
                .plusContactListComponent(new ImportServiceModule(getActivity().getContentResolver()));
        contactListComponent.inject(this);
        super.onAttach(context);

        if (context instanceof OnInteractionListener) {
            interactionListener = (OnInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        contactListAdapter = new ContactListAdapter(this);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider);
        contactsRecyclerView.addItemDecoration(new CustomItemDecoration(dividerDrawable));
        contactsRecyclerView.setAdapter(contactListAdapter);

        contactFragment = (ContactFragment)
                getFragmentManager().findFragmentById(R.id.fragment_container);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contactListPresenter.onAttach(this);

        if(!checkSelfPermission()){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 
                                ContactListPresenter.REQUEST_CODE_READ_CONTACTS);
        }

        if (checkSelfPermission()){
            contactListPresenter.load();

            setDualPaneValue();
            if (savedInstanceState != null) {
                selectedContactId = savedInstanceState.getString("curChoice", "");
            }
            if (isDualPane()) {
                startShowingDetails(selectedContactId);
            }
        }
    }

    @Override
    public boolean checkSelfPermission() {
        int hasReadContactPermission = ContextCompat.checkSelfPermission(getActivity(), 
                                                    Manifest.permission.READ_CONTACTS);
        return checkPermissionGranted(hasReadContactPermission);
    }

    private void setDualPaneValue(){
        View detailsFrame = getActivity().findViewById(R.id.fragment_container);
        isDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
    }

    void startShowingDetails(String id) {
        selectedContactId = id;
        contactListPresenter.showDetails(id);
    }

    @Override
    public boolean checkPermissionGranted(int permission) {
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, 
                                            String[] permissions, 
                                            int[] grantResults){
        contactListPresenter.onPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }

    @Override
    public ContactFragment getContactFragment() {
        return contactFragment;
    }

    @Override
    public void showContactDetailsFragment(String contactId) {
        contactFragment = ContactFragment.newInstance(contactId);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, contactFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void openContactDetails(String contactId) {
        interactionListener.openContactDetails(contactId);
    }

    @Override
    public boolean isDualPane() {
        return isDualPane;
    }

    @Override
    public void onClick(String id) {
        if(getArguments() != null && getArguments().getInt(MODE_KEY) == CHOICE_MODE){
            interactionListener.itemSelected(getArguments().getInt(RETURN_KEY), id);
        } else {
            startShowingDetails(id);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("curChoice", selectedContactId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main_menu, menu);

        if(getArguments() != null && getArguments().getInt(MODE_KEY) == CHOICE_MODE){
            MenuItem mapItem = (MenuItem) menu.findItem(R.id.app_bar_map);
            mapItem.setVisible(false);
            MenuItem routeItem = (MenuItem) menu.findItem(R.id.app_bar_route);
            routeItem.setVisible(false);
        }

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(onQueryTextListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_search:
                return true;
            case R.id.app_bar_map:
                interactionListener.openAllContactsMapFragment();
                return true;
            case R.id.app_bar_route:
                interactionListener.openRouteFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            contactListPresenter.getContactsByDisplayName(query);
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            contactListPresenter.getContactsByDisplayName(newText);
            return false;
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        contactListPresenter.clearDisposable();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contactsRecyclerView = null;
        interactionListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLoadingIndicator(){
        contactListProgressBarWarp.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContacts(@NonNull List<ContactEntity> contacts) {
        contactListAdapter.setContactList(contacts);
    }

    @Override
    public void setGoneVisibility() {
        contactListProgressBarWarp.setVisibility(View.GONE);
    }

    public interface OnInteractionListener{
        void itemSelected(int return_key, String contactId);
        void openRouteFragment();
        void openAllContactsMapFragment();
        void openContactDetails(String contactId);
    }

}
