/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import java.io.Serializable;

public class User implements Serializable {
    private String ID;
    private String email;
    private String type;


    public User() {
        //this contructor is required
    }

    public User(String ID, String email, String type) {
        this.ID = ID;
        this.email = email;
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
