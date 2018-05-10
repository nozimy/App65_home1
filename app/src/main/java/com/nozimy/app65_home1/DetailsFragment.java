package com.nozimy.app65_home1;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
    private String mContactLoopUpKey;
    private OnDetailsFragmentInteractionListener mListener;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private static final String[] PROJECTION =
            {
                    Data._ID,
                    Data.MIMETYPE,
                    Data.DATA1,
                    Data.DATA2,
                    Data.DATA3,
                    Data.DATA4,
                    Data.DATA5,
                    Data.DATA6,
                    Data.DATA7,
                    Data.DATA8,
                    Data.DATA9,
                    Data.DATA10,
                    Data.DATA11,
                    Data.DATA12,
                    Data.DATA13,
                    Data.DATA14,
                    Data.DATA15
            };
    private static final String SELECTION = Data.LOOKUP_KEY + " = ?";
    private String[] mSelectionArgs = { "" };

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String mContactLoopUpKey) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.DETAILS_KEY, mContactLoopUpKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContactLoopUpKey = getArguments().getString(MainActivity.DETAILS_KEY);
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
        loadContactDetails();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsFragmentInteractionListener) {
            mListener = (OnDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void loadContactDetails(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        mSelectionArgs[0] = mContactLoopUpKey;
        Cursor mCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                SELECTION,
                mSelectionArgs,
                null);

        if(mCursor!=null){
            while (mCursor.moveToNext()) {
                String mime = mCursor.getString(mCursor.getColumnIndex(Data.MIMETYPE));
                switch (mime){
                    case StructuredName.CONTENT_ITEM_TYPE:
                        String fio = "";
                        String temp;

                        temp = mCursor.getString(
                                mCursor.getColumnIndex(StructuredName.FAMILY_NAME));
                        fio += temp != null ? " "+temp : "";

                        temp = mCursor.getString(
                                mCursor.getColumnIndex(StructuredName.GIVEN_NAME));
                        fio += temp != null ? " "+temp : "";

                        temp = mCursor.getString(
                                mCursor.getColumnIndex(StructuredName.MIDDLE_NAME));
                        fio += temp != null ? " "+temp : "";

                        nameTextView.setText(fio);
                        break;
                    case Phone.CONTENT_ITEM_TYPE:
                        String phoneNumber = mCursor.getString(
                                mCursor.getColumnIndex(Data.DATA1));
                        phoneTextView.setText(phoneNumber);
                        break;
                    case Email.CONTENT_ITEM_TYPE:
                        String email = mCursor.getString(
                                mCursor.getColumnIndex(Data.DATA1));
                        emailTextView.setText(email);
                        break;
                }
            }
            mCursor.close();
        }
    }

    public interface OnDetailsFragmentInteractionListener {
        void onDetailsFragmentInteraction(Uri uri);
    }
}
