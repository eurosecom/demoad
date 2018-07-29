package com.eusecom.demoad.viewmodel;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eusecom.demoad.datamodel.IDemoadDataModel;
import com.eusecom.demoad.datamodel.model.RealmLocation;
import com.eusecom.demoad.util.mvvmschedulers.ISchedulerProvider;
import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Demoad View model.
 */
public class DemoadViewModel implements IDemoadViewModel {

    IDemoadDataModel mDataModel;

    ISchedulerProvider mSchedulerProvider;


    public DemoadViewModel(@NonNull final IDemoadDataModel dataModel,
                                     @NonNull final ISchedulerProvider schedulerProvider) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;

    }

    //method for RealmLocation
    //emit SaveLocation ToRealm
    public void emitSaveLocationToRealm(Location locx) {
        mObservableSaveLocationToRealm.onNext(locx);
    }

    @NonNull
    private BehaviorSubject<Location> mObservableSaveLocationToRealm = BehaviorSubject.create();

    @NonNull
    public Observable<RealmLocation> savedLocationToRealm() {

        return mObservableSaveLocationToRealm
                .observeOn(mSchedulerProvider.ui())
                .distinctUntilChanged((location1, location2) ->
                        filterLocationsByDistance(location1, location2)
                )
                .flatMap(domx -> mDataModel.saveLocationToRealm(domx ));

    }

    public void clearObservableSaveLocationToRealm() {

        mObservableSaveLocationToRealm = BehaviorSubject.create();

    }
    //end emit Save LocationToRealm



    /**
     * Rx filter for emited Locations before save location to Realm compare item to
     * RxJava distinctUntilChanged operator. It only compares emitted items from the source Observable against
     * their immediate predecessors in order to determine whether or not they are distinct.
     */
    public boolean filterLocationsByDistance(Location location1, Location location2) {

        float distanceInMeters = location1.distanceTo(location2);
        Log.d("DemoadActivityMsg dist", String.valueOf(distanceInMeters));

       return (distanceInMeters < 5);
    }

    //emit Clear Locations FromRealm
    public void emitClearAllLocsFromRealm(String podm) {
        mObservableClearLocsFromRealm.onNext(podm);
    }

    @NonNull
    private BehaviorSubject<String> mObservableClearLocsFromRealm = BehaviorSubject.create();

    @NonNull
    public Observable<List<RealmLocation>> clearedLocationsFromRealm() {

        return mObservableClearLocsFromRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(podm -> mDataModel.clearAllLocationsFromRealm());

    }

    public void clearObservableClearAllLocsFromRealm() {

        mObservableClearLocsFromRealm = BehaviorSubject.create();

    }
    //end emit Save LocationToRealm

    public Observable<List<RealmLocation>> getMyLocsFromRealm() {

        return mDataModel.getLocationsFromRealm();
    }

    //emit margin Locations FromRealm
    public void emitMarginLocsFromRealm(String wside) {
        mObservableMarginLocsFromRealm.onNext(wside);
    }

    @NonNull
    private BehaviorSubject<String> mObservableMarginLocsFromRealm = BehaviorSubject.create();

    @NonNull
    public Observable<List<RealmLocation>> getMarginLocsFromRealm() {

        return mObservableMarginLocsFromRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(wside -> mDataModel.getMarginLocationsFromRealm(wside));

    }

    public void clearObservableMarginLocsFromRealm() {

        mObservableMarginLocsFromRealm = BehaviorSubject.create();

    }
    //end emit margin Location FromRealm


}
