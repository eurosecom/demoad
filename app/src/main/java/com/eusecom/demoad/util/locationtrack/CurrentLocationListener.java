package com.eusecom.demoad.util.locationtrack;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CurrentLocationListener extends LiveData<Location> {

    private static CurrentLocationListener instance;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private Context mContext;

    public static CurrentLocationListener getInstance(Context appContext) {
        if (instance == null) {
            instance = new CurrentLocationListener(appContext);
        }
        return instance;
    }

    private CurrentLocationListener(Context appContext) {
        mContext = appContext;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext);

        if(checkPermission(appContext)) {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null)
                        setValue(location);
                }
            });
            createLocationRequest();
        }


    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onActive() {
        super.onActive();

        if(checkPermission(mContext)) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (location != null)
                    setValue(location);
            }
        }
    };

    @Override
    protected void onInactive() {
        super.onInactive();
        if (mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private boolean checkPermission(Context appContext) {
        int result = ContextCompat.checkSelfPermission(appContext, ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

}
