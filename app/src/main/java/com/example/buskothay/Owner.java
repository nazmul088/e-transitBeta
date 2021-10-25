/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import java.util.List;

public class Owner  extends User {


    private List<String> busList;
    private  List<String> driverList;

    public Owner(List<String> busList, List<String> driverList) {
        this.busList = busList;
        this.driverList = driverList;
    }

    public Owner(String ID, String email, String type, List<String> busList, List<String> driverList) {
        super(ID, email, type);
        this.busList = busList;
        this.driverList = driverList;
    }

    public List<String> getBusList() {
        return busList;
    }

    public void setBusList(List<String> busList) {
        this.busList = busList;
    }

    public List<String> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<String> driverList) {
        this.driverList = driverList;
    }
}
