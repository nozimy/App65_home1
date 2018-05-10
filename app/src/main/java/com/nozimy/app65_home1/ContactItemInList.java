package com.nozimy.app65_home1;

public class ContactItemInList {
    public String lookUpKey;
    public String name;
    public ContactItemInList(String lookUpKey, String name){
        this.lookUpKey = lookUpKey;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
