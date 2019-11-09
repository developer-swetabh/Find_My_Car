package com.example.findmycar.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class MapView extends BaseFragment implements MainContract.IMapView, OnMapReadyCallback {

    private MainContract.IMapViewPresenter presenter;
    private AddressResultReceiver mResultReceiver;
    private GoogleMap mMap;
    private Marker currentMarker;
    private LatLng startLatLong;


    public MapView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        presenter = new MapViewImpl(this, mCommunicator);
        final Button location = rootView.findViewById(R.id.button_loc);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(this);
       /* mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //mMap.clear();

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(12.9464507, 77.705724))
                        .zoom(17)
                        .bearing(0)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);
                *//*currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.4219999, -122.0862462))
                        .title("Random Loc")
                );*//*
                //.icon(bitmapDescriptorFromVector(getActivity(),R.drawable.spider)));

            }
        });
        if (mMap != null) {
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng center = mMap.getCameraPosition().target;
                    if (currentMarker != null) {
                        currentMarker.remove();

                        //location.setText(getStringAddress);
                    }
                    currentMarker = mMap.addMarker(new MarkerOptions().position(center).title("new position"));
                    startLatLong = currentMarker.getPosition();
                }
            });
        }*/
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (MainActivity.mLastknownlocation != null) {
            LatLng latLng = new LatLng(MainActivity.mLastknownlocation.getLatitude()
                    , MainActivity.mLastknownlocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Current Location"));

            CameraPosition currentLoc = CameraPosition.builder()
                    .target(latLng)
                    .zoom(17)
                    .bearing(0)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(currentLoc), 10000, null);

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}
