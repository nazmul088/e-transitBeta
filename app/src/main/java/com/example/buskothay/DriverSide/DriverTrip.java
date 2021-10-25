/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.DriverSide;

public class DriverTrip {
    private String date;
    private String TripStartTime;
    private String TripEndTime;
    private String From;
    private String To;
    private String currentLocationLatitude;
    private String currentLocationLongitude;
    private String bearing;
    private String Ride_id;
    private String Registration_Number;
    private String TripStatus;
    private String travelsName;
    private String lastKnownLocation;
    private String Route_id;
    private String busType;



    public String getTripStartTime() {
        return TripStartTime;
    }





    public DriverTrip() {
        this.To= "";
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public DriverTrip(String date, String tripStartTime, String tripEndTime, String from, String to, String currentLocationLatitude, String currentLocationLongitude, String bearing, String ride_id, String bus_id, String tripStatus, String travelsName, String lastKnownLocation, String Route_id,String busType) {
        this.date = date;
        TripStartTime = tripStartTime;
        TripEndTime = tripEndTime;
        From = from;
        To = to;
        this.currentLocationLatitude = currentLocationLatitude;
        this.currentLocationLongitude = currentLocationLongitude;
        this.bearing = bearing;
        Ride_id = ride_id;
        Registration_Number = bus_id;
        TripStatus = tripStatus;
        this.travelsName = travelsName;
        this.lastKnownLocation = lastKnownLocation;
        this.Route_id = Route_id;
        this.busType = busType;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getRoute_id() {
        return Route_id;
    }

    public void setRoute_id(String route_id) {
        Route_id = route_id;
    }

    public String getCurrentLocationLatitude() {
        return currentLocationLatitude;
    }

    public void setCurrentLocationLatitude(String currentLocationLatitude) {
        this.currentLocationLatitude = currentLocationLatitude;
    }

    public String getCurrentLocationLongitude() {
        return currentLocationLongitude;
    }

    public void setCurrentLocationLongitude(String currentLocationLongitude) {
        this.currentLocationLongitude = currentLocationLongitude;
    }

    public void setTripStartTime(String tripStartTime) {
        TripStartTime = tripStartTime;
    }

    public String getTripEndTime() {
        return TripEndTime;
    }

    public void setTripEndTime(String tripEndTime) {
        TripEndTime = tripEndTime;
    }

    public String getTripStatus() {
        return TripStatus;
    }

    public void setTripStatus(String tripStatus) {
        TripStatus = tripStatus;
    }




    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }


    public String getRide_id() {
        return Ride_id;
    }

    public void setRide_id(String ride_id) {
        Ride_id = ride_id;
    }


    public String getRegistration_Number() {
        return Registration_Number;
    }

    public void setRegistration_Number(String registration_Number) {
        Registration_Number = registration_Number;
    }

    public String getTravelsName() {
        return travelsName;
    }

    public void setTravelsName(String travelsName) {
        this.travelsName = travelsName;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }
}
