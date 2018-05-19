package com.nozimy.app65_home1.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.nozimy.app65_home1.data.entities.Contact;

import java.util.ArrayList;

public class DataManager {
    private Context context;
    private ArrayList<Contact> contacts;
    private static final String[] CONTACT_DETAILS_PROJECTION =
            {
                    ContactsContract.Data._ID,
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.Data.DATA1,
                    ContactsContract.Data.DATA2,
                    ContactsContract.Data.DATA3,
                    ContactsContract.Data.DATA4,
                    ContactsContract.Data.DATA5,
                    ContactsContract.Data.DATA6,
                    ContactsContract.Data.DATA7,
                    ContactsContract.Data.DATA8,
                    ContactsContract.Data.DATA9,
                    ContactsContract.Data.DATA10,
                    ContactsContract.Data.DATA11,
                    ContactsContract.Data.DATA12,
                    ContactsContract.Data.DATA13,
                    ContactsContract.Data.DATA14,
                    ContactsContract.Data.DATA15
            };
    private static final String CONTACT_DETAILS_SELECTION = ContactsContract.Data.LOOKUP_KEY + " = ?";

    public DataManager(Context context){
        this.context = context;
    }

    public ArrayList<Contact> getContacts(){
        if(contacts == null){
            loadContacts();
        }
        return contacts;
    }

    public Contact getContact(String lookUpKey){
        return loadContactDetails(lookUpKey);
    }

    private Cursor getContactsCursor() {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    }

    private Cursor getContactDetailsCursor(String lookUpKey) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] mSelectionArgs = { lookUpKey };
        return contentResolver.query(ContactsContract.Data.CONTENT_URI,
                CONTACT_DETAILS_PROJECTION,
                CONTACT_DETAILS_SELECTION,
                mSelectionArgs,
                null);
    }

    private boolean isCursorValid(Cursor cursor) {
        return cursor != null && !cursor.isClosed() && cursor.getCount() > 0 && cursor.moveToFirst();
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private ArrayList<Contact> loadContacts(){
        contacts = new ArrayList<Contact>();
        Cursor cursor = getContactsCursor();
        if(isCursorValid(cursor)) {
            try {
                while (cursor.moveToNext()) {
                    String contactName = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    String lookUpKey = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    contacts.add(new Contact(lookUpKey, contactName));
                }
            } finally {
                closeCursor(cursor);
            }
        }
        return contacts;
    }



    private Contact loadContactDetails(String lookUpKey){
        Contact contact = new Contact(lookUpKey, "");

        Cursor cursor = getContactDetailsCursor(lookUpKey);
        if(isCursorValid(cursor)) {
            try {
                while (cursor.moveToNext()) {
                    String mime = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                    switch (mime){
                        case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                            String fio = "";
                            String temp;

                            contact.familyName = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));

                            contact.givenName = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));

                            contact.middleName = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                            break;
                        case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                            contact.phoneNumber = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.Data.DATA1));

                            break;
                        case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                            contact.email = cursor.getString(
                                    cursor.getColumnIndex(ContactsContract.Data.DATA1));
                            break;
                    }
                }
            }finally {
                closeCursor(cursor);
            }
        }
        return contact;
    }
}
