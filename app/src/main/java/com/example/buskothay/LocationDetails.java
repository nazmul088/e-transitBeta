/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationDetails implements Parcelable {
    String latitude;
    String longitude;
    String placeName;


    public LocationDetails() {
    }

    public LocationDetails(String latitude, String longitude, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

    public LocationDetails(String currentLocationLatitude, String currentLocationLongitude) {
        this.latitude = currentLocationLatitude;
        this.longitude = currentLocationLongitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }



    private LocationDetails(Parcel in){
        this.longitude = in.readString();
        this.latitude = in. readString();
        this.placeName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(placeName);
    }

    public static final Creator<LocationDetails> CREATOR = new Creator<LocationDetails>(){
        public LocationDetails createFromParcel(Parcel in){
            return new LocationDetails(in);

        }

        @Override
        public LocationDetails[] newArray(int size) {
            return new LocationDetails[0];
        }
    };

    public static Creator<LocationDetails> getCREATOR() {
        return CREATOR;
    }
}
