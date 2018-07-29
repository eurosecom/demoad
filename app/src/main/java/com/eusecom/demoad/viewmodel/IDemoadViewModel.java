package com.eusecom.demoad.viewmodel;

import android.location.Location;
import android.support.annotation.NonNull;
import com.eusecom.demoad.datamodel.model.RealmLocation;
import java.util.List;
import rx.Observable;

public interface IDemoadViewModel {

    //methods for RealmLocation
    @NonNull
    public Observable<List<RealmLocation>> getMyLocsFromRealm();

    @NonNull
    public void emitSaveLocationToRealm(Location locx);

    @NonNull
    public Observable<RealmLocation> savedLocationToRealm();

    @NonNull
    public void clearObservableSaveLocationToRealm();

    @NonNull
    public void emitClearAllLocsFromRealm(String podm);

    @NonNull
    public Observable<List<RealmLocation>> clearedLocationsFromRealm();

    @NonNull
    public void clearObservableClearAllLocsFromRealm();

    @NonNull
    public void emitMarginLocsFromRealm(String wside);

    @NonNull
    public Observable<List<RealmLocation>> getMarginLocsFromRealm();

    @NonNull
    public void clearObservableMarginLocsFromRealm();

}
