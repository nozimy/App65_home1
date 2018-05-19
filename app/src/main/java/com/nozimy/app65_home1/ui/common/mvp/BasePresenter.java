package com.nozimy.app65_home1.ui.common.mvp;

import com.nozimy.app65_home1.data.DataManager;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V>{
    DataManager dataManager;
    private V mvpView;

    public BasePresenter(DataManager dm){
        dataManager = dm;
    }


    @Override
    public void onAttach(V mvpV) {
        mvpView = mvpV;
    }

    public V getMvpView(){
        return mvpView;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
