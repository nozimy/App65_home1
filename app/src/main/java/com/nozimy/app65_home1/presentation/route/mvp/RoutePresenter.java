package com.nozimy.app65_home1.ui.route.mvp;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.domain.interactor.route.RouteInteractor;
import com.nozimy.app65_home1.network.route.model.Leg;
import com.nozimy.app65_home1.network.route.model.Location;
import com.nozimy.app65_home1.network.route.model.Route;
import com.nozimy.app65_home1.network.route.model.RouteResponse;
import com.nozimy.app65_home1.network.route.model.Step;
import com.nozimy.app65_home1.ui.common.mvp.BasePresenter;
import com.nozimy.app65_home1.ui.route.RouteFragment;
import com.nozimy.app65_home1.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RoutePresenter<V extends RouteContract.View> extends BasePresenter<V>
        implements RouteContract.Presenter<V>{

    private GoogleMap mMap;
    private RouteInteractor routeInteractor;

    private ContactEntity contactA;
    private ContactEntity contactB;

    public RoutePresenter(RouteInteractor routeInteractor) {
        this.routeInteractor = routeInteractor;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        init();

        if(contactA != null
                && contactB != null
                && contactA.getLat() != 0
                && contactA.getLng() != 0
                && contactB.getLat() != 0
                && contactB.getLng() != 0){

            String a = contactA.getLat()+","+contactA.getLng();
            String b = contactB.getLat()+","+contactB.getLng();

            routeInteractor
                    .getRoute(a, b)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(routeResponse -> {
                        showRoute(routeResponse,contactA, contactB);
                    }, Throwable::printStackTrace);
        }else{
            showRouteErrorAlert();
        }

    }

    //TODO: Улучшить рисование линии маршрута
    void showRoute(RouteResponse routeResponse, ContactEntity originContact, 
        ContactEntity destinationContact){

        LatLng origin = new LatLng(originContact.getLat(), originContact.getLng());
        LatLng destination = new LatLng(destinationContact.getLat(), destinationContact.getLng());

        String status = routeResponse.getStatus();
        Location startLocation = null;
        ArrayList<Location> routerPoints = new ArrayList<>();

        if (!status.equals("OK")) {
            if(routeResponse.getErrorMessage() != null){
                Log.d("RESPONSE",routeResponse.getErrorMessage());
            }
            showRouteErrorAlert();
        } else {
            List<Route> routes = routeResponse.getRoutes();
            Route route = routes.get(0);
            List<Leg> legs = route.getLegs();
            Leg leg = legs.get(0);
            List<Step> steps = leg.getSteps();
            startLocation = steps.get(0).getStartLocation();
            for (Step step : steps) {
                routerPoints.add(step.getEndLocation());
            }
        }

        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();
        mMap
            .addMarker(new MarkerOptions().position(origin).title(originContact.getDisplayName())
            .snippet(originContact.getAddress()));
        mMap
            .addMarker(new MarkerOptions().position(destination).title(destinationContact.getDisplayName())
            .snippet(destinationContact.getAddress()));

        PolylineOptions polylineOptions = new PolylineOptions();
        if (startLocation != null & routerPoints != null) {
            polylineOptions.add(new LatLng(startLocation.getLat(), startLocation.getLng()));
            for (Location point: routerPoints) {
                polylineOptions.add(new LatLng(point.getLat(), point.getLng()));
            }
        }
        polylineOptions.width(10);
        polylineOptions.color(Color.BLUE);
        mMap.addPolyline(polylineOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    public void init() {
        if(contactA != null){
            getMvpView().setButtonAText(contactA.getDisplayName());
        }
        if(contactB != null){
            getMvpView().setButtonBText(contactB.getDisplayName());
        }
    }

    @Override
    public void loadContact(String contactId, int pointType) {
        routeInteractor.getContact(contactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactEntity -> {
                    if(pointType == RouteFragment.POINT_A){
                        contactA = contactEntity;
                    } else if (pointType == RouteFragment.POINT_B){
                        contactB = contactEntity;
                    }
                }, Throwable::printStackTrace);
    }

    void showRouteErrorAlert(){
        getMvpView().showToast("Невозможно построить маршрут");
    }
}

