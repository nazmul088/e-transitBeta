/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.AuthoritySide;

import com.example.buskothay.OwnerSide.DriverUpdated;
import com.example.buskothay.User;

public class BusUpdated {
    private String from,route_Id,to,travelsName;
    private DriverUpdated driver;
    private User owner;
    private String modelName,sittingCapacity,registrationNumber,modelYear,registrationYear,busType;
    private String fileName;

    public BusUpdated() {
    }

    public BusUpdated(String from, String route_Id, String to, String travelsName, User owner, String modelName, String sittingCapacity, String registrationNumber, String modelYear, String registrationYear, String busType, String fileName) {
        this.from = from;
        this.route_Id = route_Id;
        this.to = to;
        this.travelsName = travelsName;
        this.owner = owner;
        this.modelName = modelName;
        this.sittingCapacity = sittingCapacity;
        this.registrationNumber = registrationNumber;
        this.modelYear = modelYear;
        this.registrationYear = registrationYear;
        this.busType = busType;
        this.fileName = fileName;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRoute_Id() {
        return route_Id;
    }

    public void setRoute_Id(String route_Id) {
        this.route_Id = route_Id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTravelsName() {
        return travelsName;
    }

    public void setTravelsName(String travelsName) {
        this.travelsName = travelsName;
    }

    public DriverUpdated getDriver() {
        return driver;
    }

    public void setDriver(DriverUpdated driver) {
        this.driver = driver;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSittingCapacity() {
        return sittingCapacity;
    }

    public void setSittingCapacity(String sittingCapacity) {
        this.sittingCapacity = sittingCapacity;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(String registrationYear) {
        this.registrationYear = registrationYear;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
