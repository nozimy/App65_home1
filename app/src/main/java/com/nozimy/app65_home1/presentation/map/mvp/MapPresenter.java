package com.nozimy.app65_home1.ui.map.mvp;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nozimy.app65_home1.db.entity.ContactEntity;
import com.nozimy.app65_home1.domain.interactor.map.MapInteractor;
import com.nozimy.app65_home1.network.geocode.model.FeatureMember;
import com.nozimy.app65_home1.network.geocode.model.GeoObject;
import com.nozimy.app65_home1.ui.common.mvp.BasePresenter;
import com.nozimy.app65_home1.ui.map.MyMapFragment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MapPresenter<V extends MapContract.View> extends BasePresenter<V>
        implements MapContract.Presenter<V>{

    private GoogleMap mMap;

    private String contactId;
    private String contactName;
    private String contactAddress;
    private LatLng contactLatLng;

    MapInteractor mapInteractor;

    private boolean showAllContacts;

    public MapPresenter(MapInteractor mapInteractor) {
        this.mapInteractor = mapInteractor;
    }

    @Override
    public void setArguments(String contactId, boolean showAllContacts) {
        this.contactId = contactId;
        this.showAllContacts = showAllContacts;

        if(!showAllContacts){
            mapInteractor.getContact(contactId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(contactEntity -> {
                                this.contactName = contactEntity.getDisplayName();
                                if(contactEntity.getLat() != 0 && contactEntity.getLat() != 0){
                                    this.contactLatLng = new LatLng(contactEntity.getLat(), contactEntity.getLng());
                                }
                                this.contactAddress = contactEntity.getAddress();
                            },
                            Throwable::printStackTrace
                    );
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if(showAllContacts){
            mapInteractor.getContacts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(contactEntities -> {
                        showContactMarkers(contactEntities);
                    }, Throwable::printStackTrace);
        }else{
            if(contactId != null && contactLatLng != null){
                showMarker2(contactLatLng, contactName, contactAddress, false);
            }else{
                LatLng defaultLatLng = new LatLng(57, 53);
                showMarker2(defaultLatLng, "Выберите место", "", false);
            }
        }


    }

    @Override
    public void onMapClick(LatLng latLng) {
        contactLatLng = latLng;

        if(!showAllContacts){
            mapInteractor.getGeoObjects(contactLatLng.latitude, contactLatLng.longitude)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(geoResponse -> {
                                List<FeatureMember> geoObjects = geoResponse
                                        .getResponse()
                                        .getGeoObjectCollection()
                                        .getFeatureMembers();
                                if (geoObjects.size() == 0) {
                                    contactAddress = "Адрес не определен";
                                } else {
                                    GeoObject geoObject = geoObjects.get(0).getGeoObject();
                                    contactAddress = geoObject.getName() + "," + geoObject.getDescription();
                                }
                                showMarker2(contactLatLng, contactName, contactAddress, true);
                            }
                            , Throwable::printStackTrace
                    );
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(showAllContacts){
            String id = (String) marker.getTag();
            getMvpView().openContactDetails(id);
            return true;
        }
        return false;
    }

    @Override
    public void saveContactAddress(MyMapFragment.OnMapFragmentInteractionListener listener) {
        if(contactId != null && contactLatLng != null){
            mapInteractor.updateContactAddress(contactId, contactLatLng.latitude, 
            contactLatLng.longitude, contactAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        listener.popBackStack();
                    }, Throwable::printStackTrace);
        }
    }

    void showMarker2(LatLng contactLatLng, String contactName, String contactAddress,
     boolean clicked){
        mMap.clear();
        float zoom = 10;
        if(clicked){
            zoom = mMap.getCameraPosition().zoom;
        }
        Marker marker = mMap.addMarker(
                new MarkerOptions()
                        .position(contactLatLng)
                        .title(contactName)
                        .snippet(contactAddress)
        );
        marker.showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(contactLatLng, zoom));
    }

    void showContactMarkers(List<ContactEntity> contactEntities){
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        boolean isAnyMarker = false;

        for(ContactEntity contact: contactEntities){
            if(contact.getLat() != 0 && contact.getLat() != 0){
                isAnyMarker = true;

                LatLng latLng = new LatLng(contact.getLat(), contact.getLng());
                Marker marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(contact.getDisplayName())
                                .snippet(contact.getAddress())
                );
                marker.setTag(contact.getId());
                builder.include(latLng);
            }
        }
        if(isAnyMarker){
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(showAllContacts){
            String id = (String) marker.getTag();
            getMvpView().openContactDetails(id);
        }
    }
}
