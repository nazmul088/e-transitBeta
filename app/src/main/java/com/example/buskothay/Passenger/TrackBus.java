/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.buskothay.Driver;
import com.example.buskothay.DriverSide.DriverTrip;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrackBus extends AppCompatActivity implements TrackBusAdapter.OnNoteListener{

    private ProgressDialog progressDialog;
    private Button button;
    private DatabaseReference databaseReference;
    private ArrayList<DriverTrip> driverTrips;
    private RecyclerView recyclerView;
    TrackBusAdapter trackBusAdapter;
    LinearLayoutManager layoutManager;
    private ArrayList<TrackBusclass> trackBuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_bus);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        driverTrips = new ArrayList<>();

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout2);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCurrentFocus()!=null && getCurrentFocus() instanceof AutoCompleteTextView)
                {
                    InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        button = (Button) findViewById(R.id.button12);
        final ArrayList<String> allBuses = new ArrayList<String>();
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView3);

        databaseReference = FirebaseDatabase.getInstance().getReference("DriverTrip");
        //List all the available Location
        final Query query = databaseReference.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DriverTrip driverTrip = dataSnapshot.getValue(DriverTrip.class); //get all the locaations from database
                        driverTrips.add(driverTrip);
                        if(!allBuses.contains(driverTrip.getTravelsName()))
                            allBuses.add(driverTrip.getTravelsName());

                    }
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TrackBus.this);
                    builder.setMessage("No Bus Available")
                            .setTitle("");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(),PassengerWindow.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TrackBus.this,
                android.R.layout.simple_dropdown_item_1line, allBuses);
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setCursorVisible(false);
                if(getCurrentFocus()!=null && getCurrentFocus() instanceof AutoCompleteTextView)
                {
                    InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setCursorVisible(true);
            }
        });


        button = (Button) findViewById(R.id.button14);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trackBuses = new ArrayList<>();


                recyclerView = findViewById(R.id.RecyclerView3);
                layoutManager = new LinearLayoutManager(TrackBus.this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                trackBusAdapter = new TrackBusAdapter(trackBuses,TrackBus.this);
                recyclerView.setAdapter(trackBusAdapter);
                trackBusAdapter.notifyDataSetChanged();


                progressDialog.show();

                //Hide Keyboard
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);


                 String selectedBus = textView.getText().toString();



                 for(int i=0;i<driverTrips.size();i++)
                 {
                     if(driverTrips.get(i).getTravelsName().equalsIgnoreCase(selectedBus))
                     {
                         TrackBusclass trackBusclass = new TrackBusclass(driverTrips.get(i).getTravelsName(),driverTrips.get(i).getRegistration_Number(),driverTrips.get(i).getBusType(),driverTrips.get(i).getFrom(),driverTrips.get(i).getTo(),driverTrips.get(i).getRoute_id());
                         trackBuses.add(trackBusclass);
                     }
                 }
                trackBusAdapter = new TrackBusAdapter(trackBuses,TrackBus.this);
                recyclerView.setAdapter(trackBusAdapter);
                trackBusAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        });




    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(TrackBus.this,LiveTrackBusActivity.class);
        intent.putExtra("BusName",trackBuses.get(position).getBusName());
        intent.putExtra("BusId",trackBuses.get(position).getBusRegistrationNumber());
        intent.putExtra("RouteId",trackBuses.get(position).getRouteId());
        startActivity(intent);
    }


    class TrackBusclass{
        String busName;
        String busRegistrationNumber;
        String type;
        String from;
        String to;
        String routeId;


        public TrackBusclass(String busName, String busRegistrationNumber, String type, String from, String to,String routeId) {
            this.busName = busName;
            this.busRegistrationNumber = busRegistrationNumber;
            this.type = type;
            this.from = from;
            this.to = to;
            this.routeId = routeId;
        }

        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public String getBusName() {
            return busName;
        }

        public void setBusName(String busName) {
            this.busName = busName;
        }

        public String getBusRegistrationNumber() {
            return busRegistrationNumber;
        }

        public void setBusRegistrationNumber(String busRegistrationNumber) {
            this.busRegistrationNumber = busRegistrationNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}