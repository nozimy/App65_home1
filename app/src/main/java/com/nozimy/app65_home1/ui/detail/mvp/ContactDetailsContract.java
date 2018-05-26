package com.nozimy.app65_home1.ui.detail.mvp;

import android.app.Activity;

import com.nozimy.app65_home1.model.Contact;
import com.nozimy.app65_home1.ui.common.mvp.MvpPresenter;
import com.nozimy.app65_home1.ui.common.mvp.MvpView;
import com.nozimy.app65_home1.ui.common.mvp.PermissionsPresenter;

public interface ContactDetailsContract {
    interface View extends MvpView{
        boolean checkSelfPermission();
        boolean checkPermissionGranted(int permission);
        Activity getViewActivity();
        void setDetails(Contact contact);
    }
    interface Presenter<V extends ContactDetailsContract.View> extends MvpPresenter<V>, PermissionsPresenter {
        void loadDetails(String lookUpKey);
    }
}
