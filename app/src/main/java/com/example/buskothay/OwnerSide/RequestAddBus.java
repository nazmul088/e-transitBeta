/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import com.example.buskothay.User;

import java.util.ArrayList;

public class RequestAddBus {


    private User owner;

    private String busName;
    private String modelName;
    private String registrationNumber;
    private String sittingCapacity;
    private String modelYear;
    private String registrationYear;
    private String busType;
    private String fromLocation;
    private String toLocation;
    private ArrayList<String> stoppages;
    private String busFileName;

    public RequestAddBus() {
    }

    public RequestAddBus(User owner, String busName, String modelName, String registrationNumber, String sittingCapacity, String modelYear, String registrationYear, String busType, String fromLocation, String toLocation, ArrayList<String> stoppages, String busFileName) {
        this.owner = owner;
        this.busName = busName;
        this.modelName = modelName;
        this.registrationNumber = registrationNumber;
        this.sittingCapacity = sittingCapacity;
        this.modelYear = modelYear;
        this.registrationYear = registrationYear;
        this.busType = busType;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.stoppages = stoppages;
        this.busFileName = busFileName;
    }


    public String getBusFileName() {
        return busFileName;
    }

    public void setBusFileName(String busFileName) {
        this.busFileName = busFileName;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getSittingCapacity() {
        return sittingCapacity;
    }

    public void setSittingCapacity(String sittingCapacity) {
        this.sittingCapacity = sittingCapacity;
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

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public ArrayList<String> getStoppages() {
        return stoppages;
    }

    public void setStoppages(ArrayList<String> stoppages) {
        this.stoppages = stoppages;
    }


}