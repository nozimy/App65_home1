package com.nozimy.app65_home1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class ItemFragment extends Fragment {

    private static final int REQUEST_CODE_READ_CONTACTS=1;
    private static boolean READ_CONTACTS_GRANTED = false;
    ListView contactList;
    ArrayList<ContactItemInList> contacts = new ArrayList<ContactItemInList>();
    private OnListFragmentInteractionListener mListener;

    public ItemFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        contactList = (ListView) view.findViewById(R.id.list_unique);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onListFragmentInteraction(contacts.get(position).lookUpKey);
            }
        });
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

    private void loadContacts(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor mCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(mCursor!=null){
            while (mCursor.moveToNext()) {
                String contactName = mCursor.getString(
                        mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                String lookUpKey = mCursor.getString(
                        mCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                contacts.add(new ContactItemInList(lookUpKey, contactName));
            }
            mCursor.close();
        }
        ArrayAdapter<ContactItemInList> adapter = new ArrayAdapter<ContactItemInList>(getActivity(),
                R.layout.fragment_item, contacts);
        contactList.setAdapter(adapter);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String contactLookUpKey);
    }

}
