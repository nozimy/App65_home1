package com.nozimy.app65_home1.di.contact;

import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.di.scope.FragmentScope;
import com.nozimy.app65_home1.domain.interactor.contact.ContactInteractor;
import com.nozimy.app65_home1.domain.interactor.contact.ContactInteractorDefault;
import com.nozimy.app65_home1.ui.contact.mvp.ContactDetailsContract;
import com.nozimy.app65_home1.ui.contact.mvp.ContactPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ContactModule {

    @Provides
    @FragmentScope
    ContactInteractor provideContactInteractor(DataRepository dataRepository){
        return new ContactInteractorDefault(dataRepository);
    }

    @Provides
    @FragmentScope
    ContactDetailsContract.Presenter provideContactPresenter(ContactInteractor contactInteractor, CompositeDisposable compositeDisposable){
        return new ContactPresenter(contactInteractor, compositeDisposable);
    }

}
