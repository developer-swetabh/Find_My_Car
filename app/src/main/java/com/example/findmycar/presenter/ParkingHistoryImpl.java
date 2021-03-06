package com.example.findmycar.presenter;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

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

    public ParkingHistoryImpl(MainContract.IHistoryView historyView, MainContract.IActivityCommunicator communicator) {
        mHistoryView = historyView;
        mActivityCommunicator = communicator;
    }

    @Override
    public void LoadParkingHistory() {
        List<Parking> list = ParkingApplication.getParkingHelperInstance().getHistoryList();
        if (list.isEmpty()) {
            Log.d(MainContract.TAG, "No parking list");
            mHistoryView.noParkingHistory();
        } else {
            Log.d(MainContract.TAG, "contains some list");
            mHistoryView.showParkingList(list);
        }
    }

    @Override
    public void viewOnMap(double lat, double longitude) {
        mHistoryView.viewOnMap(lat, longitude);
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
        mHistoryView.notifyParkingListUpdated(model);
    }
}
