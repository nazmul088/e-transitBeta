/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.buskothay.BusDetails;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.Route;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindStoppageByBusName  {
    private DatabaseReference databaseReference;
    private String route,stoppage;
    ArrayList<LocationDetails> locationArrayList = new ArrayList<LocationDetails>();
    private Context context;
    private String fromLocation,toLocation;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void getAllStoppages(final String busname){



        databaseReference = FirebaseDatabase.getInstance().getReference("BusDetails");
        Query query = databaseReference.orderByChild("travelsName").equalTo(busname);
        query.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        BusDetails busDetails = dataSnapshot.getValue(BusDetails.class);
                        String route1 = busDetails.getRoute_Id();
                        route = route1;
                        break;
                    }



                    databaseReference = FirebaseDatabase.getInstance().getReference("Route");
                    final Query query = databaseReference.orderByChild("id").equalTo(route);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Route route1 = dataSnapshot.getValue(Route.class);
                                stoppage = route1.getStoppage();
                                fromLocation = route1.getFrom();
                                toLocation = route1.getTo();
                                break;
                            }
                            databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");
                            final String[] allStoppage = stoppage.split(",");
                            int len = allStoppage.length;
                            for (int i = 0; i < len; i++) {

                                Query query1 = databaseReference.orderByChild("placeName").equalTo(allStoppage[i]);
                                query1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            LocationDetails location = dataSnapshot.getValue(LocationDetails.class);
                                            LocationDetails location1 = new LocationDetails(location.getLatitude(),location.getLongitude(),location.getPlaceName());
                                            locationArrayList.add(location1);
                                        }
                                        if(locationArrayList.size() == allStoppage.length)
                                        {
                                            Intent intent = new Intent(context, RouteActivity.class);
                                            intent.putParcelableArrayListExtra("allStoppage",locationArrayList);
                                            intent.putExtra("busName",busname);
                                            intent.putExtra("route",fromLocation+"-"+toLocation);

                                            FindStoppageByBusName.this.context.startActivity(intent);
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
