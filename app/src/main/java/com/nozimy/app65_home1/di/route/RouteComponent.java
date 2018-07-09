package com.nozimy.app65_home1.di.route;

import com.nozimy.app65_home1.di.scope.FragmentScope;
import com.nozimy.app65_home1.ui.route.RouteFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {RouteModule.class})
public interface RouteComponent {
    void inject(RouteFragment routeFragment);
}
