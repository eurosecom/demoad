package com.eusecom.demoad.di.module;

import com.eusecom.demoad.di.scope.ActivityScope;
import com.eusecom.demoad.view.DemoadActivity;
import com.eusecom.demoad.view.MapsActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = {MainActivityModule.class })
    abstract DemoadActivity bindDemoadActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {MainActivityModule.class })
    abstract MapsActivity bindMapsActivity();

}
