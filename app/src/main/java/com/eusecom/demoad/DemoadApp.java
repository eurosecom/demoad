package com.eusecom.demoad;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import com.eusecom.demoad.di.component.DaggerAppComponent;
import com.eusecom.demoad.util.mvvmschedulers.ISchedulerProvider;
import com.eusecom.demoad.util.mvvmschedulers.SchedulerProvider;
import com.squareup.leakcanary.LeakCanary;
import javax.inject.Inject;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DemoadApp extends MultiDexApplication implements HasActivityInjector {

    //dagger 2.11
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @NonNull
    private SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();



        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        //Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //dagger 2.11
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);


    }

    //dagger 2.11
    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @NonNull
    public Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }


}
