/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import com.example.buskothay.OwnerSide.DriverUpdated;

import java.util.ArrayList;

public class BusDetails {
    private String busNumber,from,route_Id,to,travelsName;
    private DriverUpdated driver;
    private User owner;
    private String driverId;
    private String modelName,sittingCapacity,registrationNumber,modelYear,registrationYear,busType;
    private ArrayList<String>bus_Stoppage;


    public BusDetails(ArrayList<String>bus_Stoppage,String from, User owner, String to, String travelsName, String modelName, String sittingCapacity, String registrationNumber, String modelYear, String registrationYear, String busType) {
        this.bus_Stoppage=bus_Stoppage;
        this.from = from;
        this.owner = owner;
        this.to = to;
        this.travelsName = travelsName;
        this.modelName = modelName;
        this.sittingCapacity = sittingCapacity;
        this.registrationNumber = registrationNumber;
        this.modelYear = modelYear;
        this.registrationYear = registrationYear;
        this.busType = busType;
    }

    public BusDetails(ArrayList<String>bus_Stoppage, String from, User owner, String route_Id, String to, String travelsName, DriverUpdated driver, String modelName, String sittingCapacity, String registrationNumber, String modelYear, String registrationYear, String busType) {
        this.bus_Stoppage=bus_Stoppage;

        this.from = from;
        this.owner = owner;
        this.route_Id = route_Id;
        this.to = to;
        this.travelsName = travelsName;
        this.driver = driver;
        this.modelName = modelName;
        this.sittingCapacity = sittingCapacity;
        this.registrationNumber = registrationNumber;
        this.modelYear = modelYear;
        this.registrationYear = registrationYear;
        this.busType = busType;
    }



    public BusDetails( String from, User owner, String to, String travelsName, String modelName, String sittingCapacity, String registrationNumber, String modelYear, String registrationYear, String bus_type, DriverUpdated driver) {

        this.from = from;
        this.owner = owner;
        this.to = to;
        this.travelsName = travelsName;
        this.modelName = modelName;
        this.sittingCapacity = sittingCapacity;
        this.registrationNumber = registrationNumber;
        this.modelYear = modelYear;
        this.registrationYear = registrationYear;
        this.busType = bus_type;
        this.driver = driver;
    }

        //Mader for Pending Request Owner class
    public BusDetails(ArrayList<String> stoppages, String from_location, User owner, String toLocation, String bus_name, String modelName, String sit_capacity, String reg_no, String modelYear, String reg_year, String bus_type, DriverUpdated driver) {
        this.bus_Stoppage = stoppages;
        this.from = from_location;
        this.owner = owner;
        this.to = toLocation;
        this.travelsName = bus_name;
        this.modelName = modelName;
        this.sittingCapacity = sit_capacity;
        this.registrationNumber = reg_no;
        this.modelYear = modelYear;
        this.registrationYear = reg_year;
        this.busType = bus_type;
        this.driver = driver;

    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public BusDetails() {
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
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

    public void setTravelsName(String travelsName) {
        this.travelsName = travelsName;
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

    public ArrayList<String> getBus_Stoppage() {
        return bus_Stoppage;
    }

    public void setBus_Stoppage(ArrayList<String> bus_Stoppage) {
        this.bus_Stoppage = bus_Stoppage;
    }
}
