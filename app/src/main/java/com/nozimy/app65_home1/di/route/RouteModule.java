package com.nozimy.app65_home1.di.route;

import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.di.scope.FragmentScope;
import com.nozimy.app65_home1.domain.interactor.route.RouteInteractor;
import com.nozimy.app65_home1.domain.interactor.route.RouteInteractorуDefault;
import com.nozimy.app65_home1.network.route.RouteApiClient;
import com.nozimy.app65_home1.ui.route.mvp.RouteContract;
import com.nozimy.app65_home1.ui.route.mvp.RoutePresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class RouteModule {

    @Provides
    @FragmentScope
    RouteInteractor provideRouteInteractor(RouteApiClient routeApiClient, 
                                            DataRepository dataRepository){
        return new RouteInteractorуDefault(routeApiClient, dataRepository);
    }

    @Provides
    @FragmentScope
    RouteContract.Presenter provideRoutePresenter(RouteInteractor routeInteractor){
        return new RoutePresenter(routeInteractor);
    }
}
