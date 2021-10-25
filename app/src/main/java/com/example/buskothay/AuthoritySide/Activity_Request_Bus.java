/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.AuthoritySide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.BusDetails;
import com.example.buskothay.OwnerSide.RequestAddBus;
import com.example.buskothay.R;
import com.example.buskothay.User;
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

public class Activity_Request_Bus extends AppCompatActivity {

    private ListView listViewBuses;
    private DatabaseReference databaseReference;
    private List<BusUpdated> busList;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> keysList;
    private ArrayList<String> DriverFileName = new ArrayList<>();
    private ArrayList<String> BusFileName = new ArrayList<>();
    private ArrayList<RequestAddBus> requestAddBusArrayList = new ArrayList<>();
    private ArrayList<String> databasesKey = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.req_list_file);

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


        busList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();


        listViewBuses = (ListView) findViewById(R.id.listViewBusDetails);
        databaseReference = FirebaseDatabase.getInstance().getReference("RequestForAddBus");
        Query query = databaseReference.orderByKey();
//         = FirebaseDatabase.getInstance().getReference("BusDetails");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                busList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RequestAddBus requestAddBus = snapshot.getValue(RequestAddBus.class);
                        databasesKey.add(snapshot.getKey());
                        String bus_name = requestAddBus.getBusName();
                        String bus_type = requestAddBus.getBusType();

                        String modelName = requestAddBus.getModelName();
                        String modelYear = requestAddBus.getModelYear();
                        User user = requestAddBus.getOwner();
                        //String ownerId =requestAddBus.getOwnerId();
                        String reg_no = requestAddBus.getRegistrationNumber();
                        String reg_Year = requestAddBus.getRegistrationYear();
                        String sit_capacity = requestAddBus.getSittingCapacity();
                        String from_location = requestAddBus.getFromLocation();
                        String toLocation = requestAddBus.getToLocation();
                        ArrayList<String> stoppages = requestAddBus.getStoppages();
                        //DriverFileName.add(requestAddBus.getDriverFileName());
                        BusFileName.add(requestAddBus.getBusFileName());
                        requestAddBusArrayList.add(requestAddBus);


                        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    }
                    Request_Bus_List adapter = new Request_Bus_List(Activity_Request_Bus.this, requestAddBusArrayList);
                    listViewBuses.setAdapter(adapter);

                    listViewBuses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            RequestAddBus bus = requestAddBusArrayList.get(i);

                            showDetailInfo(bus.getBusName(), bus.getBusType(), bus.getFromLocation(), bus.getModelName(), bus.getModelYear(), bus.getRegistrationNumber(), bus.getRegistrationYear(), bus.getSittingCapacity(), bus.getToLocation(), bus.getStoppages(), BusFileName.get(i), requestAddBusArrayList.get(i), databasesKey.get(i));
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void showDetailInfo(String BusName, String busType, String fromLocation, String modelName, String modelYear, String Reg_no, String Reg_Year, String Sit_cap, String to_Location, ArrayList<String> stoppages, String busFileName, RequestAddBus requestAddBus, String key) {


        Intent intent = new Intent(this, Show_Detail_Info_Authority.class);
        intent.putExtra("message1", BusName);
        intent.putExtra("message2", busType);
        intent.putExtra("message3", fromLocation);
        intent.putExtra("message4", modelName);
        intent.putExtra("message5", modelYear);
        intent.putExtra("message7", Reg_no);
        intent.putExtra("message8", Reg_Year);
        intent.putExtra("message9", Sit_cap);
        intent.putExtra("message10", to_Location);
        intent.putExtra("message11", stoppages);
        intent.putExtra("message13", busFileName);
        intent.putExtra("message14", requestAddBus.getOwner());
        intent.putExtra("message16", key);


        startActivity(intent);
    }
}
