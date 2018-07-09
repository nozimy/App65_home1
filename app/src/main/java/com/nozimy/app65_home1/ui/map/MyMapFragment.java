package com.nozimy.app65_home1.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.nozimy.app65_home1.R;
import com.nozimy.app65_home1.app.ContactListApp;
import com.nozimy.app65_home1.di.map.MapComponent;
import com.nozimy.app65_home1.ui.map.mvp.MapContract;

import javax.inject.Inject;

public class MyMapFragment extends Fragment implements MapContract.View {

    @Inject
    MapContract.Presenter presenter;

    private static final String ARG_CONTACT_ID = "contactId";
    private static final String ARG_SHOW_ALL_CONTACTS = "showAllContacts";

    private OnMapFragmentInteractionListener mListener;

    public MyMapFragment() {
    }

    public static MyMapFragment newInstance(String contactId, boolean showAllContacts) {
        MyMapFragment fragment = new MyMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT_ID, contactId);
        args.putBoolean(ARG_SHOW_ALL_CONTACTS, showAllContacts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        ContactListApp contactListApp = ((ContactListApp) getActivity().getApplication());
        MapComponent mapComponent = contactListApp.getAppComponent().plusMapComponent();
        mapComponent.inject(this);

        presenter.onAttach(this);

        super.onAttach(context);

        if (context instanceof OnMapFragmentInteractionListener) {
            mListener = (OnMapFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            presenter.setArguments(getArguments().getString(ARG_CONTACT_ID), getArguments().getBoolean(ARG_SHOW_ALL_CONTACTS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(presenter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMapFragmentInteractionListener {
        void popBackStack();
        void openContactDetails(String contactId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.select_geo_map_menu, menu);

        if(getArguments() != null && getArguments().getBoolean(ARG_SHOW_ALL_CONTACTS)){
            MenuItem saveItem = (MenuItem) menu.findItem(R.id.action_select_geo_save);
            saveItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_select_geo_save:
                presenter.saveContactAddress(mListener);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void openContactDetails(String contactId) {
        mListener.openContactDetails(contactId);
    }
}
