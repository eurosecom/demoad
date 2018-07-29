package com.eusecom.demoad.di.component;

import com.eusecom.demoad.di.module.MainActivityModule;
import com.eusecom.demoad.di.scope.ActivityScope;
import com.eusecom.demoad.view.DemoadActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface DemoadActivityComponent extends AndroidInjector<DemoadActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DemoadActivity>{}
}
