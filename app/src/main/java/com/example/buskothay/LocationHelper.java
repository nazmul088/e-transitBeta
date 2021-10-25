/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

public class LocationHelper {
    private String uId;
    private String busId;
    private String status;
    private String latitude;
    private String longitude;

    public LocationHelper(String uId, String latitude, String longitude) {
        this.uId = uId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationHelper(String uId,String status, String latitude, String longitude) {
        this.uId = uId;

        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
