/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.R;

public class FairSuccessfulActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fair_successful);

        FairPayment fairPayment = (FairPayment)  getIntent().getSerializableExtra("FairPayment");

        int Balance =  getIntent().getIntExtra("Balance",0);

        textView = (TextView) findViewById(R.id.bus_name);
        textView.setText(fairPayment.getBusName());


        textView = (TextView) findViewById(R.id.textView61);
        textView.setText(fairPayment.getFrom());

        textView = (TextView) findViewById(R.id.textView62);
        textView.setText(fairPayment.getTo());

        textView = (TextView) findViewById(R.id.amount);
        textView.setText(String.valueOf(fairPayment.getAmount()));

        textView = (TextView) findViewById(R.id.textView64);
        textView.setText(String.valueOf(Balance));

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FairSuccessfulActivity.this,PassengerWindow.class);
                startActivity(intent);
            }
        });
    }
}