/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

public class Driver  extends User{

    private String name;
    private  String License_No;
    private String  experience;
    private boolean assigned;
    private String ownerId;

    public Driver(){

    }

    public Driver(String name, String license_No, String experience) {
        this.name = name;
        License_No = license_No;
        this.experience = experience;
    }

    public Driver(String ID, String email, String type, String name, String license_No, String experience,boolean assigned) {
        super(ID, email, type);
        this.name = name;
        License_No = license_No;
        this.experience = experience;
        this.assigned=assigned;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicense_No() {
        return License_No;
    }

    public void setLicense_No(String license_No) {
        License_No = license_No;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
