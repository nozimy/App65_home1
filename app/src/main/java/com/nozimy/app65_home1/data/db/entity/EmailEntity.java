package com.nozimy.app65_home1.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "emails",
        foreignKeys = {
                @ForeignKey(entity = ContactEntity.class,
                        parentColumns = "id",
                        childColumns = "contactId",
                        onDelete = ForeignKey.CASCADE),
        },
        indices = {@Index(value = "contactId")})
public class EmailEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String contactId;
    private String address;

    public EmailEntity(String contactId, String address) {
        this.contactId = contactId;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getContactId() {
        return contactId;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
