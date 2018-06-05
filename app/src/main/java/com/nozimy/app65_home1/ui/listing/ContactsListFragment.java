package com.nozimy.app65_home1.ui.listing;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nozimy.app65_home1.ContactsListApp;
import com.nozimy.app65_home1.DataRepository;
import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.ui.detail.MapsActivity;
import com.nozimy.app65_home1.ui.listing.mvp.ContactListContract;
import com.nozimy.app65_home1.utils.CommonUtils;
import com.nozimy.app65_home1.utils.Settings;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.ui.common.CustomItemDecoration;
import com.nozimy.app65_home1.ui.detail.DetailsActivity;
import com.nozimy.app65_home1.ui.detail.DetailsFragment;
import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.ui.listing.mvp.ContactsListPresenter;
import com.nozimy.app65_home1.viewmodel.ContactListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ContactsListFragment extends Fragment implements ContactClickCallback, ContactListContract.View, OnMapReadyCallback {

    ContactsListPresenter contactsListPresenter;
    private ContactListViewModel contactListViewModel;

    public static final String DETAILS_KEY = "com.nozimy.app65_home1.DETAILS_KEY";
    boolean isDualPane = false;

    @BindView(R.id.list_unique) RecyclerView contactsRecyclerView;
    @BindView(R.id.contactListProgressBarWrap) LinearLayout contactListProgressBarWarp;
    private Unbinder unbinder;

    private ContactsListAdapter contactsListAdapter = new ContactsListAdapter(this);
    String selectedContactId;
    private DetailsFragment detailsFragment;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private GoogleMap mMap;

    public ContactsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        contactsRecyclerView.setLayoutManager(layoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider);
        contactsRecyclerView.addItemDecoration(new CustomItemDecoration(dividerDrawable));
        contactsRecyclerView.setAdapter(contactsListAdapter);

        detailsFragment = (DetailsFragment)
                getFragmentManager().findFragmentById(R.id.fragment_container);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);

        setupPresenter();

        if(!checkSelfPermission()){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, ContactsListPresenter.REQUEST_CODE_READ_CONTACTS);
        }

        if (checkSelfPermission()){
            contactsListPresenter.load();

            setDualPaneValue();
            if (savedInstanceState != null) {
                selectedContactId = savedInstanceState.getString("curChoice", "");
            }
            if (isDualPane()) {
                startShowingDetails(selectedContactId);
            }
        }
    }

    private void setupPresenter() {
        DataRepository repository = ((ContactsListApp) getActivity().getApplication()).getRepository();
        contactsListPresenter = new ContactsListPresenter(repository, new ImportService(getActivity()), new Settings(getActivity()));
        contactsListPresenter.onAttach(this);
    }

    @Override
    public boolean checkSelfPermission() {
        int hasReadContactPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
        return checkPermissionGranted(hasReadContactPermission);
    }

    private void setDualPaneValue(){
        View detailsFrame = getActivity().findViewById(R.id.fragment_container);
        isDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
    }

    void startShowingDetails(String id) {
        selectedContactId = id;
        contactsListPresenter.showDetails(id);
    }

    @Override
    public boolean checkPermissionGranted(int permission) {
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        contactsListPresenter.onPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void subscribeUi(){
        mDisposable.add(CommonUtils.zipWithTimer(contactListViewModel.getContactsRx())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__->showLoadingIndicator())
                .subscribe(contactEntities -> {
                    if (contactEntities != null) {
                        contactsListAdapter.setContactList(contactEntities);
                        contactListProgressBarWarp.setVisibility(View.GONE);
                    }
                }, throwable  -> {
                    contactListProgressBarWarp.setVisibility(View.GONE);
                }));

//        contactListViewModel.getContactsRx()
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(__->showLoadingIndicator())
//                .subscribe(contactEntities -> {
//                    if (contactEntities != null) {
//                        contactsListAdapter.setContactList(contactEntities);
//                        contactListProgressBarWarp.setVisibility(View.GONE);
//                    }
//                }, throwable  -> {
//                    contactListProgressBarWarp.setVisibility(View.GONE);
//                });
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }

    @Override
    public DetailsFragment getDetailsFragment() {
        return detailsFragment;
    }

    @Override
    public void showContactDetailsFragment(String contactId) {
        detailsFragment = DetailsFragment.newInstance(contactId);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, detailsFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void openContactDetailsActivity(String contactId) {
        Intent mIntent = new Intent(getActivity(), DetailsActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString(DETAILS_KEY, contactId);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    @Override
    public boolean isDualPane() {
        return isDualPane;
    }

    @Override
    public void onClick(String id) {
        startShowingDetails(id);
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
//                Intent mIntent = new Intent(getActivity(), MapsActivity.class);
//                startActivity(mIntent);

                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                mapFragment.getMapAsync(this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.map_fragment, mapFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            getContactsByDisplayName(query);
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            getContactsByDisplayName(newText);
            return false;
        }
    };

    private void getContactsByDisplayName(String search){
        search = "%"+search+"%";

        mDisposable.add(contactListViewModel.getByDisplayNameRx(search)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ContactEntity>>() {
                    @Override
                    public void accept(List<ContactEntity> contactEntities) throws Exception {
                        if (contactEntities != null) {
                            contactsListAdapter.setContactList(contactEntities);
                        }
                    }
                }));
    }

    @Override
    public void onStop() {
        super.onStop();

        mDisposable.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contactsRecyclerView = null;
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}

