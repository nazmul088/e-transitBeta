/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;


import java.io.Serializable;

public class RechargeCard implements Serializable {

    private int amount;
    private  boolean is_available;
    private String tokenID;
    public RechargeCard(){

    }


    public RechargeCard(int amount, boolean is_available) {
        this.amount = amount;
        this.is_available = is_available;
    }



    public RechargeCard(int amount, boolean is_available, String tokenID) {
        this.amount = amount;
        this.is_available = is_available;
        this.tokenID = tokenID;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

}