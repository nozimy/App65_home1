package com.nozimy.app65_home1.ui.detail;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nozimy.app65_home1.data.DataManager;
import com.nozimy.app65_home1.data.entities.Contact;
import com.nozimy.app65_home1.ui.detail.mvp.ContactDetailsContract;
import com.nozimy.app65_home1.ui.detail.mvp.ContactDetailsPresenter;
import com.nozimy.app65_home1.ui.listing.ContactsListFragment;
import com.nozimy.app65_home1.R;

public class DetailsFragment extends Fragment implements ContactDetailsContract.View{

    ContactDetailsPresenter contactDetailsPresenter;

    private String mContactLoopUpKey;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(String mContactLoopUpKey) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ContactsListFragment.DETAILS_KEY, mContactLoopUpKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContactLoopUpKey = getArguments().getString(ContactsListFragment.DETAILS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        nameTextView = view.findViewById(R.id.details_text);
        emailTextView = view.findViewById(R.id.email);
        phoneTextView = view.findViewById(R.id.phone_number);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupPresenter();
        contactDetailsPresenter.requestReadContacts();

        if (checkSelfPermission()){
            contactDetailsPresenter.loadDetails(mContactLoopUpKey);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        contactDetailsPresenter.onPermissionsResult(requestCode, permissions, grantResults);
        if (checkSelfPermission()){
            contactDetailsPresenter.loadDetails(mContactLoopUpKey);
        }
    }

    public String getShownLookUpKey() {
        return getArguments().getString(ContactsListFragment.DETAILS_KEY);
    }


    private void setupPresenter() {
        DataManager dataManager = new DataManager(getActivity());
        contactDetailsPresenter = new ContactDetailsPresenter(dataManager);
        contactDetailsPresenter.onAttach(this);
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
    public void setDetails(Contact contact) {
        String fio = "";
        fio += getNotNullString(contact.familyName) + " " + getNotNullString(contact.givenName) + " " + getNotNullString(contact.middleName);
        nameTextView.setText(fio);
        phoneTextView.setText(contact.phoneNumber);
        emailTextView.setText(contact.email);
    }

    private String getNotNullString(String str){
        return str != null ? str : "";
    }
}