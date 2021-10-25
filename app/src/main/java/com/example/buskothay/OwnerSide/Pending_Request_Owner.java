/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.AuthoritySide.BusUpdated;
import com.example.buskothay.AuthoritySide.Request_Bus_List;
import com.example.buskothay.BusDetails;
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

public class Pending_Request_Owner extends AppCompatActivity {


    private ListView listViewBuses;
    private DatabaseReference databaseBus;
    private List<RequestAddBus> busList;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_request_owner_xml);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("View Bus");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();


        busList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();


        listViewBuses = (ListView) findViewById(R.id.listViewBusDetails_owner);
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

                        String bus_name = requestAddBus.getBusName();
                        String bus_type = requestAddBus.getBusType();

                        String modelName = requestAddBus.getModelName();
                        String modelYear = requestAddBus.getModelYear();
                        String reg_no = requestAddBus.getRegistrationNumber();
                        String reg_Year = requestAddBus.getRegistrationYear();
                        String sit_capacity = requestAddBus.getSittingCapacity();
                        String from_location = requestAddBus.getFromLocation();
                        String toLocation = requestAddBus.getToLocation();
                        ArrayList<String> stoppages = requestAddBus.getStoppages();

                        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        //String busId,DriverUpdated driver,ArrayList<String>bus_Stoppage,String from, User owner, String to, String travelsName, String modelName, String sittingCapacity, String registrationNumber, String modelYear, String registrationYear, String busType

                        if (currentFirebaseUser.getUid().equalsIgnoreCase(requestAddBus.getOwner().getID())) {
                            busList.add(requestAddBus);
                        }
                    }
                    progressDialog.dismiss();
                    Request_Bus_List adapter = new Request_Bus_List(Pending_Request_Owner.this, busList);
                    listViewBuses.setAdapter(adapter);

                    listViewBuses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            RequestAddBus bus = busList.get(i);
                            //   String find_key=keysList.get(i);

                            // showUpdateDeleteDialog(bus.getBusId(),bus.getTravelsName(),bus.getBusNumber(),bus.getDriver_Id(),bus.getFrom(),bus.getTo(),bus.getBusCondition(),bus.getOwnerId());

                            showDetailInfo(bus.getBusName(), bus.getBusType(), bus.getFromLocation(), bus.getModelName(), bus.getModelYear(), bus.getOwner(), bus.getRegistrationNumber(), bus.getRegistrationYear(), bus.getSittingCapacity(), bus.getToLocation());
                        }
                    });


                }
                else{
                    Toast.makeText(Pending_Request_Owner.this, "No Request Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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

    private void showDetailInfo(String BusName, String busType, String fromLocation, String modelName, String modelYear, User user, String Reg_no, String Reg_Year, String Sit_cap, String to_Location) {


        Intent intent = new Intent(this, Show_Detail_to_Owner.class);
        intent.putExtra("messagep1", BusName);
        intent.putExtra("messagep2", busType);
        intent.putExtra("messagep3", fromLocation);
        intent.putExtra("messagep4", modelName);
        intent.putExtra("messagep5", modelYear);
        intent.putExtra("messagep6", user.getID());
        intent.putExtra("messagep7", Reg_no);
        intent.putExtra("messagep8", Reg_Year);
        intent.putExtra("messagep9", Sit_cap);
        intent.putExtra("messagep10", to_Location);
        intent.putExtra("messagep12","Not Provided");
        intent.putExtra("messagep13","Not Provided");
        startActivity(intent);


    }

}
