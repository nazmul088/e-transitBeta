/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buskothay.Adapter;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.R;
import com.example.buskothay.Route;
import com.example.buskothay.StoppageList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activitySetFare extends AppCompatActivity {
    private static ArrayList<StoppageList> data;
    //ItemDatamodel itemDatamodel;
    StoppageList stoppageList;
    private DatabaseReference databaseReference;
    private String route,stoppage;
    ArrayList<LocationDetails> locationArrayList = new ArrayList<LocationDetails>();
    private Context context;
    private String fromLocation,toLocation;
    ArrayList<String> locations = new ArrayList<String>();
    ArrayList<String> ExpAmtArray_val = new ArrayList<String>();
    private String currentUser;


    // list of Expensesname



    String[] Expensesname;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private String str;
    private Button button;
    private String owner_id,route_id,bus_reg_iD;
    private DatabaseReference mDatabase;

    public activitySetFare() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_fare);
        currentUser = getIntent().getExtras().getString("UserID");



        LocalBroadcastManager .getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("VALUES"));
        databaseReference = FirebaseDatabase.getInstance().getReference("Route");


        Intent intent = getIntent();
        str = intent.getStringExtra("message_Routeid");
        route_id = str;
        System.out.println(route_id);

        final Query query = databaseReference.orderByChild("id").equalTo(route_id);
        Intent intent1 = getIntent();
        str = intent1.getStringExtra("message_ownerId");
        owner_id = str;
        Intent intent2 = getIntent();
        str = intent2.getStringExtra("messagep7");
        bus_reg_iD= str;

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
                for(int i=0;i<allStoppage.length;i++){
                    locations.add(allStoppage[i]);
                }
                recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_expenses);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(activitySetFare.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                data = new ArrayList<>();
                for(int i =0;i<locations.size();i++){
                    stoppageList = new StoppageList(i,locations.get(i));
                    // System.out.println(locations.get(i+1));
                    data.add(stoppageList);
                }
                // call Adapter class by passing ArrayList data
                adapter = new Adapter(data);
                // set adapter to recyclerview
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        button=(Button)findViewById(R.id.submitexpenses);
        //button=(Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFare(route_id,owner_id,bus_reg_iD,data);
            } //See pending Request
        });








    }

    public void saveFare(String route_id,String owner_id,String bus_reg_iD,ArrayList<StoppageList> data){



        FarePay temp=new FarePay();
        temp.setBus_reg_Id(bus_reg_iD);
        temp.setOwnerId(owner_id);
        temp.setRouteId(route_id);
        //temp.setRouteId(route_id);
        //temp.setStoppageLists(data);
        ArrayList<StoppageList> stpp = new ArrayList<>();
        for(int i=0;i<data.size();i++){

            int val=Integer.parseInt(ExpAmtArray_val.get(i));
            StoppageList spList=new StoppageList(data.get(i).getExpenseId(),data.get(i).getExpenseName(),val);
            stpp.add(i,spList);
        }
        temp.setStoppageLists(stpp);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("FareList");
        Query query = mDatabase.orderByChild("bus_reg_Id").equalTo(temp.getBus_reg_Id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        FarePay farePay = dataSnapshot.getValue(FarePay.class);
                        farePay.setStoppageLists(temp.getStoppageLists());
                        mDatabase.child(dataSnapshot.getKey()).setValue(farePay);
                        break;
                    }
                }
                else
                {
                    mDatabase.push().setValue(temp);


                }
                Intent intent = new Intent(activitySetFare.this,OwnerWindow.class);
                intent.putExtra("UserID",currentUser);
                startActivity(intent);
                Toast.makeText(activitySetFare.this, "Fare Updated Successfully", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }

    public  BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ExpAmtArray_val= intent.getStringArrayListExtra("hi");
        }
    };
}

