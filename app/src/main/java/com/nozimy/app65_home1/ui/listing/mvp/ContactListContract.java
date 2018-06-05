package com.nozimy.app65_home1.ui.listing.mvp;

import android.app.Activity;

import com.nozimy.app65_home1.ui.common.mvp.MvpPresenter;
import com.nozimy.app65_home1.ui.common.mvp.MvpView;
import com.nozimy.app65_home1.ui.common.mvp.PermissionsPresenter;
import com.nozimy.app65_home1.ui.detail.DetailsFragment;

public interface ContactListContract {
    interface View extends MvpView {
        boolean checkSelfPermission();
        boolean checkPermissionGranted(int permission);
        Activity getViewActivity();
        void showContactDetailsFragment(String lookUpKey);
        void openContactDetailsActivity(String lookUpKey);
        DetailsFragment getDetailsFragment();
        boolean isDualPane();
        void subscribeUi();
        void showLoadingIndicator();
    }
    interface Presenter<V extends ContactListContract.View> extends MvpPresenter<V>, PermissionsPresenter {
        void load();
        void showDetails(String id);
    }
}
