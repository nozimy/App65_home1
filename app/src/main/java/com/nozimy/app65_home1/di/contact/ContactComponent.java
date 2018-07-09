package com.nozimy.app65_home1.di.contact;

import com.nozimy.app65_home1.di.common.DisposableModule;
import com.nozimy.app65_home1.di.scope.FragmentScope;
import com.nozimy.app65_home1.ui.contact.ContactFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {ContactModule.class, DisposableModule.class})
public interface ContactComponent {

    void inject(ContactFragment contactFragment);

}
