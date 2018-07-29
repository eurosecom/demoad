package com.eusecom.demoad.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.eusecom.demoad.DemoadApp;
import com.eusecom.demoad.datamodel.datarepository.DataRepController;
import com.eusecom.demoad.datamodel.datarepository.IDataRepController;
import com.eusecom.demoad.datamodel.model.IModelsFactory;
import com.eusecom.demoad.datamodel.model.ModelsFactory;
import com.eusecom.demoad.datamodel.settrepository.ISettingsController;
import com.eusecom.demoad.datamodel.settrepository.SettingsController;
import com.eusecom.demoad.util.mvvmschedulers.ISchedulerProvider;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;


/**
 * AppModule
 */
@Module(subcomponents = {})
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    ISettingsController ISettingsController(SharedPreferences sharedPreferences) {
        return new SettingsController(sharedPreferences);
    }

    @Provides
    @Singleton
    public ISchedulerProvider providesISchedulerProvider(Application application) {

        return ((DemoadApp) application).getSchedulerProvider();
    }

    @Provides
    @Singleton
    IDataRepController providesIDataRepConroller(Application application) {
        return new DataRepController(application);
    }

    @Provides
    @Singleton
    IModelsFactory provideIModelsFactory() {
        return new ModelsFactory();
    }



}
