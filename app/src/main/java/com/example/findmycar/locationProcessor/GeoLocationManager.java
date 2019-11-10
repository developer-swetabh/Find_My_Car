package com.example.findmycar.locationProcessor;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class GeoLocationManager implements OnFailureListener, OnSuccessListener<LocationSettingsResponse> {

    public static final long UPDATE_INTERVAL = 5000,
            FASTEST_INTERVAL = 5000; // = 5 seconds
    private final Context mContext;
    private final IGeoLocationCallback mLocationCallback;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback googleLocationCallback;
    private LocationSettingsRequest.Builder mLocationBuilder;

    public GeoLocationManager(Context context, IGeoLocationCallback locationCallback) {
        mContext = context;
        mLocationCallback = locationCallback;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        createLocationRequest();
        googleLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    mLocationCallback.onGeoLocationResult(location);
                    Log.d("CurrentLocation : ", "LAT =" + location.getLatitude() + ", long = " + location.getLongitude());
                }
            }
        };
    }

    public void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(mContext);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(mLocationBuilder.build());
        task.addOnSuccessListener(this);
        task.addOnFailureListener(this);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        mLocationCallback.onLocationFailure(e);
    }

    @Override
    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        startLocationUpdates();
    }

    public void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                googleLocationCallback,
                null /* Looper */);
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(googleLocationCallback);
    }
}
