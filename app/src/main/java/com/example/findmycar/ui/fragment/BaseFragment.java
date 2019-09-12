package com.example.findmycar.ui.fragment;

import android.support.v4.app.Fragment;

import com.example.findmycar.contract.MainContract;

public class BaseFragment<T> extends Fragment implements MainContract.IFragmentInteraction {

    protected MainContract.IActivityCommunicator mCommunicator;

    public void setCommunicator(T communicator) {
        mCommunicator = (MainContract.IActivityCommunicator) communicator;
    }

    @Override
    public void onSaveNewClicked() {
    }
}
