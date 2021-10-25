/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.buskothay.LoginActivity;
import com.example.buskothay.R;

public class OwnerWindow extends AppCompatActivity {

    // private Button button;
    private CardView button;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_view_activity);

        SharedPreferences prefs = this.getSharedPreferences("logInPref", Context.MODE_PRIVATE);
        currentUser = prefs.getString("userId", "");


        button = (CardView) findViewById(R.id.card33);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity9();
            } //Assign Driver Activity
        });
        button = (CardView) findViewById(R.id.card11);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            } //Add Bus Activity
        });
        button = (CardView) findViewById(R.id.card22);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity44();
            } //View Bus Activity
        });
        button = (CardView) findViewById(R.id.card44);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_pending();
            } //See pending Request
        });

        button = (CardView) findViewById(R.id.card55);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_v44();
            } //for Logout
        });


        button = (CardView) findViewById(R.id.card_fare);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_card_fare();
            }
        });


    }


    public void openActivity9() {
        //Intent intent=new Intent(this,LoginActivity.class);
        Intent intent = new Intent(this, viewBusToAssignDriver.class);
        intent.putExtra("UserID", currentUser);
        startActivity(intent);

    }

    public void openActivity4() {
        //Intent intent=new Intent(this,LoginActivity.class);
        Intent intent = new Intent(this, AddBusActivity.class);
        intent.putExtra("UserID", currentUser);
        startActivity(intent);

    }

    public void openActivity44() {
        //Intent intent=new Intent(this,LoginActivity.class);
        Intent intent = new Intent(this, ViewBusActivity.class);
        intent.putExtra("UserID", currentUser);
        startActivity(intent);

    }

    public void openActivity_v44() {

        SharedPreferences prefs = this.getSharedPreferences("logInPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", "");     //RESET TO DEFAULT VALUE
        editor.putString("type", "");
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    public void openActivity_card_fare() {
        Intent intent = new Intent(this, viewBusToSetFare.class);
        intent.putExtra("UserID", currentUser);
        startActivity(intent);

    }

    public void openActivity_pending() {
        Intent intent = new Intent(this, Pending_Request_Owner.class);
        intent.putExtra("UserID", currentUser);
        startActivity(intent);

    }


}

