/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.DriverSide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.buskothay.LocationDetails;
import com.example.buskothay.LoginActivity;
import com.example.buskothay.R;

import java.util.Comparator;


public class DriverWindow extends AppCompatActivity {
    //private Button button;
    private CardView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_window_activity);
        button = (CardView) findViewById(R.id.card111);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity5();
            }
        });


        button = (CardView) findViewById(R.id.card555);
        // button=(Button) findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity6();
            }
        });
    }

    public void openActivity5() {
        // Intent intent=new Intent(this,LoginActivity.class);
        Intent intent = new Intent(this, DriverMapActivityUpdated.class);
        startActivity(intent);

    }

    public void openActivity6() {
        SharedPreferences prefs = this.getSharedPreferences("logInPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", "");     //RESET TO DEFAULT VALUE
        editor.putString("type", "");
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
