package com.example.findmycar;

import android.app.Application;
import android.content.Context;

import com.example.findmycar.database.ParkingQueryHelper;

public class ParkingApplication extends Application {

    private static ParkingQueryHelper mParkingHelper ;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static ParkingQueryHelper getParkingHelperInstance(){
        if(mParkingHelper==null){
            mParkingHelper = new ParkingQueryHelper(context);
        }

        return mParkingHelper;
    }
}
