package com.nozimy.app65_home1.ui.listing;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nozimy.app65_home1.ContactsListApp;
import com.nozimy.app65_home1.DataRepository;
import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.model.Contact;
import com.nozimy.app65_home1.ui.common.CustomItemDecoration;
import com.nozimy.app65_home1.ui.detail.DetailsActivity;
import com.nozimy.app65_home1.ui.detail.DetailsFragment;
import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.ui.listing.mvp.ContactsListMvpView;
import com.nozimy.app65_home1.ui.listing.mvp.ContactsListPresenter;
import com.nozimy.app65_home1.utils.CommonUtils;
import com.nozimy.app65_home1.viewmodel.ContactListViewModel;

import java.util.List;

public class ContactsListFragment extends Fragment implements OnListFragmentInteractionListener, ContactsListMvpView{

    ContactsListPresenter contactsListPresenter;
    public static final String DETAILS_KEY = "com.nozimy.app65_home1.DETAILS_KEY";
    boolean isDualPane = false;
    int curCheckPosition;
    RecyclerView contactsRecyclerView;
    private DetailsFragment detailsFragment;
    private ContactsListAdapter contactsListAdapter = new ContactsListAdapter(this);

    private ContactListViewModel contactListViewModel;

    public ContactsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        contactsRecyclerView = (RecyclerView) view.findViewById(R.id.list_unique);
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
        contactListViewModel.getContacts().observe(this, new Observer<List<ContactEntity>>() {
            @Override
            public void onChanged(@Nullable List<ContactEntity> contactEntities) {
                if (contactEntities != null) {
                    contactsListAdapter.setContactList(contactEntities);
                }
            }
        });

        setupPresenter();
        contactsListPresenter.requestReadContacts();

        if (checkSelfPermission()){
            contactsListPresenter.load();

            setDualPaneValue();
            if (savedInstanceState != null) {
                curCheckPosition = savedInstanceState.getInt("curChoice", 0);
            }
            if (isDualPane()) {
                startShowingDetails(curCheckPosition);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", curCheckPosition);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contactsRecyclerView = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        contactsListPresenter.onPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupPresenter() {
        DataRepository repository = ((ContactsListApp) getActivity().getApplication()).getRepository();
        contactsListPresenter = new ContactsListPresenter(repository, new ImportService(getActivity()));
        contactsListPresenter.onAttach(this);
    }

    @Override
    public void onListFragmentInteraction(int position) {
        startShowingDetails(position);
    }

    void startShowingDetails(int index) {
        curCheckPosition = index;
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
        return isDualPane;
    }

    private void setDualPaneValue(){
        View detailsFrame = getActivity().findViewById(R.id.fragment_container);
        isDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
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

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            getContactsByDisplayName(query);
//            CommonUtils.showToast(getActivity(), query);
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

        contactListViewModel.getByDisplayName(search).observe(this, new Observer<List<ContactEntity>>() {
            @Override
            public void onChanged(@Nullable List<ContactEntity> contactEntities) {
                if (contactEntities != null) {
                    contactsListAdapter.setContactList(contactEntities);
                }
            }
        });
    }
}

