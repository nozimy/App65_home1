package com.nozimy.app65_home1.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "phones",
        foreignKeys = {
            @ForeignKey(entity = ContactEntity.class,
                    parentColumns = "id",
                    childColumns = "contactId",
                    onDelete = ForeignKey.CASCADE),
        },
        indices = {@Index(value = "contactId")})
public class PhoneEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String contactId;
    private String number;

    public PhoneEntity(String contactId, String number) {
        this.contactId = contactId;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getContactId() {
        return contactId;
    }

    public String getNumber() {
        return number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
