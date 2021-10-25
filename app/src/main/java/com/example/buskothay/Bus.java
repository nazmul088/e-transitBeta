/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

public class Bus {
    String busId;
    String travelsName;
    String busNumber;
    String date;
    String from;
    String to;
    String busCondition;
    String ownerId;
    String route_Id;
    String driver_Id;


    public Bus() {
    }

    public Bus(String busId,String travelsName, String busNumber, String from, String to, String busCondition) {
        this.busId = busId;
        this.travelsName = travelsName;
        this.busNumber = busNumber;
        //this.route_Id=route_Id;
        this.from = from;
        this.to = to;
        this.busCondition = busCondition;
    }

    public String getTravelsName() {
        return travelsName;
    }

    public void setTravelsName(String travelsName) {
        this.travelsName = travelsName;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getBusCondition() {
        return busCondition;
    }

    public void setBusCondition(String busCondition) {
        this.busCondition = busCondition;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRoute_Id() {
        return route_Id;
    }

    public String getDriver_Id() {
        return driver_Id;
    }

    public void setDriver_Id(String driver_Id) {
        this.driver_Id = driver_Id;
    }

    public void setRoute_Id(String route_Id) {
        this.route_Id = route_Id;
    }
}


