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
import com.example.buskothay.OwnerSide.BusUpdatedAdapter;
import com.example.buskothay.OwnerSide.RequestAddBus;
import com.example.buskothay.OwnerSide.Show_Detail_to_Owner;
import com.example.buskothay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewApprovedBus extends AppCompatActivity {

    private ListView listViewBuses;
    private DatabaseReference databaseBus;
    private List<BusUpdated> busList;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus);


        busList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();

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


        listViewBuses = (ListView) findViewById(R.id.listViewBusDetails2);
        databaseBus = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference("BusDetails")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        busList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                BusUpdated bus = snapshot.getValue(BusUpdated.class);

                                busList.add(bus);


                            }


                            Approved_Bus_List_Adapter adapter = new Approved_Bus_List_Adapter(viewApprovedBus.this,busList);
                            listViewBuses.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        listViewBuses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BusUpdated bus = busList.get(i);
                //showUpdateDeleteDialog(bus.getBusId(),bus.getTravelsName(),bus.getBusNumber(),bus.getDriver_Id(),bus.getFrom(),bus.getTo(),bus.getBusCondition(),bus.getOwnerId());
                showDetailInfo(bus.getTravelsName(), bus.getBusType(), bus.getFrom(), bus.getModelName(), bus.getModelYear(), bus.getRegistrationNumber(), bus.getRegistrationYear(), bus.getSittingCapacity(), bus.getTo(),bus.getRoute_Id(), bus.getFileName(), bus);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void showDetailInfo(String BusName, String busType, String fromLocation, String modelName, String modelYear, String Reg_no, String Reg_Year, String Sit_cap, String to_Location, String routeId, String busFileName, BusUpdated requestAddBus) {


        Intent intent = new Intent(this, ShowDetailInfoAuthorityInViewBus.class);
        intent.putExtra("message1", BusName);
        intent.putExtra("message2", busType);
        intent.putExtra("message3", fromLocation);
        intent.putExtra("message4", modelName);
        intent.putExtra("message5", modelYear);
        intent.putExtra("message7", Reg_no);
        intent.putExtra("message8", Reg_Year);
        intent.putExtra("message9", Sit_cap);
        intent.putExtra("message10", to_Location);
        intent.putExtra("message11", routeId);
        intent.putExtra("message13", busFileName);
        intent.putExtra("message14", requestAddBus.getOwner());

        startActivity(intent);
    }
}
