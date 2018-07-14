package com.nozimy.app65_home1.ui.common.mvp;

import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.domain.interactor.BaseInteractor;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V>{
    private V mvpView;

    @Override
    public void onAttach(V mvpV) {
        mvpView = mvpV;
    }

    public V getMvpView(){
        return mvpView;
    }
}
