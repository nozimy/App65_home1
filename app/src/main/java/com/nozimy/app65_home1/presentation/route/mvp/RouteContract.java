package com.nozimy.app65_home1.ui.route.mvp;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.nozimy.app65_home1.ui.common.mvp.MvpPresenter;
import com.nozimy.app65_home1.ui.common.mvp.MvpView;

public interface RouteContract {

    interface View extends MvpView{
        void showToast(String message);
        void setButtonAText(String a);
        void setButtonBText(String b);
    }

    interface Presenter<V extends RouteContract.View> extends MvpPresenter<V>, OnMapReadyCallback {
        void init();
        void loadContact(String contactId, int pointType);
    }
}
