package com.example.findmycar.contract;

import android.content.Context;
import android.location.Location;

import com.example.findmycar.model.Parking;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainContract {

    public static final String TAG = "FindMyCar";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.example.findmycar";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";

    public interface IHistoryView {
        void noParkingHistory();

        void showParkingList(List<Parking> list);

        void viewOnMap(double lat, double longitude);

        void showSaveAddDialog(String addressOutput);

        void notifyParkingListUpdated(Parking parking);
    }

    public interface IParkingPresenter {
        void LoadParkingHistory();

        void viewOnMap(double lat, double longitude);

        LocationRequest createLocationRequest(Context context, OnSuccessListener<LocationSettingsResponse> successListener, OnFailureListener failureListener);

        void addressNotFound();

        void loadFetchedAddress(String addressOutput);

        void checkLocationSettings(OnSuccessListener<LocationSettingsResponse> successListener, OnFailureListener failureListener);

        void enableGPS();

        void saveLocation(String address, String extraInfo, Location lastKnownLocation);
    }

    public interface IFragmentInteraction {
        void onSaveNewClicked();
    }

    public interface IActivityCommunicator {

        void showLocationSettingsDialog(Exception e);

        void showEnableGPSAlert();

        void showAddressNotFoundAlert();

    }
}
