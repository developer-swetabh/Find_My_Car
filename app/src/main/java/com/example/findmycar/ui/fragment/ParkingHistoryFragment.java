package com.example.findmycar.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.findmycar.R;
import com.example.findmycar.adapters.ParkingAdapter;
import com.example.findmycar.contract.MainContract;
import com.example.findmycar.model.Parking;
import com.example.findmycar.presenter.ParkingHistoryImpl;
import com.example.findmycar.ui.receivers.AddressResultReceiver;
import com.example.findmycar.ui.services.FetchAddressIntentService;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ParkingHistoryFragment extends BaseFragment implements MainContract.IHistoryView,
        OnFailureListener, OnSuccessListener<LocationSettingsResponse> {

    MainContract.IParkingPresenter presenter;
    private TextView tv_NoHistory;
    private RecyclerView rv_ParkingListView;
    private Context mContext;
    public static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private AddressResultReceiver mResultReceiver;
    private Location lastKnownLocation;
    private ParkingAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_history, container, false);
        mContext = getActivity();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            getActivity().onBackPressed();
        }
        tv_NoHistory = view.findViewById(R.id.tv_no_history);
        rv_ParkingListView = view.findViewById(R.id.rv_parking_list);
        rv_ParkingListView.setHasFixedSize(true);
        rv_ParkingListView.setLayoutManager(new LinearLayoutManager(mContext));
        presenter = new ParkingHistoryImpl(this, mCommunicator);
        mAdapter = new ParkingAdapter(presenter, new ArrayList<Parking>());
        rv_ParkingListView.setAdapter(mAdapter);
        presenter.LoadParkingHistory();
        mResultReceiver = new AddressResultReceiver(new Handler(), presenter);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    lastKnownLocation = location;
                    Log.d(MainContract.TAG, "LAT =" + location.getLatitude() + ", long = " + location.getLongitude());
                }
            }
        };
        locationRequest = presenter.createLocationRequest(mContext, this, this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(MainContract.TAG, "onResume() : starting location updates");
        startLocationUpdates();

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            getActivity().onBackPressed();
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onSaveNewClicked() {
        //presenter.checkLocationSettings(this, this);
        if (lastKnownLocation != null)
            startIntentService();
        else {
            presenter.enableGPS();
        }
    }

    private void startIntentService() {
        Intent intent = new Intent(mContext, FetchAddressIntentService.class);
        intent.putExtra(MainContract.RECEIVER, mResultReceiver);
        intent.putExtra(MainContract.LOCATION_DATA_EXTRA, lastKnownLocation);
        mContext.startService(intent);
    }

    @Override
    public void noParkingHistory() {
        rv_ParkingListView.setVisibility(View.GONE);
        tv_NoHistory.setText("No history list found");
    }

    @Override
    public void showParkingList(List<Parking> list) {
        mAdapter.updateList(list);
        tv_NoHistory.setVisibility(View.GONE);
    }

    @Override
    public void viewOnMap(double lat, double longitude) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", lat + "," + longitude);
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void showSaveAddDialog(String addressOutput) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_save_location, null);
        final EditText address = dialogLayout.findViewById(R.id.edt_address);
        final EditText extraInfo = dialogLayout.findViewById(R.id.edt_extra);
        address.setText(addressOutput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.saveLocation(address.getText().toString(), extraInfo.getText().toString(), lastKnownLocation);
            }
        });
        builder.setCancelable(true);
        builder.setView(dialogLayout);
        builder.show();
    }

    @Override
    public void notifyParkingListUpdated(Parking parking) {
        Log.d(MainContract.TAG, "notifyParkingListUpdated");
        mAdapter.updateList(parking);
    }


    @Override
    public void onFailure(@NonNull Exception e) {
        if (e instanceof ResolvableApiException) {
            // Location settings are not satisfied, but this can be fixed
            // by showing the user a dialog.
            mCommunicator.showLocationSettingsDialog(e);
        }
    }

    @Override
    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        startLocationUpdates();
    }
}
