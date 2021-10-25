/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import java.io.Serializable;

public class FairPayment implements Serializable {
    String passengerId;
    String rideId;
    String from;
    String to;
    int amount;
    String fromTime;
    String toTime;
    String date;
    boolean direction;
    String busRegistrationId;
    String busName;


    public FairPayment() {
    }

    public FairPayment(String passengerId, String rideId, String from, String to, int amount, String fromTime, String toTime, String date,boolean direction,String busRegistrationId,String busName) {
        this.passengerId = passengerId;
        this.rideId = rideId;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.date = date;
        this.direction = direction;
        this.busRegistrationId = busRegistrationId;
        this.busName = busName;
    }

    public FairPayment(String uid, String rideId, String from, String to, int amount, String fromTime, String toTime, String date,String busRegistrationId,String busName) {
        this.passengerId = uid;
        this.rideId = rideId;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.date = date;
        this.busRegistrationId = busRegistrationId;
        this.busName = busName;
    }


    public String getBusRegistrationId() {
        return busRegistrationId;
    }

    public void setBusRegistrationId(String busRegistrationId) {
        this.busRegistrationId = busRegistrationId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
