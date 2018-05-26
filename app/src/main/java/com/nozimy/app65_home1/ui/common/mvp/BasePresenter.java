package com.nozimy.app65_home1.ui.common.mvp;

import com.nozimy.app65_home1.DataRepository;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V>{
    DataRepository dataRepository;
    private V mvpView;

    public BasePresenter(DataRepository repository){
        dataRepository = repository;
    }

    @Override
    public void onAttach(V mvpV) {
        mvpView = mvpV;
    }

    public V getMvpView(){
        return mvpView;
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }
}
