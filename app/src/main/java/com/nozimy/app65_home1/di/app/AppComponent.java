package com.nozimy.app65_home1.di.app;

import com.nozimy.app65_home1.app.ContactListApp;
import com.nozimy.app65_home1.di.contact.ContactComponent;
import com.nozimy.app65_home1.di.contacts.ContactListComponent;
import com.nozimy.app65_home1.di.contacts.ImportServiceModule;
import com.nozimy.app65_home1.di.map.MapComponent;
import com.nozimy.app65_home1.di.route.RouteComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, RepositoryModule.class, NetworkModule.class})
public interface AppComponent {

    ContactListComponent plusContactListComponent(ImportServiceModule importServiceModule);
    ContactComponent plusContactComponent();
    MapComponent plusMapComponent();
    RouteComponent plusRouteComponent();

    void inject(ContactListApp contactListApp);

}
