package com.example.findmycar.contract;

import android.content.Context;
import android.location.Location;

import com.example.findmycar.model.Parking;
import com.example.findmycar.ui.fragment.BaseFragment;
import com.example.findmycar.ui.fragment.ParkingHistoryFragment;
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

    public interface BaseParkingPresenter {
        void addressNotFound();

        void loadFetchedAddress(String addressOutput);
    }

    public interface IHistoryView {
        void noParkingHistory();

        void showParkingList(List<Parking> list);

        void viewOnMap(double lat, double longitude);

        void showSaveAddDialog(String addressOutput);

        void notifyParkingListUpdated(Parking parking);
    }

    public interface IParkingPresenter extends BaseParkingPresenter {
        void LoadParkingHistory();

        void viewOnMap(double lat, double longitude);

        void enableGPS();

        void saveLocation(String address, String extraInfo, Location lastKnownLocation);
    }

    public interface IMapViewPresenter extends BaseParkingPresenter {

    }

    public interface IMapView {

        void showAddress(String addressOutput);
    }

    public interface IFragmentInteraction {
        void setCommunicator(MainContract.IActivityCommunicator communicator);

        void onSaveNewClicked();

        void onLocationUpdate(Location location);
    }

    public interface IActivityCommunicator {

        void showEnableGPSAlert();

        void showAddressNotFoundAlert();

        void addFragmentInterationListener(IFragmentInteraction fragmentInteraction);
    }
}
