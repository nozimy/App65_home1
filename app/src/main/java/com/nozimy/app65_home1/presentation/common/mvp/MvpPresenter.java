package com.nozimy.app65_home1.ui.common.mvp;

public interface MvpPresenter<V extends MvpView> {
    void onAttach(V mvpView);
}
