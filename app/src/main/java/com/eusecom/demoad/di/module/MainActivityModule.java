package com.eusecom.demoad.di.module;

import android.content.Context;
import android.support.annotation.NonNull;
import com.eusecom.demoad.datamodel.DemoadDataModel;
import com.eusecom.demoad.datamodel.IDemoadDataModel;
import com.eusecom.demoad.datamodel.datarepository.IDataRepController;
import com.eusecom.demoad.datamodel.model.IModelsFactory;
import com.eusecom.demoad.datamodel.settrepository.ISettingsController;
import com.eusecom.demoad.di.scope.ActivityScope;
import com.eusecom.demoad.util.locationtrack.CurrentLocationListener;
import com.eusecom.demoad.util.mvvmschedulers.ISchedulerProvider;
import com.eusecom.demoad.viewmodel.DemoadViewModel;
import com.eusecom.demoad.viewmodel.IDemoadViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    @Provides
    @ActivityScope
    public IDemoadDataModel providesIDemoadDataModel(@NonNull final IDataRepController dataRepController,
            @NonNull final IModelsFactory modelsFactory, @NonNull final ISettingsController iSettingsController) {
        return new DemoadDataModel(dataRepController, modelsFactory, iSettingsController);
    }

    @Provides
    @ActivityScope
    public IDemoadViewModel providesIDemoadViewModel(IDemoadDataModel  dataModel,
            ISchedulerProvider schedulerProvider) {
        return new DemoadViewModel(dataModel, schedulerProvider);
    }

    @Provides
    @ActivityScope
    public CurrentLocationListener providesCurrentLocationListener(Context context) {
        return CurrentLocationListener.getInstance(context);
    }

}
