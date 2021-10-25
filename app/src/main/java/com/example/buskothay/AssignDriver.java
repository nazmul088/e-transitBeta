/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.OwnerSide.OwnerWindow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AssignDriver extends AppCompatActivity {
    private ListView listViewBuses;
    private DatabaseReference databaseBus;
    private List<Driver> driverList;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_driver);
        driverList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Intent intent=getIntent();
        final String owner_id =intent.getStringExtra("owner_id");
        final String bus_id =intent.getStringExtra("bus_id");
       // final String type =intent.getStringExtra("type");

        listViewBuses = (ListView) findViewById(R.id.busListItem2);
        databaseBus = FirebaseDatabase.getInstance().getReference();
//         = FirebaseDatabase.getInstance().getReference("BusDetails");


        FirebaseDatabase.getInstance().getReference("Driver")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {

                        //driverList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //Bus bus = snapshot.getValue(Bus.class);
                                Driver driver=snapshot.getValue(Driver.class);
                                final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                               // if(currentFirebaseUser.getUid().equalsIgnoreCase(bus.ownerId)){

                                    if (driver.isAssigned()==false){
                                        driverList.add(driver);
                                    }

                                //}

                            }
                            DriverList adapter = new DriverList(AssignDriver.this, driverList);
                            listViewBuses.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                });

        listViewBuses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Bus bus =busList.get(i);
                final Driver driver= driverList.get(i);
                showDriverDialog(driver.getID(),driver.getName(),bus_id);
                driver.setAssigned(true);
                driver.setOwnerId(owner_id);
                databaseReference = FirebaseDatabase.getInstance().getReference("Driver");
                Query query = databaseReference.orderByChild("id").equalTo(driver.getID());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for (DataSnapshot snapshot1:snapshot.getChildren()) {

                               // User user = snapshot1.getValue(User.class);
                                final Driver driver1=snapshot1.getValue(Driver.class);

                                String clubKey= snapshot1.getKey();
                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Driver").child(clubKey).child("ownerId").setValue(owner_id);
                                databaseReference.child("Driver").child(clubKey).child("assigned").setValue(true);
                                databaseReference.child("Driver").child(clubKey).child("busId").setValue(bus_id);


                              //  ownerUpdate(owner_id,bus_id,driver1.getID());









                                //         final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                           //     DatabaseReference drTravellingPath = FirebaseDatabase.getInstance().getReference("Owner").child(currentFirebaseUser.getUid());

                               // drTravellingPath.removeValue();

                                Intent intent=new Intent(AssignDriver.this, OwnerWindow.class);
                                startActivity(intent);



                              /*  if(user.getType().equalsIgnoreCase("Passenger")){
                                    System.out.println("Asche ekhane");
                                    Intent intent = new Intent(LoginActivity.this,PassengerWindow.class);
                                    startActivity(intent);


                                }*/
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
              //  Intent intent=new Intent(AssignDriver.this,LoginActivity.class);
               // startActivity(intent);


            }
        });


    }

/*    private void ownerUpdate(final  String owner_id,final String bus_id,final String driverId) {

        databaseReference =FirebaseDatabase.getInstance().getReference("User");
        Query query11 = databaseReference.orderByChild("id").equalTo(owner_id);
        query11.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot snapshot2:snapshot.getChildren()) {

                        String clubKey2= snapshot2.getKey();
                        Owner owner1 =snapshot2.getValue(Owner.class);
                        List<String> busList =owner1.getBusList();
                        busList.add(bus_id);
                        List<String> driverList =owner1.getDriverList();
                        driverList.add(driverId);

                        //System.out.println(busList);



                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("User").child(clubKey2).child("busList").setValue(busList);
                        databaseReference.child("User").child(clubKey2).child("driver_List").setValue(driverList);






                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

    private void showDriverDialog(final String driverId, final String driverName, final String busId) {

        DatabaseReference  databaseReference1 = FirebaseDatabase.getInstance().getReference("BusDetails");
        Query query = databaseReference1.orderByChild("busId").equalTo(busId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        BusDetails busDetails = dataSnapshot.getValue(BusDetails.class);
                        //busDetails.setDriver_Id(driverId);
                        databaseReference1.child(dataSnapshot.getKey()).setValue(busDetails);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();

    }


}