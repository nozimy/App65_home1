package com.nozimy.app65_home1.di.contacts;

import com.nozimy.app65_home1.di.common.DisposableModule;
import com.nozimy.app65_home1.di.scope.FragmentScope;
import com.nozimy.app65_home1.ui.contacts.ContactListFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {ContactListModule.class, ImportServiceModule.class, DisposableModule.class})
public interface ContactListComponent {

    void inject(ContactListFragment contactListFragment);

}
