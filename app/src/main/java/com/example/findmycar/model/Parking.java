package com.example.findmycar.model;


public class Parking {

    public Parking(long time, String add, String extraInfo, double lat, double longitude) {
        this.timeOfParking = time;
        this.address = add;
        this.extraInfo = extraInfo;
        this.latitude = lat;
        this.longitude = longitude;

    }

    public Parking() {

    }

    public long getTimeOfParking() {
        return timeOfParking;
    }

    public void setTimeOfParking(long timeOfParking) {
        this.timeOfParking = timeOfParking;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    private long timeOfParking = 0;

    private String address;
    private String extraInfo;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double latitude;
    private double longitude;
}
