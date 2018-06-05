package com.nozimy.app65_home1.ui.detail.mvp;

import com.nozimy.app65_home1.ui.common.mvp.MvpPresenter;
import com.nozimy.app65_home1.ui.common.mvp.MvpView;

public interface ContactDetailsContract {
    interface View extends MvpView{
        void subscribeUi();
    }
    interface Presenter<V extends ContactDetailsContract.View> extends MvpPresenter<V> {
        void loadDetails();
    }
}
