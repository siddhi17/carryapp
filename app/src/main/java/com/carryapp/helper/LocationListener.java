package com.carryapp.helper;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by vishaldharnkar on 05/08/16.
 */
public class LocationListener implements android.location.LocationListener{
    Location mLastLocation;
    private String TAG = "LOC_LIST";
    private AppCompatActivity context;
    private OnLocationAvailable onLocationAvailableListener;

    public void setOnLocationAvailableListener(OnLocationAvailable listener) {
        onLocationAvailableListener = listener;
    }
    public LocationListener(String provider)
    {
        Log.e(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
    }
    @Override
    public void onLocationChanged(Location location)
    {
        Log.e(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);
        onLocationAvailableListener.locationAvailable(location);

    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }
    @Override
    public void onProviderEnabled(String provider)
    {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.e(TAG, "onStatusChanged: " + provider);
    }
}

