package com.atrainingtracker.banalservice.devices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.atrainingtracker.banalservice.BANALService;
import com.atrainingtracker.banalservice.sensor.MySensorManager;

public class SpeedAndLocationDevice_GPS extends SpeedAndLocationDevice implements LocationListener {
    private static final String TAG = "SpeedAndLocationDevice_GPS";
    private static final boolean DEBUG = BANALService.DEBUG & false;

    LocationManager mLocationManager;

    @SuppressLint({"LongLogTag", "MissingPermission"})
    public SpeedAndLocationDevice_GPS(Context context, MySensorManager mySensorManager) {
        super(context, mySensorManager, DeviceType.SPEED_AND_LOCATION_GPS);
        if (DEBUG) {
            Log.d(TAG, "constructor");
        }

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, SAMPLING_TIME, MIN_DISTANCE, this);
    }

    @Override
    public String getName() {
        return "gps";   // here, we do not use R.string to be compatible with the old (pre 3.8) way
    }

    @Override
    public void shutDown() {
        mLocationManager.removeUpdates(this);

        super.shutDown();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (DEBUG) Log.d(TAG, "onStatusChanged(" + provider + ", " + status + ")");
    }

    @SuppressLint({"LongLogTag", "MissingPermission"})
    @Override
    public void onProviderEnabled(@NonNull String provider) {
        if (DEBUG) Log.d(TAG, "onProviderEnabled: " + provider);
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            if (DEBUG) Log.d(TAG, "GPS location provider enabled");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, SAMPLING_TIME, MIN_DISTANCE, this);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            if (DEBUG) Log.d(TAG, "GPS location provider disabled");
            mLocationManager.removeUpdates(this);
            LocationUnavailable();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (DEBUG) Log.i(TAG, "onLocationChanged");

        onNewLocation(location);
    }
}
