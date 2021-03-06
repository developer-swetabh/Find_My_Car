package com.example.findmycar.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.List;

public class ParkingHistoryFragment extends BaseFragment implements MainContract.IHistoryView {

    private MainContract.IParkingPresenter presenter;
    private TextView tv_NoHistory;
    private RecyclerView rv_ParkingListView;
    private Context mContext;
    public static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    private AddressResultReceiver mResultReceiver;
    private Location lastKnownLocation;
    private ParkingAdapter mAdapter;
    private String mAddressOutput = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_history, container, false);
        mContext = getActivity();
        tv_NoHistory = view.findViewById(R.id.tv_no_history);
        rv_ParkingListView = view.findViewById(R.id.rv_parking_list);
        rv_ParkingListView.setHasFixedSize(true);
        rv_ParkingListView.setLayoutManager(new LinearLayoutManager(mContext));
        presenter = new ParkingHistoryImpl(this, mCommunicator);
        mAdapter = new ParkingAdapter(presenter);
        rv_ParkingListView.setAdapter(mAdapter);
        presenter.LoadParkingHistory();
        mResultReceiver = new AddressResultReceiver(new Handler(), presenter);
        mCommunicator.addFragmentInterationListener(this);
        return view;
    }

    @Override
    public void onLocationUpdate(Location location) {
        Log.d(MainContract.TAG, "onLocationUpdate : LAT =" + location.getLatitude() + ", long = " + location.getLongitude());
        lastKnownLocation = location;
    }

    @Override
    public void onSaveNewClicked() {
        Log.d(MainContract.TAG, "onSaveNewClicked");
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
        address.setText(mAddressOutput == null ? addressOutput : mAddressOutput);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.saveLocation(address.getText().toString(), extraInfo.getText().toString(), lastKnownLocation);
                mAddressOutput = null;
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.fetch_add_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startIntentService();
                mAddressOutput = null;
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.setView(dialogLayout);
        builder.show();
    }

    @Override
    public void notifyParkingListUpdated(Parking parking) {
        Log.d(MainContract.TAG, "notifyParkingListUpdated");
        rv_ParkingListView.setVisibility(View.VISIBLE);
        if(tv_NoHistory.getVisibility() == View.VISIBLE){
            tv_NoHistory.setVisibility(View.GONE);
        }
        mAdapter.updateList(parking);
    }

    @Override
    public void onSetNewAddress(String addressOutput) {
        mAddressOutput = addressOutput;
    }
}
