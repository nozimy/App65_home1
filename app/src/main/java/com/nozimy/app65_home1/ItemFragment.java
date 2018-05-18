package com.nozimy.app65_home1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;

import static com.nozimy.app65_home1.MainActivity.DETAILS_KEY;

public class ItemFragment extends Fragment implements OnListFragmentInteractionListener{

    boolean mDualPane;
    String mCurLookUpKey;
    int mCurCheckPosition;

    private static final int REQUEST_CODE_READ_CONTACTS=1;
    private boolean READ_CONTACTS_GRANTED = false;
    RecyclerView contactList;
    ArrayList<ContactItemInList> contacts = new ArrayList<ContactItemInList>();
    private OnListFragmentInteractionListener mListener;

    public ItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        Context context = view.getContext();
        contactList = (RecyclerView) view.findViewById(R.id.list_unique);
        contactList.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int hasReadContactPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);

        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_CONTACTS_GRANTED = true;
        } else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }

        if (READ_CONTACTS_GRANTED){
            loadContacts();

            View detailsFrame = getActivity().findViewById(R.id.fragment_container);
            mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
            if (savedInstanceState != null) {
                // Restore last state for checked position.
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            }
            if (mDualPane) {
                // Make sure our UI is in the correct state.
                showDetails(mCurCheckPosition);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contacts = null;
        mListener = null;
        contactList = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch (requestCode){
            case REQUEST_CODE_READ_CONTACTS:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    READ_CONTACTS_GRANTED = true;
                }
        }
        if(READ_CONTACTS_GRANTED){
            loadContacts();
        }
        else{
            Toast.makeText(getActivity(), "Требуется установить разрешения", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    private void loadContacts(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor mCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(mCursor!=null){
            try{
                while (mCursor.moveToNext()) {
                    String contactName = mCursor.getString(
                            mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    String lookUpKey = mCursor.getString(
                            mCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    contacts.add(new ContactItemInList(lookUpKey, contactName));
                }
            } finally {
                mCursor.close();
            }
        }
        contactList.setAdapter(new RecyclerViewAdapter(contacts, this));
    }

    void showDetails(int index) {
        mCurCheckPosition = index;
        mCurLookUpKey = contacts.get(index).lookUpKey;

        DetailsFragment details = (DetailsFragment)
                getFragmentManager().findFragmentById(R.id.fragment_container);
        if (mDualPane) {
            if (details == null || !mCurLookUpKey.equals(details.getShownLookUpKey())) {
                // Make new fragment to show this selection.
                details = DetailsFragment.newInstance(mCurLookUpKey);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } else {
            Intent mIntent = new Intent(getActivity(), DetailsActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString(DETAILS_KEY, mCurLookUpKey);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        }

    }

    @Override
    public void onListFragmentInteraction(int position) {
        showDetails(position);
    }
}

