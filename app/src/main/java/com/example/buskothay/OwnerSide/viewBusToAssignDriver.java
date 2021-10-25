/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.buskothay.AssignDriver;
import com.example.buskothay.AuthoritySide.Request_Bus_List;
import com.example.buskothay.BusDetails;
import com.example.buskothay.R;
import com.example.buskothay.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import java.util.Random;

public class viewBusToAssignDriver extends AppCompatActivity {

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    private ListView listViewBuses;
    private DatabaseReference databaseBus;
    private List<BusDetails> busList;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> databaseKey = new ArrayList<>();
    private Button button;
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assign Driver");
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

        currentUser = getIntent().getExtras().getString("UserID");

        listViewBuses = (ListView) findViewById(R.id.listViewBusDetails2);
        databaseBus = FirebaseDatabase.getInstance().getReference("BusDetails");
        Query query = databaseBus.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BusDetails busDetails = dataSnapshot.getValue(BusDetails.class);
                        if (busDetails.getOwner().getID().equalsIgnoreCase(currentUser)) {
                            busList.add(busDetails);
                            databaseKey.add(dataSnapshot.getKey());
                        }
                    }
                    if (busList.size() == 0)
                        Toast.makeText(viewBusToAssignDriver.this, "No Bus Found", Toast.LENGTH_LONG).show();

                    BusUpdatedAdapter adapter = new BusUpdatedAdapter(viewBusToAssignDriver.this, busList);
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
                final AlertDialog.Builder alert = new AlertDialog.Builder(viewBusToAssignDriver.this);
                View view1 = getLayoutInflater().inflate(R.layout.dialog_to_assign_driver, null);
                EditText editText = (EditText) view1.findViewById(R.id.editText1);
                EditText editText1 = (EditText) view1.findViewById(R.id.editText2);
                button = (Button) view1.findViewById(R.id.button24);
                alert.setView(view1);
                if (bus.getDriver() != null) {
                    editText.setText(bus.getDriver().getName());
                    editText1.setText(bus.getDriver().getPhoneNumber());
                }
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String DriverPhone = editText.getText().toString();
                        String DriverName = editText1.getText().toString();

                        authenticateDriver(DriverPhone, getRandomString(6), DriverName, databaseKey.get(i));

                        alertDialog.dismiss();


                    }
                });

                //showDriverDialog(bus.getBusId(),bus.getTravelsName(),bus.getBusNumber(),bus.getRoute_Id(),bus.getFrom(),bus.getTo(),bus.getBusCondition(),bus.getOwnerId());


            }
        });


    }

    private void showDriverDialog(String busId, String travelsName, String busNumber, String route_id, String from, String to, String busCondition, String ownerId) {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = new Intent(viewBusToAssignDriver.this, AssignDriver.class);
        intent.putExtra("owner_id", currentFirebaseUser.getUid());
        intent.putExtra("bus_id", busId);
        // intent.putExtra("type",(String) radioButton.getText());
        startActivity(intent);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void authenticateDriver(String phone, String password, String name, String key) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(phone + "@gmail.com", password)
                .addOnCompleteListener(viewBusToAssignDriver.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(viewBusToAssignDriver.this, "Create User with Phone", Toast.LENGTH_LONG).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(viewBusToAssignDriver.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), OwnerWindow.class);
                            intent.putExtra("UserID", currentUser);
                            startActivity(intent);
                        } else {
                            User user = new User(task.getResult().getUser().getUid(), phone + "@gmail.com", "driver");
                            DatabaseReference mDatabase;
                            mDatabase = FirebaseDatabase.getInstance().getReference("User");
                            mDatabase.push().setValue(user);
                            mDatabase = FirebaseDatabase.getInstance().getReference("BusDetails");
                            mDatabase.child(key).child("driverId").setValue(task.getResult().getUser().getUid());
                            DriverUpdated driverUpdated = new DriverUpdated(name, phone, password);
                            mDatabase.child(key).child("driver").setValue(driverUpdated);
                            Toast.makeText(viewBusToAssignDriver.this, "Driver Registered", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), OwnerWindow.class);
                            intent.putExtra("UserID", currentUser);
                            startActivity(intent);

                        }
                    }
                });


    }


    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


}
