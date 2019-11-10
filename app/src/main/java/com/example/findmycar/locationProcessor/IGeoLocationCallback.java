package com.example.findmycar.locationProcessor;

import android.location.Location;

public interface IGeoLocationCallback {

    void onGeoLocationResult(Location locationResult);

    void onLocationFailure(Exception e);
}
