package com.eusecom.demoad.datamodel.datarepository;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.eusecom.demoad.datamodel.model.RealmLocation;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * The Implementation of RealmController for Realm data operations.
 *
 * @author  eurosecom
 * @version 1.0
 * @since   2018-06-23
 */
public class DataRepController implements IDataRepController {
 
    private static DataRepController instance;
    private final Realm realm;
 
    public DataRepController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static DataRepController getInstance() {

        return instance;
    }

    //get realm
    public Realm getRealm() {
 
        return realm;
    }
 
    //Refresh the realm istance
    public void refresh() {
 
        realm.refresh();
    }


    //get locations from RealmLocation
    public List<RealmLocation> getLocationsFromRealmLocation() {

        return realm.where(RealmLocation.class).findAllSorted("time");
    }


    //try if location exist
    public RealmLocation existRealmLocation(@NonNull final RealmLocation locx) {

        Long idpx = locx.getIdp();
        return realm.where(RealmLocation.class).equalTo("idp", idpx).findFirst();

    }


    //to save location into RealmLocation
    public void setRealmLocationData(@NonNull final RealmLocation locx) {

        realm.beginTransaction();
        realm.copyToRealm(locx);
        realm.commitTransaction();

    }


    //delete location from RealmLocation
    public void deleteRealmLocationData(@NonNull final RealmLocation locx) {

        Long idpx = locx.getIdp();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmLocation> result = realm.where(RealmLocation.class).equalTo("idp", idpx).findAll();
                result.clear();
            }
        });

    }


    //clear all locations from RealmLocation
    public List<RealmLocation> clearAllLocationsFromRealm() {

        realm.beginTransaction();
        realm.clear(RealmLocation.class);
        realm.commitTransaction();

        return getLocationsFromRealmLocation();

    }

    //get locations with max/min latitude/longitude
    public List<RealmLocation> getMarginLocsFromRealmLocation(String wside) {

        RealmResults<RealmLocation> results = realm.where(RealmLocation.class).findAll();

        List<RealmLocation> locs = null;
        if(wside.equals("N")) {
            double maxlat = results.max("latitude").doubleValue();
            locs = realm.where(RealmLocation.class).equalTo("latitude", maxlat).findAll();
        }
        if(wside.equals("S")) {
            double minlat = results.min("latitude").doubleValue();
            locs = realm.where(RealmLocation.class).equalTo("latitude", minlat).findAll();
        }
        if(wside.equals("W")) {
            double maxlon = results.max("longitude").doubleValue();
            locs = realm.where(RealmLocation.class).equalTo("longitude", maxlon).findAll();
        }
        if(wside.equals("E")) {
            double minlon = results.min("longitude").doubleValue();
            locs = realm.where(RealmLocation.class).equalTo("longitude", minlon).findAll();
        }


        return locs;
    }






}