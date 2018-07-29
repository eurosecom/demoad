package com.eusecom.demoad.di.component;

import com.eusecom.demoad.di.module.MainActivityModule;
import com.eusecom.demoad.di.scope.ActivityScope;
import com.eusecom.demoad.view.MapsActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MapsActivityComponent extends AndroidInjector<MapsActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MapsActivity>{}
}
