package com.nozimy.app65_home1.ui.listing.mvp;

import android.app.Activity;

import com.nozimy.app65_home1.model.Contact;
import com.nozimy.app65_home1.ui.common.mvp.MvpView;
import com.nozimy.app65_home1.ui.detail.DetailsFragment;

import java.util.List;

public interface ContactsListMvpView extends MvpView{
    boolean checkSelfPermission();
    boolean checkPermissionGranted(int permission);
    Activity getViewActivity();
    void setContacts(List<Contact> contacts);
    void showContactDetailsFragment(String lookUpKey);
    void openContactDetailsActivity(String lookUpKey);
    DetailsFragment getDetailsFragment();
    boolean isDualPane();
}
