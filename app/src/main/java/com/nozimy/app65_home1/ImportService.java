package com.nozimy.app65_home1;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.nozimy.app65_home1.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ImportService {
    private ContentResolver contentResolver;

    private static final String CONTACT_PHONE_SELECTION = ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?";
    private static final String CONTACT_EMAIL_SELECTION = ContactsContract.CommonDataKinds.Email.LOOKUP_KEY + " = ?";

    public ImportService(Context context) {
        contentResolver = context.getContentResolver();
    }

    public ArrayList<ContactFromProvider> loadContacts(){
        ArrayList<ContactFromProvider> contactList = new ArrayList<>();

        Cursor cursor = getContactsCursor();
        if(isCursorValid(cursor)) {
            try {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String lookUpKey = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    String displayName = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                    ArrayList<String> phoneNumbers = loadPhones(lookUpKey);
                    ArrayList<String> emails = loadEmails(lookUpKey);

                    if (phoneNumbers.size() > 0) {
                        contactList.add(new ContactFromProvider(id, displayName, phoneNumbers, emails));
                    }
                }
            } finally {
                closeCursor(cursor);
            }
        }

        return contactList;
    }

    private Cursor getContactsCursor() {
        return contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
    }

    private Cursor getPhoneCursor(String lookUpKey) {
        String[] mSelectionArgs = { lookUpKey };
        return contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                CONTACT_PHONE_SELECTION,
                mSelectionArgs,
                null);
    }

    private Cursor getEmailCursor(String lookUpKey) {
        String[] mSelectionArgs = { lookUpKey };
        return contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                CONTACT_EMAIL_SELECTION,
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

    private ArrayList<String> loadPhones(String lookUpKey){
        ArrayList<String> phoneNumbers = new ArrayList<>();
        Cursor phoneCursor = getPhoneCursor(lookUpKey);

        if(isCursorValid(phoneCursor)) {
            try {
                while (phoneCursor.moveToNext()) {
                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (!phoneNumber.isEmpty() && !phoneNumbers.contains(phoneNumber)) {
                        phoneNumbers.add(phoneNumber);
                    }
                }
            } finally {
                closeCursor(phoneCursor);
            }
        }

        return phoneNumbers;
    }

    private ArrayList<String> loadEmails(String lookUpKey){
        ArrayList<String> emails = new ArrayList<>();
        Cursor emailsCursor = getEmailCursor(lookUpKey);

        if(isCursorValid(emailsCursor)) {
            try {
                while (emailsCursor.moveToNext()) {
                    String email = emailsCursor.getString(emailsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    if (!email.isEmpty() && !emails.contains(email)) {
                        emails.add(email);
                    }
                }
            } finally {
                closeCursor(emailsCursor);
            }
        }

        return emails;
    }

    public static class ContactFromProvider {
        List<String> phones;
        List<String> emails;
        private String id;
        private String displayName;

        public ContactFromProvider(String id, String displayName, List<String> phones, List<String> emails) {
            this.phones = phones;
            this.emails = emails;
            this.id = id;
            this.displayName = displayName;
        }

//        @Override
//        public long getId() {
//            return 0;
//        }
//
//        @Override
        public String getId() {
            return id;
        }

//        @Override
        public String getDisplayName() {
            return displayName;
        }

//        @Override
        public List<String> getPhones() {
            return phones;
        }

//        @Override
        public List<String> getEmails() {
            return emails;
        }

//        @Override
//        public String getFamilyName() {
//            return null;
//        }
//
//        @Override
//        public String getGivenName() {
//            return null;
//        }
//
//        @Override
//        public String getMiddleName() {
//            return null;
//        }
    }
}
