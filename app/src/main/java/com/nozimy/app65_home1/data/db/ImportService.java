package com.nozimy.app65_home1;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportService {
    private ContentResolver contentResolver;

    private static final String CONTACT_PHONE_SELECTION = 
                                Phone.LOOKUP_KEY + " = ?";
    private static final String CONTACT_EMAIL_SELECTION = 
                                Email.LOOKUP_KEY + " = ?";

    public ImportService(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public ArrayList<ContactFromProvider> loadContacts(){
        ArrayList<ContactFromProvider> contactList = new ArrayList<>();

        Cursor cursor = getContactsCursor();
        if(isCursorValid(cursor)) {
            try {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(Contacts._ID));
                    String lookUpKey = cursor.getString(
                            cursor.getColumnIndex(Contacts.LOOKUP_KEY));
                    String displayName = cursor.getString(
                            cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));

                    Map<String,String> names = getNames(id);

                    ArrayList<String> phoneNumbers = loadPhones(lookUpKey);
                    ArrayList<String> emails = loadEmails(lookUpKey);

                    if (phoneNumbers.size() > 0) {
                        contactList
                            .add(new ContactFromProvider(id, displayName, phoneNumbers, emails, names.get("family"), 
                                    names.get("given"), 
                                    names.get("middle")));
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
        return contentResolver.query(Phone.CONTENT_URI,
                null,
                CONTACT_PHONE_SELECTION,
                mSelectionArgs,
                null);
    }

    private Cursor getEmailCursor(String lookUpKey) {
        String[] mSelectionArgs = { lookUpKey };
        return contentResolver.query(Email.CONTENT_URI,
                null,
                CONTACT_EMAIL_SELECTION,
                mSelectionArgs,
                null);
    }

    private boolean isCursorValid(Cursor cursor) {
        return cursor != null 
                && !cursor.isClosed() 
                && cursor.getCount() > 0 
                && cursor.moveToFirst();
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
                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
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
                    String email = emailsCursor.getString(emailsCursor.getColumnIndex(Email.ADDRESS));
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

    private Map<String, String> getNames(String contactId){
        Map<String, String> names = new HashMap<String, String>();

        String whereName = Data.MIMETYPE + " = ? AND " + StructuredName.CONTACT_ID + " = ?";
        String[] whereNameParams = new String[] { StructuredName.CONTENT_ITEM_TYPE, contactId };
        Cursor nameCur = contentResolver.query(Data.CONTENT_URI, null, whereName, whereNameParams, StructuredName.GIVEN_NAME);
        if(isCursorValid(nameCur)){
            while (nameCur.moveToNext()) {
                names.put("given", nameCur.getString(nameCur.getColumnIndex(StructuredName.GIVEN_NAME)));
                names.put("family", nameCur.getString(nameCur.getColumnIndex(StructuredName.GIVEN_NAME)));
                names.put("middle", nameCur.getString(nameCur.getColumnIndex(StructuredName.MIDDLE_NAME)));
            }
            closeCursor(nameCur);
        }

        return names;
    }

    public static class ContactFromProvider {
        List<String> phones;
        List<String> emails;
        private String id;
        private String displayName;
        private String familyName;
        private String givenName;
        private String middleName;

        public ContactFromProvider(String id, 
                                    String displayName, 
                                    List<String> phones, 
                                    List<String> emails, 
                                    String familyName, 
                                    String givenName, 
                                    String middleName) {
            this.phones = phones;
            this.emails = emails;
            this.id = id;
            this.displayName = displayName;
            this.familyName = familyName;
            this.givenName = givenName;
            this.middleName = middleName;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public List<String> getPhones() {
            return phones;
        }

        public List<String> getEmails() {
            return emails;
        }

        public String getFamilyName() {
            return familyName;
        }

        public String getGivenName() {
            return givenName;
        }

        public String getMiddleName() {
            return middleName;
        }
    }
}
