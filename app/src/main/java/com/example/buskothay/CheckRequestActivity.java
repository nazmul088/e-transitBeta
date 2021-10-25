/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class CheckRequestActivity  extends AppCompatActivity {

    private ListView listViewBuses;
    private DatabaseReference databaseBus;
    private List<Bus> busList;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_request_list);


        busList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();


    }

}
