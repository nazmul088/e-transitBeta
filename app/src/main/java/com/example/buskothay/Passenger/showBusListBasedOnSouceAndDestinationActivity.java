/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.BusDetails;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.R;
import com.example.buskothay.Route;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showBusListBasedOnSouceAndDestinationActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;

    Button button;
    private ListView listView;
    int iteration = 0;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bus_list_based_on_souce_and_destination);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        final ArrayList<String> allLocations = new ArrayList<String>();


        databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");

        listView = (ListView) findViewById(R.id.busListItem);


        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.SelectedSource);


        final AutoCompleteTextView textView2 = (AutoCompleteTextView)
                findViewById(R.id.SelectedDestination);


        final ArrayList<String> busList = new ArrayList<String>();


        //List all the available Location
        final Query query = databaseReference.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        LocationDetails location = dataSnapshot.getValue(LocationDetails.class);
                        allLocations.add(location.getPlaceName());
                    }
                    progressDialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, allLocations);

        textView.setAdapter(adapter);
        textView2.setAdapter(adapter);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busList.clear();
                progressDialog.show();
                final String sourceLocation = textView.getText().toString();
                final String destinationLocation = textView2.getText().toString();

                if (getCurrentFocus() != null && getCurrentFocus() instanceof AutoCompleteTextView) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("Route");
                final ArrayList<String> routeList = new ArrayList<String>();
                Query query1 = databaseReference.orderByKey();
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                int count = 0;
                                Route route = dataSnapshot.getValue(Route.class);
                                String[] stoppage = route.getStoppage().split(",");
                                for (int i = 0; i < stoppage.length; i++) {
                                    if (sourceLocation.equalsIgnoreCase(stoppage[i]) || destinationLocation.equalsIgnoreCase(stoppage[i])) {
                                        count++;
                                    }
                                    if (count == 2) {
                                        if (!routeList.contains(route.getID())) {
                                            routeList.add(route.getID());
                                        }

                                    }
                                }

                            }
                        } else {
                            Toast.makeText(showBusListBasedOnSouceAndDestinationActivity.this, "No Bus Found", Toast.LENGTH_LONG).show();
                        }

                        for (int i = 0; i < routeList.size(); i++) {
                            iteration = i;
                            String route = routeList.get(i);
                            databaseReference = FirebaseDatabase.getInstance().getReference("BusDetails");
                            Query query11 = databaseReference.orderByChild("route_Id").equalTo(route);
                            query11.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        BusDetails busDetails = dataSnapshot.getValue(BusDetails.class);
                                        String busName = busDetails.getTravelsName();


                                        if (!busList.contains(busName))
                                            busList.add(busName);
                                    }
                                    if (iteration + 1 == routeList.size()) {

                                        ArrayAdapter arrayAdapter = new ArrayAdapter(showBusListBasedOnSouceAndDestinationActivity.this, android.R.layout.simple_list_item_1, busList);
                                        listView.setAdapter(arrayAdapter);
                                        progressDialog.dismiss();

                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                                                String selectedItemPosition = (String) adapter.getItemAtPosition(position);
                                                FindStoppageByBusName findStoppageByBusName = new FindStoppageByBusName();
                                                findStoppageByBusName.setContext(showBusListBasedOnSouceAndDestinationActivity.this);
                                                findStoppageByBusName.getAllStoppages(selectedItemPosition);
                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }
}