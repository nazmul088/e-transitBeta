/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

public class Route {
    private String From,ID,Stoppage,To;

    public Route(String from, String ID, String stoppage, String to) {
        From = from;
        this.ID = ID;
        Stoppage = stoppage;
        To = to;
    }

    public Route() {
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStoppage() {
        return Stoppage;
    }

    public void setStoppage(String stoppage) {
        Stoppage = stoppage;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }
}
