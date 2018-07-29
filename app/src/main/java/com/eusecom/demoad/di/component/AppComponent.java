package com.eusecom.demoad.di.component;

import android.app.Application;

import com.eusecom.demoad.DemoadApp;
import com.eusecom.demoad.di.module.ActivityBuilder;
import com.eusecom.demoad.di.module.AppModule;
import javax.inject.Singleton;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


/**
 * AppComponent
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Application application);
        AppComponent build();
    }

    void inject(DemoadApp app);
}
