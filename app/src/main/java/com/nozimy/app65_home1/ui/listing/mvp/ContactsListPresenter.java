package com.nozimy.app65_home1.ui.listing.mvp;

import android.Manifest;
import android.support.v4.app.ActivityCompat;

import com.nozimy.app65_home1.data.DataManager;
import com.nozimy.app65_home1.ui.common.mvp.BasePresenter;
import com.nozimy.app65_home1.utils.CommonUtils;

public class ContactsListPresenter<V extends ContactsListMvpView> extends BasePresenter<V> implements ContactsListMvpPresenter<V> {

    private static final int REQUEST_CODE_READ_CONTACTS=1;

    private String curLookUpKey;

    public ContactsListPresenter(DataManager dm) {
        super(dm);
    }

    public void load(){
        getMvpView().setContacts(getDataManager().getContacts());
    }

    @Override
    public void requestReadContacts() {
        if(!getMvpView().checkSelfPermission()){
            //todo: I should't use android lib in the presenter.
            // Use DI or anything else to decouple this behaviour.
            // https://medium.com/@cervonefrancesco/you-can-do-that-by-hiding-this-behavior-behind-an-interface-to-get-the-location-lets-say-e7a778c9ea6e
            ActivityCompat.requestPermissions(getMvpView().getViewActivity(), new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
    }

    @Override
    public void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean readContactsGranted = false;

        switch (requestCode){
            case REQUEST_CODE_READ_CONTACTS:
                if(grantResults.length > 0 && getMvpView().checkPermissionGranted(grantResults[0])){
                    readContactsGranted = true;
                }
        }

        if(readContactsGranted){
            load();
        }
        else{
            CommonUtils.showToast(getMvpView().getViewActivity(), "Требуется установить разрешения");
        }
    }

    @Override
    public void showDetails(int position) {
        curLookUpKey = getDataManager().getContacts().get(position).lookUpKey;

        if (getMvpView().isDualPane()) {
            if (getMvpView().getDetailsFragment() == null || !curLookUpKey.equals(getMvpView().getDetailsFragment().getShownLookUpKey())) {
                getMvpView().showContactDetailsFragment(curLookUpKey);
            }
        } else {
            getMvpView().openContactDetailsActivity(curLookUpKey);
        }
    }
}
