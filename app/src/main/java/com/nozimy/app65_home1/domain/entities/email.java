package main.java.com.nozimy.app65_home1.domain.entities;

public class Email {

    private int id;
    private String contactId;
    private String address;

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
