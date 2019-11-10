package com.example.findmycar.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.findmycar.R;
import com.example.findmycar.contract.MainContract;
import com.example.findmycar.presenter.MapViewImpl;
import com.example.findmycar.presenter.ParkingHistoryImpl;
import com.example.findmycar.ui.activity.MainActivity;
import com.example.findmycar.ui.receivers.AddressResultReceiver;
import com.example.findmycar.ui.services.FetchAddressIntentService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayDeque;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapView extends BaseFragment implements MainContract.IMapView,
        OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private MainContract.IMapViewPresenter presenter;
    private AddressResultReceiver mResultReceiver;
    private GoogleMap mMap;
    private Marker currentMarker;
    private LatLng startLatLong;
    private boolean locationFound = false;
    private Context mContext;
    private Location lastKnownLocation;
    private Button locationBtn;
    private String mAddress = null;


    public MapView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        mContext = getActivity();
        presenter = new MapViewImpl(this, mCommunicator);
        mResultReceiver = new AddressResultReceiver(new Handler(), presenter);
        locationBtn = rootView.findViewById(R.id.button_loc);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(this);
        mCommunicator.addFragmentInterationListener(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnCameraIdleListener(this);
    }

    @Override
    public void onLocationUpdate(Location location) {
        Log.d(MainContract.TAG, "MapView : onLocationUpdate : LAT =" + location.getLatitude() + ", long = " + location.getLongitude());
        if (location != null && !locationFound) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition currentLoc = CameraPosition.builder()
                    .target(latLng)
                    .zoom(19.5f)
                    .bearing(0)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(currentLoc), 10000, null);
            lastKnownLocation = location;
            locationFound = true;
        }
    }

    private void startIntentService() {
        Intent intent = new Intent(mContext, FetchAddressIntentService.class);
        intent.putExtra(MainContract.RECEIVER, mResultReceiver);
        intent.putExtra(MainContract.LOCATION_DATA_EXTRA, lastKnownLocation);
        mContext.startService(intent);
    }

    @Override
    public void showAddress(String addressOutput) {
        locationBtn.setText(addressOutput);
        mAddress = addressOutput;
    }

    @Override
    public void onCameraIdle() {
        LatLng center = mMap.getCameraPosition().target;
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(center).title("new position").visible(false));
        startLatLong = currentMarker.getPosition();
        if (lastKnownLocation != null) {
            startIntentService();
            lastKnownLocation.setLatitude(startLatLong.latitude);
            lastKnownLocation.setLongitude(startLatLong.longitude);
        }
    }

    @Override
    public void onSaveNewClicked() {

    }
}
