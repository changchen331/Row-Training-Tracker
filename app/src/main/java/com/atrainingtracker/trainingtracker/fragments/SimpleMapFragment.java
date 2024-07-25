package com.atrainingtracker.trainingtracker.fragments;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.atrainingtracker.trainingtracker.TrainingApplication;
import com.atrainingtracker.trainingtracker.fragments.mapFragments.BaseMapFragment;
import com.google.android.gms.maps.GoogleMap;

public class SimpleMapFragment extends BaseMapFragment {
    public static final String TAG = SimpleMapFragment.class.getName();
    private static final boolean DEBUG = TrainingApplication.DEBUG & true;

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull final GoogleMap map) {
        if (DEBUG) Log.i(TAG, "onMapReady");
        super.onMapReady(map);

        mMap.setMyLocationEnabled(true);
        centerMapOnMyLocation(14, 0);
    }
}