package com.example.findmycar.ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findmycar.R;
import com.example.findmycar.contract.MainContract;

public class AddressFragment<T> extends DialogFragment implements MainContract.IFragmentInteraction {
    MainContract.IActivityCommunicator mCommunicator;

    public void setCommunicator(T communicator) {

    }

    @Override
    public void setCommunicator(MainContract.IActivityCommunicator communicator) {
        mCommunicator = (MainContract.IActivityCommunicator) communicator;
    }

    @Override
    public void onSaveNewClicked() {

    }

    @Override
    public void onLocationUpdate(Location location) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_save_location, container);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
