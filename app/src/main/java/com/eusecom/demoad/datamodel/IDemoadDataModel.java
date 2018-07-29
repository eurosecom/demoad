package com.eusecom.demoad.datamodel;

import android.location.Location;
import android.support.annotation.NonNull;
import com.eusecom.demoad.datamodel.model.RealmLocation;
import java.util.List;
import rx.Observable;

public interface IDemoadDataModel {

    @NonNull
    public Observable<RealmLocation> saveLocationToRealm(Location locx);

    @NonNull
    public Observable<List<RealmLocation>> getLocationsFromRealm();

    @NonNull
    public Observable<List<RealmLocation>> clearAllLocationsFromRealm();

    @NonNull
    public RealmLocation convertLocationToRealmLocation(Location locx);

    @NonNull
    public Observable<List<RealmLocation>> getMarginLocationsFromRealm(String wside);


}
