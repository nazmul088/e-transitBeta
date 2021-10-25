/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.AuthoritySide.Request_Bus_List;
import com.example.buskothay.Bus;
import com.example.buskothay.BusDetails;
import com.example.buskothay.Driver;
import com.example.buskothay.R;
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

public class ViewBusActivity extends AppCompatActivity {
    private ListView listViewBuses;
    private DatabaseReference databaseReference;
    private List<BusDetails> busList;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus);


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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        busList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser =  FirebaseAuth.getInstance().getCurrentUser().getUid();

        listViewBuses = (ListView) findViewById(R.id.listViewBusDetails2);
        databaseReference = FirebaseDatabase.getInstance().getReference("BusDetails");
        currentUser = getIntent().getExtras().getString("UserID");
        Query query = databaseReference.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BusDetails busDetails = dataSnapshot.getValue(BusDetails.class);
                        if (busDetails.getOwner().getID().equalsIgnoreCase(currentUser))
                            busList.add(busDetails);
                    }
                    if (busList.size() == 0)
                        Toast.makeText(ViewBusActivity.this, "No Bus Found", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    BusUpdatedAdapter adapter = new BusUpdatedAdapter(ViewBusActivity.this, busList);
                    listViewBuses.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        listViewBuses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BusDetails bus = busList.get(i);
                if (bus.getDriver() != null && bus.getDriverId() != null)
                    showDetailInfo(bus.getTravelsName(), bus.getBusType(), bus.getFrom(), bus.getModelName(), bus.getModelYear(), bus.getOwner().getID(), bus.getRegistrationNumber(), bus.getRegistrationYear(), bus.getSittingCapacity(), bus.getTo(), bus.getBus_Stoppage(), bus.getDriver().getPhoneNumber(), bus.getDriver().getPassword());
                else
                    showDetailInfo(bus.getTravelsName(), bus.getBusType(), bus.getFrom(), bus.getModelName(), bus.getModelYear(), bus.getOwner().getID(), bus.getRegistrationNumber(), bus.getRegistrationYear(), bus.getSittingCapacity(), bus.getTo(), bus.getBus_Stoppage(), "Null Driver", "Null Password");
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void showDetailInfo(String BusName, String busType, String fromLocation, String modelName, String modelYear, String ownerId, String Reg_no, String Reg_Year, String Sit_cap, String to_Location, ArrayList<String> stoppages, String DriverMobile, String driverPassword) {


        Intent intent = new Intent(this, Show_Detail_to_Owner.class);
        intent.putExtra("messagep1", BusName);
        intent.putExtra("messagep2", busType);
        intent.putExtra("messagep3", fromLocation);
        intent.putExtra("messagep4", modelName);
        intent.putExtra("messagep5", modelYear);
        intent.putExtra("messagep6", ownerId);
        intent.putExtra("messagep7", Reg_no);
        intent.putExtra("messagep8", Reg_Year);
        intent.putExtra("messagep9", Sit_cap);
        intent.putExtra("messagep10", to_Location);
        intent.putExtra("messagep11", stoppages);
        if(!DriverMobile.equalsIgnoreCase("Null Driver") && !driverPassword.equalsIgnoreCase("Null Password"))
        {
            intent.putExtra("messagep12", DriverMobile);
            intent.putExtra("messagep13", driverPassword);
        }
        else
        {
            intent.putExtra("messagep12", "Not Available");
            intent.putExtra("messagep13", "Not Available");
        }

        startActivity(intent);
    }

    // update ticket fees details
    private void showUpdateDeleteDialog(final String busId, String travelsName, final String busNumber, final String DriverID, final String from, final String to, final String busCondition, final String OwnerId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTravelsName = (EditText) dialogView.findViewById(R.id.editTexttravelsName);
        final EditText editBusNumber = (EditText) dialogView.findViewById(R.id.editTextbusNumber);
        // final EditText editroue_id      =(EditText)dialogView.findViewById(R.id.editrouteId);


        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDelete);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bus_1 = editTravelsName.getText().toString().trim();
                String bus_2 = editBusNumber.getText().toString().trim();
                // String bus_3 = editroue_id.getText().toString().trim();
                //String bus_4 = editFromBus.getText().toString();
                // String bus_5 = editToBus.getText().toString();
                //   String bus_6 = editConditionBus.getText().toString().trim();


                updateBusDetail(busId, bus_1, bus_2, from, to, busCondition);
                //alertDialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBus(busId, OwnerId, DriverID);
            }
        });


    }

    private void deleteBus(String busId, String ownerId, String driverID) {


        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser.getUid().equalsIgnoreCase(ownerId)) {
            DatabaseReference drTravellingPath = FirebaseDatabase.getInstance().getReference("BusDetails").child(busId);


            databaseReference = FirebaseDatabase.getInstance().getReference("Driver");
            //  databaseReference = FirebaseDatabase.getInstance().getReference("Driver");
            Query query = databaseReference.orderByChild("id").equalTo(driverID);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                            // User user = snapshot1.getValue(User.class);
                            final Driver driver1 = snapshot1.getValue(Driver.class);

                            String clubKey = snapshot1.getKey();
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Driver").child(clubKey).child("ownerId").setValue("null");
                            databaseReference.child("Driver").child(clubKey).child("assigned").setValue(false);
                            databaseReference.child("Driver").child(clubKey).child("busId").setValue("null");


                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            drTravellingPath.removeValue();
            Toast.makeText(this, " Bus Detail Deleted Successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, " Bus Detail can't be deleted ", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(getApplicationContext(), ViewBusActivity.class);

        startActivity(intent);


    }

    private void updateBusDetail(String busId, String travelsNameI, String busNumberI, String from, String to, String busCondition) {
        //  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusDetails").child(busId);
        // databaseReference.removeValue();
        // Bus bus = new Bus(busId, travelsNameI, busNumberI, from, to, busCondition);
        // databaseReference.setValue(bus);
        // databaseBus.setValue(bus);
        //  databaseReference.setValue(bus);

        DatabaseReference drTravellingPath = FirebaseDatabase.getInstance().getReference("BusDetails").child(busId);


        Bus bus = new Bus(busId, travelsNameI, busNumberI, from, to, busCondition);
        //bus.ownerId=ownerId;


        drTravellingPath.removeValue();
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        bus.setOwnerId(currentFirebaseUser.getUid());


        drTravellingPath.setValue(bus);

        Intent intent = new Intent(getApplicationContext(), ViewBusActivity.class);

        startActivity(intent);

        Toast.makeText(this, " Bus Detail Updated Successfully", Toast.LENGTH_LONG).show();


    }
}