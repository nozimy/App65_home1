package com.nozimy.app65_home1.data.entities;

public class Contact {
    public String lookUpKey;
    public String name;
    public String familyName;
    public String givenName;
    public String middleName;
    public String phoneNumber;
    public String email;



    public Contact(String lookUpKey, String name){
        this.lookUpKey = lookUpKey;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

