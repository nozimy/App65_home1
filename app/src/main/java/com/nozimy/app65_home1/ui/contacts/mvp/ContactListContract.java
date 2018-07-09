package com.nozimy.app65_home1.ui.contacts.mvp;

import android.app.Activity;

import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.ui.common.mvp.MvpPresenter;
import com.nozimy.app65_home1.ui.common.mvp.MvpView;
import com.nozimy.app65_home1.ui.common.mvp.PermissionsPresenter;
import com.nozimy.app65_home1.ui.contact.ContactFragment;

import java.util.List;

public interface ContactListContract {
    interface View extends MvpView {
        boolean checkSelfPermission();
        boolean checkPermissionGranted(int permission);
        Activity getViewActivity();
        void showContactDetailsFragment(String lookUpKey);
        void openContactDetails(String lookUpKey);
        ContactFragment getContactFragment();
        boolean isDualPane();
//        void subscribeUi();
        void showLoadingIndicator();

        void showContacts(List<ContactEntity> contacts);
        void setGoneVisibility();
    }
    interface Presenter<V extends ContactListContract.View> extends MvpPresenter<V>, PermissionsPresenter {
        void load();
        void showDetails(String id);
        void clearDisposable();
        void getContactsByDisplayName(String searchText);
    }
}
