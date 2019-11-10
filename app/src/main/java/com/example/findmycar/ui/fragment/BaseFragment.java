package com.example.findmycar.ui.fragment;

import android.location.Location;
import android.support.v4.app.Fragment;

import com.example.findmycar.contract.MainContract;

public class BaseFragment extends Fragment implements MainContract.IFragmentInteraction {

    protected MainContract.IActivityCommunicator mCommunicator;

    public void setCommunicator(MainContract.IActivityCommunicator communicator) {
        mCommunicator = (MainContract.IActivityCommunicator) communicator;
    }

    @Override
    public void onSaveNewClicked() {
    }

    @Override
    public void onLocationUpdate(Location location) {

    }
}
