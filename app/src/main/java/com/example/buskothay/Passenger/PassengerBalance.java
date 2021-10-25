/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;


public class PassengerBalance {

    String passengerID;
    String passenger_Name;
    int balance;

    public PassengerBalance(){

    }

    public PassengerBalance(String passengerID, int balance) {
        this.passengerID = passengerID;
        this.balance = balance;
    }

    public PassengerBalance(String passengerID, String passenger_Name, int balance) {
        this.passengerID = passengerID;
        this.passenger_Name = passenger_Name;
        this.balance = balance;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getPassenger_Name() {
        return passenger_Name;
    }

    public void setPassenger_Name(String passenger_Name) {
        this.passenger_Name = passenger_Name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
