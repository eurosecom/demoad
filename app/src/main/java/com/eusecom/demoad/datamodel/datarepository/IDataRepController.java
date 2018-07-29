package com.eusecom.demoad.datamodel.datarepository;

import com.eusecom.demoad.datamodel.model.RealmLocation;
import java.util.List;
import io.realm.Realm;

public interface IDataRepController {

    //get realm
    Realm getRealm();

    //Refresh the realm istance
    void refresh();

    List<RealmLocation> getLocationsFromRealmLocation();

    RealmLocation existRealmLocation(RealmLocation domx);

    void setRealmLocationData(RealmLocation domx);

    void deleteRealmLocationData(RealmLocation domx);

    public List<RealmLocation> clearAllLocationsFromRealm();

    List<RealmLocation> getMarginLocsFromRealmLocation(String wside);

}
