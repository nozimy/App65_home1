package com.nozimy.app65_home1.di.map;

import com.nozimy.app65_home1.db.DataRepository;
import com.nozimy.app65_home1.di.scope.FragmentScope;
import com.nozimy.app65_home1.domain.interactor.map.MapInteractor;
import com.nozimy.app65_home1.domain.interactor.map.MapInteractorDefault;
import com.nozimy.app65_home1.network.geocode.GeocodeApiClient;
import com.nozimy.app65_home1.ui.map.mvp.MapContract;
import com.nozimy.app65_home1.ui.map.mvp.MapPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MapModule {


    @Provides
    @FragmentScope
    MapInteractor provideMapInteractor(DataRepository dataRepository, 
                                        GeocodeApiClient geocodeApiClient){
        return new MapInteractorDefault(dataRepository, geocodeApiClient);
    }

    @Provides
    @FragmentScope
    MapContract.Presenter provideMapPresenter(MapInteractor mapInteractor){
        return new MapPresenter(mapInteractor);
    }

}
