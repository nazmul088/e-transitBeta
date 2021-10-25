/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import com.example.buskothay.StoppageList;

import java.util.ArrayList;

public class FarePay {


    String bus_reg_Id;
    String ownerId;
    String routeId;
    ArrayList<StoppageList> stoppageLists;

    public FarePay() {
    }

    public FarePay(String bus_reg_Id, String ownerId, String routeId) {
        this.bus_reg_Id = bus_reg_Id;
        this.ownerId = ownerId;
        this.routeId = routeId;
    }

    public FarePay(String bus_reg_Id, String ownerId, String routeId, ArrayList<StoppageList> stoppageLists) {
        this.bus_reg_Id = bus_reg_Id;
        this.ownerId = ownerId;
        this.routeId = routeId;
        this.stoppageLists = stoppageLists;
    }

    public String getBus_reg_Id() {
        return bus_reg_Id;
    }

    public void setBus_reg_Id(String bus_reg_Id) {
        this.bus_reg_Id = bus_reg_Id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public ArrayList<StoppageList> getStoppageLists() {
        return stoppageLists;
    }

    public void setStoppageLists(ArrayList<StoppageList> stoppageLists) {
        this.stoppageLists = stoppageLists;
    }
}
