package com.example.findmycar.presenter;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.example.findmycar.ParkingApplication;
import com.example.findmycar.contract.MainContract;
import com.example.findmycar.model.Parking;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import static com.example.findmycar.ui.fragment.ParkingHistoryFragment.FASTEST_INTERVAL;
import static com.example.findmycar.ui.fragment.ParkingHistoryFragment.UPDATE_INTERVAL;

public class ParkingHistoryImpl implements MainContract.IParkingPresenter {

    private final MainContract.IHistoryView mHistoryView;
    private final MainContract.IActivityCommunicator mActivityCommunicator;
    private LocationRequest locationRequest;
    private Context mContext;
    private LocationSettingsRequest.Builder mLocationBuilder;

    public ParkingHistoryImpl(MainContract.IHistoryView historyView, MainContract.IActivityCommunicator communicator) {
        mHistoryView = historyView;
        mActivityCommunicator = communicator;
    }

    @Override
    public void LoadParkingHistory() {
        List<Parking> list = ParkingApplication.getParkingHelperInstance().getHistoryList();
        if (list.isEmpty()) {
            mHistoryView.noParkingHistory();
        } else {
            mHistoryView.showParkingList(list);
        }
    }

    @Override
    public void viewOnMap(double lat, double longitude) {
        mHistoryView.viewOnMap(lat, longitude);
    }

    @Override
    public LocationRequest createLocationRequest(Context context, OnSuccessListener<LocationSettingsResponse> successListener, OnFailureListener failureListener) {
        mContext = context;
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        checkLocationSettings(successListener, failureListener);

        return locationRequest;
    }

    @Override
    public void addressNotFound() {
        mActivityCommunicator.showAddressNotFoundAlert();
    }

    @Override
    public void loadFetchedAddress(String addressOutput) {
        mHistoryView.showSaveAddDialog(addressOutput);
    }

    @Override
    public void checkLocationSettings(OnSuccessListener<LocationSettingsResponse> successListener, OnFailureListener failureListener) {
        SettingsClient client = LocationServices.getSettingsClient(mContext);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(mLocationBuilder.build());
        task.addOnSuccessListener(successListener);
        task.addOnFailureListener(failureListener);
    }

    @Override
    public void enableGPS() {
        mActivityCommunicator.showEnableGPSAlert();
    }

    @Override
    public void saveLocation(String address, String extraInfo, Location lastKnownLocation) {
        if (TextUtils.isEmpty(extraInfo)) {
            extraInfo = "";
        }
        Parking model = new Parking(System.currentTimeMillis(), address, extraInfo, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        ParkingApplication.getParkingHelperInstance().saveLocation(model);
    }
}
