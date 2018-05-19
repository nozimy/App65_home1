package com.nozimy.app65_home1.ui.listing.mvp;

import com.nozimy.app65_home1.ui.common.mvp.MvpPresenter;
import com.nozimy.app65_home1.ui.common.mvp.PermissionsPresenter;

public interface ContactsListMvpPresenter<V extends ContactsListMvpView> extends MvpPresenter<V>, PermissionsPresenter{
    void load();
    void showDetails(int position);
}
