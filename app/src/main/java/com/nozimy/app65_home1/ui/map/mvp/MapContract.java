package com.nozimy.app65_home1.ui.map.mvp;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.nozimy.app65_home1.ui.common.mvp.MvpPresenter;
import com.nozimy.app65_home1.ui.common.mvp.MvpView;
import com.nozimy.app65_home1.ui.map.MyMapFragment;

public interface MapContract {

    interface View extends MvpView{
        void openContactDetails(String contactId);
    }

    interface Presenter<V extends MapContract.View>
            extends MvpPresenter<V>, OnMapReadyCallback, OnMapClickListener, OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

        void setArguments(String contactId, boolean showAllContacts);
        void saveContactAddress(MyMapFragment.OnMapFragmentInteractionListener listener);


    }
}
