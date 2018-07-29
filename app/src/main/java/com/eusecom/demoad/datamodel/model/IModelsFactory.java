package com.eusecom.demoad.datamodel.model;

import android.location.Location;
import android.support.annotation.NonNull;


public interface IModelsFactory {

    @NonNull
    RealmLocation getRealmLocationModel();

    @NonNull
    Location getLocationModel();


}
