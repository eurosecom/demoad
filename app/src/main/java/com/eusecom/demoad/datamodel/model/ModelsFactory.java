package com.eusecom.demoad.datamodel.model;

import android.location.Location;
import android.support.annotation.NonNull;

public class ModelsFactory implements IModelsFactory {


    @NonNull
    public RealmLocation getRealmLocationModel() {

        return new RealmLocation();
    }

    @NonNull
    public Location getLocationModel() {

        return new Location("");
    }


}
