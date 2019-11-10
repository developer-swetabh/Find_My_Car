package com.example.findmycar.presenter;

import com.example.findmycar.contract.MainContract;

public class MapViewImpl implements MainContract.IMapViewPresenter {


    private final MainContract.IMapView mMapView;
    private final MainContract.IActivityCommunicator mCommunicator;

    public MapViewImpl(MainContract.IMapView mapView, MainContract.IActivityCommunicator communicator) {
        mMapView = mapView;
        mCommunicator = communicator;
    }

    @Override
    public void addressNotFound() {

    }

    @Override
    public void loadFetchedAddress(String addressOutput) {
        mMapView.showAddress(addressOutput);
    }
}
