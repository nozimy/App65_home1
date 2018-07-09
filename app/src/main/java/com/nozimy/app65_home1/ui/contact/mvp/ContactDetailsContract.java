package com.nozimy.app65_home1.ui.contact.mvp;

import com.nozimy.app65_home1.ui.common.mvp.MvpPresenter;
import com.nozimy.app65_home1.ui.common.mvp.MvpView;

public interface ContactDetailsContract {

    interface View extends MvpView{
        void showContact(String name, String phones, String emails);
        void setProgressBarVisible();
        void setProgressBarGone();
    }

    interface Presenter<V extends ContactDetailsContract.View> extends MvpPresenter<V> {
        void getContact(String contactId);
        void clearDisposable();
    }

}
