package com.nozimy.app65_home1.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.nozimy.app65_home1.db.model.Contact;

@Entity(tableName = "contacts", indices = {@Index("display_name")})
public class ContactEntity implements Contact{
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "family_name")
    private String familyName;

    @ColumnInfo(name = "given_name")
    private String givenName;

    @ColumnInfo(name = "middle_name")
    private String middleName;

    @ColumnInfo(name = "display_name")
    private String displayName;

    private double lat;
    private double lng;

    private String address;

    public ContactEntity(@NonNull String id, String familyName, String givenName, String middleName, String displayName) {
        this.id = id;
        this.familyName = familyName;
        this.givenName = givenName;
        this.middleName = middleName;
        this.displayName = displayName;
    }

    public ContactEntity(Contact contact) {
        this.id = contact.getId();
        this.displayName = contact.getDisplayName();
        this.familyName = contact.getFamilyName();
        this.givenName = contact.getGivenName();
        this.middleName = contact.getMiddleName();
    }

    @Ignore
    public ContactEntity(@NonNull String id, String displayName){
        this.id = id;
        this.displayName = displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @NonNull
    public String getId() {
        return id;
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


    public void setId(String id) {
        this.id = id;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
