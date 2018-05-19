package com.nozimy.app65_home1.ui.detail.mvp;

import android.Manifest;
import android.support.v4.app.ActivityCompat;

import com.nozimy.app65_home1.data.DataManager;
import com.nozimy.app65_home1.ui.common.mvp.BasePresenter;
import com.nozimy.app65_home1.utils.CommonUtils;

public class ContactDetailsPresenter<V extends ContactDetailsContract.View> extends BasePresenter<V> implements ContactDetailsContract.Presenter<V> {

    private static final int REQUEST_CODE_READ_CONTACTS=1;

    public ContactDetailsPresenter(DataManager dm) {
        super(dm);
    }

    @Override
    public void loadDetails(String lookUpKey) {
        getMvpView().setDetails(getDataManager().getContact(lookUpKey));
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

        if(!readContactsGranted){
            CommonUtils.showToast(getMvpView().getViewActivity(), "Требуется установить разрешения");
        }
    }
}
