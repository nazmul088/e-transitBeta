/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.TourismSide;

public class TouristRoute {

    public String district;
    public String latitude;
    public String longitude;
    public String placeName;

    public TouristRoute(String district, String latitude, String longitude, String placeName) {
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    public TouristRoute(){

    }
}
