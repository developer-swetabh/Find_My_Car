package com.example.findmycar.ui.receivers;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.findmycar.contract.MainContract;

public class AddressResultReceiver extends ResultReceiver {
    private final MainContract.BaseParkingPresenter mPresenter;

    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     * @param presenter
     */
    public AddressResultReceiver(Handler handler, MainContract.BaseParkingPresenter presenter) {
        super(handler);
        mPresenter = presenter;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (resultData == null) {
            return;
        }

        // Display the address string
        // or an error message sent from the intent service.
        String addressOutput = resultData.getString(MainContract.RESULT_DATA_KEY);
        if (addressOutput == null) {
            addressOutput = "";
        }

        // Show message if an address was found.
        if (resultCode == MainContract.SUCCESS_RESULT) {
            Log.d(MainContract.TAG, "Address Found Success : " + addressOutput);
            mPresenter.loadFetchedAddress(addressOutput);
        } else {
            Log.d(MainContract.TAG, "Address Not Found Success : " + addressOutput);
            mPresenter.addressNotFound();
        }

    }
}
