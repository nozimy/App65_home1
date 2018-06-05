package com.nozimy.app65_home1.ui.listing.mvp;

import com.nozimy.app65_home1.DataRepository;
import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.utils.Settings;
import com.nozimy.app65_home1.ui.common.mvp.BasePresenter;
import com.nozimy.app65_home1.utils.CommonUtils;

public class ContactsListPresenter<V extends ContactListContract.View> extends BasePresenter<V> implements ContactListContract.Presenter<V> {

    public static final int REQUEST_CODE_READ_CONTACTS=1;
    private ImportService importService;
    private Settings settings;

    public ContactsListPresenter(DataRepository repository, ImportService service, Settings settings) {
        super(repository);
        this.importService = service;
        this.settings = settings;
    }

    public void load(){
        if(!settings.getContactsImported()){
            getMvpView().showLoadingIndicator();
            getDataRepository().importFromProvider(importService);
            settings.setContactsImported(true);
        }
        getMvpView().subscribeUi();
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
    public void showDetails(String id) {

        if (getMvpView().isDualPane()) {
            if (getMvpView().getDetailsFragment() == null || !id.equals(getMvpView().getDetailsFragment().getShownId())) {
                getMvpView().showContactDetailsFragment(id);
            }
        } else {
            getMvpView().openContactDetailsActivity(id);
        }
    }
}
