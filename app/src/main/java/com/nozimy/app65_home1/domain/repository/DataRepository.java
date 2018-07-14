package main.java.com.nozimy.app65_home1.domain.repository;

public interface DataRepository{
    public Single<List<Contact>> getByDisplayNameRx(String searchText);

    public Completable importFromProvider(ImportService service);

    public Single<List<Contact>> getContactsRx();

    public Maybe<Contact> getContact(String contactId);

    public Maybe<List<Phone>> getPhones(String contactId);

    public Maybe<List<Email>> getEmails(String contactId);

    public void updateContactAddress(String id, double lat, double lng, String address);
}
