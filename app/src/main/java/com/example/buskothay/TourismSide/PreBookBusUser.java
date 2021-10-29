/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.TourismSide;

public class PreBookBusUser {
    String BusNumber,userId,numberOfPerson,totalFare;

    public PreBookBusUser(String busNumber, String userId, String numberOfPerson, String totalFare) {
        BusNumber = busNumber;
        this.userId = userId;
        this.numberOfPerson = numberOfPerson;
        this.totalFare = totalFare;
    }

    public PreBookBusUser() {
    }

    public String getBusNumber() {
        return BusNumber;
    }

    public void setBusNumber(String busNumber) {
        BusNumber = busNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(String numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }
}
