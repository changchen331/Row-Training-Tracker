package com.atrainingtracker.banalservice.devices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.atrainingtracker.banalservice.BANALService;
import com.atrainingtracker.banalservice.sensor.MySensorManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SpeedAndLocationDevice_GoogleFused extends SpeedAndLocationDevice implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "SpeedAndLocationDevice_GoogleFused";
    private static final boolean DEBUG = BANALService.DEBUG & false;

    protected GoogleApiClient mGoogleApiClient;

    protected LocationRequest mLocationRequest;

    @SuppressLint("LongLogTag")
    public SpeedAndLocationDevice_GoogleFused(Context context, MySensorManager mySensorManager) {
        super(context, mySensorManager, DeviceType.SPEED_AND_LOCATION_GOOGLE_FUSED);
        if (DEBUG) {
            Log.d(TAG, "constructor");
        }

        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(SAMPLING_TIME);

        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    public String getName() {
        return "google_fused";   // here, we do not use R.string to be compatible with the old (pre 3.8) way
    }

    @SuppressLint({"LongLogTag", "MissingPermission"})
    @Override
    public void onConnected(Bundle dataBundle) {
        if (DEBUG) Log.d(TAG, "onConnected()");

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");

        LocationUnavailable();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (DEBUG) Log.d(TAG, "onLocationChanged");
        onNewLocation(location);
    }

    @Override
    public void shutDown() {
        mGoogleApiClient.disconnect();

        super.shutDown();
    }
}