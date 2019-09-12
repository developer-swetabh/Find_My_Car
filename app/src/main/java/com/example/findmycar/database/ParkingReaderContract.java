package com.example.findmycar.database;

import android.provider.BaseColumns;

public class ParkingReaderContract {
    public static final String TAG = "CAR PARKING";

    private ParkingReaderContract(){}

    public static class ParkingEntry implements BaseColumns{
        public static final String TABLE_NAME = "parking";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_EXTRA_INFO = "extra_info";
        public static final String COLUMN_NAME_ADD = "address";
        public static final String COLUMN_NAME_LAT = "latitude";
        public static final String COLUMN_NAME_LONG = "longitude";





    }
}
