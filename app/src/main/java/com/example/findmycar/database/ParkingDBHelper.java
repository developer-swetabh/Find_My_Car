package com.example.findmycar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.findmycar.database.ParkingReaderContract.*;


public class ParkingDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Parking.db";
    private static ParkingDBHelper sInstance;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ParkingEntry.TABLE_NAME + " (" +
                    ParkingEntry._ID + " INTEGER PRIMARY KEY," +
                    ParkingEntry.COLUMN_NAME_TIME + " INTEGER," +
                    ParkingEntry.COLUMN_NAME_ADD + " TEXT," +
                    ParkingEntry.COLUMN_NAME_EXTRA_INFO + " TEXT," +
                    ParkingEntry.COLUMN_NAME_LAT + " REAL," +
                    ParkingEntry.COLUMN_NAME_LONG + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ParkingEntry.TABLE_NAME;

    public ParkingDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static ParkingDBHelper getInstance(Context context) {
        if (sInstance == null)
            sInstance = new ParkingDBHelper(context);
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
