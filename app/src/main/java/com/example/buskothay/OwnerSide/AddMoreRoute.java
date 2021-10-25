/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.LocationDetails;
import com.example.buskothay.R;
import com.example.buskothay.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AddMoreRoute extends AppCompatActivity {
    private ListView listView;

    private ListView listView1;
    private DatabaseReference databaseReference;
    private ArrayList<String> locations = new ArrayList<>();
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_route);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });



        currentUser = getIntent().getExtras().getString("UserID");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Bundle bundle = getIntent().getExtras();
        String busName = bundle.getString("busName");
        String modelName = bundle.getString("modelName");
        String registrationNumber = bundle.getString("registrationNumber");
        String sittingCapacity = bundle.getString("sittingCapacity");
        String modelYear = bundle.getString("modelYear");
        String registrationYear = bundle.getString("registrationYear");
        String busType = bundle.getString("busType");
        String busFileName = bundle.getString("busFileName");



        //Grab all locations coordinates

       final HashMap<String, LocationDetails> locationDetailsHashMap = new HashMap<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");
        Query query = databaseReference.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        LocationDetails locationDetails = dataSnapshot.getValue(LocationDetails.class);
                        locationDetailsHashMap.put(locationDetails.getPlaceName(),locationDetails);
                        locations.add(locationDetails.getPlaceName());
                    }
                    progressDialog.dismiss();
                    ArrayList<String> SelectedStoppage = getIntent().getStringArrayListExtra("selectedStoppage");
                    String fromLocation = SelectedStoppage.get(0);
                    ArrayList<String> stoppages = getIntent().getStringArrayListExtra("stoppages");
                    listView = findViewById(R.id.listView2);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMoreRoute.this,
                            android.R.layout.simple_expandable_list_item_1, SelectedStoppage);
                    listView.setAdapter(adapter);

                    listView1 = (ListView) findViewById(R.id.listVIew3);

                    ArrayAdapter<String> adapter1 = new ArrayAdapter(AddMoreRoute.this, android.R.layout.simple_list_item_multiple_choice, locations);
                    listView1.setAdapter(adapter1);
                    listView1.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
                    listView1.setItemsCanFocus(false);



                    Button button = (Button) findViewById(R.id.button26);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int len = listView1.getCount();
                            SparseBooleanArray checked = listView1.getCheckedItemPositions();
                            for (int i = 0; i < len; i++)
                                if (checked.get(i)) {
                                    String item = locations.get(i);
                                    SelectedStoppage.add(item);
                                    /* do whatever you want with the checked item */
                                }


                            ArrayList<LocationDetails> locationDetailsArrayList1 = new ArrayList<>();
                            for(int i=0;i<SelectedStoppage.size();i++)
                            {
                                locationDetailsArrayList1.add(new LocationDetails(locationDetailsHashMap.get(SelectedStoppage.get(i)).getLatitude(),locationDetailsHashMap.get(SelectedStoppage.get(i)).getLongitude(),
                                        SelectedStoppage.get(i)));
                            }

                            Collections.sort(locationDetailsArrayList1,new SortPlaces(new LatLng(Double.parseDouble(locationDetailsArrayList1.get(0).getLatitude()),
                                    Double.parseDouble(locationDetailsArrayList1.get(0).getLongitude()) )));

                            ArrayList<String> locations2 = new ArrayList<>();
                             for(int i=0;i<locationDetailsArrayList1.size();i++)
                                locations2.add(locationDetailsArrayList1.get(i).getPlaceName());


                            final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            databaseReference = FirebaseDatabase.getInstance().getReference("User");
                            Query query = databaseReference.orderByChild("id").equalTo(currentUser);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                    {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                        {
                                            User user = dataSnapshot.getValue(User.class);

                                            RequestAddBus requestAddBus = new RequestAddBus(user,busName,modelName,registrationNumber,sittingCapacity,modelYear
                                                    ,registrationYear,busType,fromLocation,locations2.get(locations2.size()-1),locations2,busFileName);
                                            databaseReference = FirebaseDatabase.getInstance().getReference("RequestForAddBus");
                                            databaseReference.push().setValue(requestAddBus);
                                            Toast.makeText(getApplicationContext(),
                                                    "Request Completed",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            Intent intent = new Intent(getApplicationContext(),OwnerWindow.class);
                                            intent.putExtra("UserID",currentUser);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull  DatabaseError error) {

                                }
                            });
                        }
                    });



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







       /* intent.putExtra("fromLocation",fromLocation1);
        intent.putExtra("toLocation",toLocation1);
        intent.putStringArrayListExtra("Stoppages",locations);*/




    }


    class SortPlaces implements Comparator<LocationDetails> {
        LatLng currentLoc;

        public SortPlaces(LatLng current){
            currentLoc = current;
        }
        @Override
        public int compare(final LocationDetails place1, final LocationDetails place2) {
            double lat1 = Double.parseDouble(place1.getLatitude()) ;
            double lon1 = Double.parseDouble(place1.getLongitude()) ;
            double lat2 = Double.parseDouble(place2.getLatitude()) ;
            double lon2 = Double.parseDouble(place2.getLongitude()) ;

            double distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1);
            double distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2);
            return (int) (distanceToPlace1 - distanceToPlace2);
        }

        public double distance(double fromLat, double fromLon, double toLat, double toLon) {
            double radius = 6378137;   // approximate Earth radius, *in meters*
            double deltaLat = toLat - fromLat;
            double deltaLon = toLon - fromLon;
            double angle = 2 * Math.asin( Math.sqrt(
                    Math.pow(Math.sin(deltaLat/2), 2) +
                            Math.cos(fromLat) * Math.cos(toLat) *
                                    Math.pow(Math.sin(deltaLon/2), 2) ) );
            return radius * angle;
        }
    }
}