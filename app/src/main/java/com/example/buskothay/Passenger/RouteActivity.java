/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.LocationDetails;
import com.example.buskothay.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RouteActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String route,stoppage;
    private String inputBusName ;
    private String inputRouteNumber ;
    private ListView listView;
    private Button button;
    private TextView textView,textView2;

    ArrayList<LocationDetails> locationArrayList = new ArrayList<LocationDetails>();

    public String getInputBusName() {
        return inputBusName;
    }

    public void setInputBusName(String inputBusName) {
        this.inputBusName = inputBusName;
    }




    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);


        locationArrayList = getIntent().getParcelableArrayListExtra("allStoppage");

        Bundle bundle = getIntent().getExtras();
        inputBusName = bundle.getString("busName");
        inputRouteNumber=bundle.getString("route");


        //inputBusName = getIntent().getParcelableExtra("busName");

        button = (Button) findViewById(R.id.button6);
        textView = (TextView) findViewById(R.id.busName);
        textView2 = (TextView) findViewById(R.id.route_Id_Number);

        listView = (ListView) findViewById(R.id.fullRouteListView);

        textView.setText(inputBusName);
        textView2.setText(inputRouteNumber);


        ArrayList<String> locations = new ArrayList<String>();
        for(int i=0; i <locationArrayList.size();i++)
        {
            locations.add((i+1)+"."+locationArrayList.get(i).getPlaceName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,locations);
        listView.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(RouteActivity.this, AddRouteBetweenLocation.class);
                //intent.putExtra("allStoppage",locationArrayList);
               // intent.putParcelableArrayListExtra("allStoppage",locationArrayList);
               // startActivity(intent);
            }
        });










        databaseReference = FirebaseDatabase.getInstance().getReference("BusDetails");

                //final String inputBusName = editText.getText().toString();





    }









}