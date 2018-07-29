package com.eusecom.demoad.datamodel;

import android.location.Location;
import android.support.annotation.NonNull;
import com.eusecom.demoad.datamodel.datarepository.IDataRepController;
import com.eusecom.demoad.datamodel.model.IModelsFactory;
import com.eusecom.demoad.datamodel.model.RealmLocation;
import com.eusecom.demoad.datamodel.settrepository.ISettingsController;
import java.util.List;
import java.util.UUID;
import rx.Observable;

public class DemoadDataModel implements IDemoadDataModel {

    IDataRepController mDataRepController;
    IModelsFactory mModelsFactory;
    ISettingsController mSettingsController;

    public DemoadDataModel(@NonNull final IDataRepController dataRepController, @NonNull final IModelsFactory modelsFactory,
                           @NonNull final ISettingsController settingsController) {
        mDataRepController = dataRepController;
        mModelsFactory = modelsFactory;
        mSettingsController = mSettingsController;
    }


    //save location to realm
    @NonNull
    @Override
    public Observable<RealmLocation> saveLocationToRealm(@NonNull final Location locx) {

        setRealmLocationData(convertLocationToRealmLocation(locx));
        return Observable.just(convertLocationToRealmLocation(locx));

    }
    private void setRealmLocationData(@NonNull final RealmLocation locx) {

        mDataRepController.setRealmLocationData(locx);

    }

    //get locations
    @NonNull
    @Override
    public Observable<List<RealmLocation>> getLocationsFromRealm() {

        return Observable.just(mDataRepController.getLocationsFromRealmLocation());
    }

    //clear all location from realm
    @NonNull
    @Override
    public Observable<List<RealmLocation>> clearAllLocationsFromRealm() {

        return Observable.just(mDataRepController.clearAllLocationsFromRealm());

    }

    //get locations with max/min latitude/longitude
    @NonNull
    @Override
    public Observable<List<RealmLocation>> getMarginLocationsFromRealm(String wside) {

        return Observable.just(mDataRepController.getMarginLocsFromRealmLocation(wside));
    }


    //convert Location to RealmLocation to save in Realm database
    @NonNull
    public RealmLocation convertLocationToRealmLocation(Location loc) {

        Long idp = UUID.randomUUID().getMostSignificantBits();
        String unixTime = String.valueOf(System.currentTimeMillis());

        RealmLocation locx  = mModelsFactory.getRealmLocationModel();
        locx.setIdp(idp);
        locx.setLongitude(loc.getLongitude());
        locx.setLatitude(loc.getLatitude());
        locx.setAltitude(loc.getAltitude());
        locx.setTime(loc.getTime());
        locx.setAccuracy(loc.getAccuracy());
        locx.setSpeed(loc.getSpeed());
        locx.setProvider(loc.getProvider());
        locx.setMemo("Location Changed " + loc.getLatitude() + " : " + loc.getLongitude() + " : loc " + unixTime);

        return locx;
    }


}
