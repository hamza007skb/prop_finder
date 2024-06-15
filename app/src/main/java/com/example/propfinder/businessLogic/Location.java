package com.example.propfinder.businessLogic;

public class Location {
    private final long LONGITUDE;
    private final long LATITUDE;
    private final String CITY;
    private final int POSTEL_CODE;
    public long getLONGITUDE() {
        return LONGITUDE;
    }
    public long getLATITUDE() {
        return LATITUDE;
    }
    public String getCITY() {
        return CITY;
    }
    public int getPOSTEL_CODE() {
        return POSTEL_CODE;
    }
    public Location(long LONGITUDE, long LATITUDE, String CITY, int POSTEL_CODE) {
        this.LONGITUDE = LONGITUDE;
        this.LATITUDE = LATITUDE;
        this.CITY = CITY;
        this.POSTEL_CODE = POSTEL_CODE;
    }

    @Override
    public String toString() {
        return "Location{" +
                "LONGITUDE=" + LONGITUDE +
                ", LATITUDE=" + LATITUDE +
                ", CITY='" + CITY + '\'' +
                ", POSTEL_CODE=" + POSTEL_CODE +
                '}';
    }
}