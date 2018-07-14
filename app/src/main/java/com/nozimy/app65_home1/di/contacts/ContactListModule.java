package com.nozimy.app65_home1.di.contacts;


import com.nozimy.app65_home1.ImportService;
import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.di.scope.FragmentScope;
import com.nozimy.app65_home1.domain.interactor.contacts.ContactListInteractor;
import com.nozimy.app65_home1.domain.interactor.contacts.ContactListInteractorDefault;
import com.nozimy.app65_home1.ui.contacts.mvp.ContactListContract;
import com.nozimy.app65_home1.ui.contacts.mvp.ContactListPresenter;
import com.nozimy.app65_home1.utils.Settings;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ContactListModule {

    @Provides
    @FragmentScope
    ContactListInteractor provideContactListInteractor(DataRepository dataRepository, 
                                                        ImportService importService){
        return new ContactListInteractorDefault(dataRepository, importService);
    }

    @Provides
    @FragmentScope
    ContactListContract.Presenter provideContactListPresenter(ContactListInteractor contactListInteractor, 
                                                                Settings settings, 
                                                                CompositeDisposable compositeDisposable){
        return new ContactListPresenter(contactListInteractor, settings, compositeDisposable);
    }
}
