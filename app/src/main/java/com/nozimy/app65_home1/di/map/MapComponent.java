package com.nozimy.app65_home1.di.map;

import com.nozimy.app65_home1.di.scope.FragmentScope;
import com.nozimy.app65_home1.ui.map.MyMapFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {MapModule.class})
public interface MapComponent {
    void inject(MyMapFragment myMapFragment);
}
