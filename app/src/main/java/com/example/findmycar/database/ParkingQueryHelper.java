package com.example.findmycar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.example.findmycar.model.Parking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkingQueryHelper {


    private final Context mContext;
    private final ParkingDBHelper mDbhelper;

    public ParkingQueryHelper(Context context) {
        this.mContext = context;
        mDbhelper = ParkingDBHelper.getInstance(context);

    }


    public long saveLocation(Parking model) {
        long id = -1;
        SQLiteDatabase db = mDbhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ParkingReaderContract.ParkingEntry.COLUMN_NAME_TIME, model.getTimeOfParking());
        contentValues.put(ParkingReaderContract.ParkingEntry.COLUMN_NAME_ADD, model.getAddress());
        contentValues.put(ParkingReaderContract.ParkingEntry.COLUMN_NAME_EXTRA_INFO, model.getExtraInfo());
        contentValues.put(ParkingReaderContract.ParkingEntry.COLUMN_NAME_LAT, model.getLatitude());
        contentValues.put(ParkingReaderContract.ParkingEntry.COLUMN_NAME_LONG, model.getLongitude());

        try {
            id = db.insertOrThrow(ParkingReaderContract.ParkingEntry.TABLE_NAME, null, contentValues);
        } catch (SQLiteException e) {
            Log.d(ParkingReaderContract.TAG, "Exception: " + e.getMessage());
            Toast.makeText(mContext, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }

        return id;
    }

    public List<Parking> getHistoryList() {
        SQLiteDatabase sqLiteDatabase = mDbhelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(ParkingReaderContract.ParkingEntry.TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    List<Parking> parkingList = new ArrayList<Parking>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(ParkingReaderContract.ParkingEntry._ID));
                        long time = cursor.getLong(cursor.getColumnIndex(ParkingReaderContract.ParkingEntry.COLUMN_NAME_TIME));
                        String add = cursor.getString(cursor.getColumnIndex(ParkingReaderContract.ParkingEntry.COLUMN_NAME_ADD));
                        String extraInfo = cursor.getString(cursor.getColumnIndex(ParkingReaderContract.ParkingEntry.COLUMN_NAME_EXTRA_INFO));
                        double lat = cursor.getDouble(cursor.getColumnIndex(ParkingReaderContract.ParkingEntry.COLUMN_NAME_LAT));
                        double longitude = cursor.getDouble(cursor.getColumnIndex(ParkingReaderContract.ParkingEntry.COLUMN_NAME_LONG));

                        parkingList.add(new Parking(time, add, extraInfo, lat, longitude));
                    } while (cursor.moveToNext());

                    return parkingList;
                }
            }
        } catch (Exception e) {

        }
        return Collections.emptyList();
    }
}

