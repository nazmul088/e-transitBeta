/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.AuthoritySide;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.R;
import com.example.buskothay.Route;
import com.example.buskothay.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Show_Detail_Info_Authority extends AppCompatActivity {

    private String msg1;
    private TextView textView;
    private String str;
    private Button button;

    private DatabaseReference mDatabase ;
    private String busId,busNumber,from,ownerId,route_Id,to,travelsName,driver_Id;
    private String modelName,sittingCapacity,registrationNumber,modelYear,registrationYear,busType;
    private ArrayList<String> bus_Stoppage;

    private long mLastClickTime = 0; // to prevent double click

    private String busDetailsObjectKey ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_bus_updated);

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



        textView=(TextView)findViewById(R.id.auth_side1);
        Intent intent=getIntent();
        str =intent.getStringExtra("message1");
        travelsName=str;
        textView.setText(str);


        textView=(TextView)findViewById(R.id.auth_side2);
        Intent intent1=getIntent();
        str =intent1.getStringExtra("message2");
        busType=str;
        textView.setText(str);


        textView=(TextView)findViewById(R.id.auth_side3);
        Intent intent3=getIntent();
        str =intent3.getStringExtra("message3");
        from=str;
        textView.setText(str);

        textView=(TextView)findViewById(R.id.auth_side5);
        Intent intent4=getIntent();
        str =intent4.getStringExtra("message4");
        modelName=str;
        textView.setText(str);

        textView=(TextView)findViewById(R.id.auth_side6);
        Intent intent5=getIntent();
        str =intent5.getStringExtra("message5");
        modelYear=str;
        textView.setText(str);



        textView=(TextView)findViewById(R.id.auth_side8);
        Intent intent7=getIntent();
        str =intent7.getStringExtra("message7");
        registrationNumber=str;
        textView.setText(str);

        textView=(TextView)findViewById(R.id.auth_side9);
        Intent intent8=getIntent();
        str =intent8.getStringExtra("message8");
        registrationYear=str;
        textView.setText(str);

        textView=(TextView)findViewById(R.id.auth_side10);
        Intent intent9=getIntent();
        str =intent9.getStringExtra("message9");
        sittingCapacity=str;
        textView.setText(str);

        textView=(TextView)findViewById(R.id.auth_side4);
        Intent intent10=getIntent();
        str =intent10.getStringExtra("message10");
        to=str;
        textView.setText(str);




        Intent intent11=getIntent();
        bus_Stoppage=intent11.getStringArrayListExtra("message11");

        User user = (User) getIntent().getSerializableExtra("message14");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("BusDetails");

        textView=(TextView) findViewById(R.id.button15) ;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Show_Detail_Info_Authority.this, Check_Route_Stoppage.class);
                intent.putExtra("message__1",bus_Stoppage);

                startActivity(intent);



            }
        });

        textView = (TextView) findViewById(R.id.button20) ;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getApplicationContext(),ShowLicenseActivity.class);
                intent2.putExtra("BusLicense",intent1.getStringExtra("message13"));//message13 is file name
                startActivity(intent2);
            }
        });

        System.out.println("UID"+FirebaseAuth.getInstance().getCurrentUser().getUid());


        button=(Button) findViewById(R.id.button16) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For accepting request for adding Bus
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();



                String stoppages = bus_Stoppage.get(0);
                for(int i=1;i<bus_Stoppage.size();i++)
                {
                    stoppages = stoppages+ ","+bus_Stoppage.get(i);
                }
                mDatabase = FirebaseDatabase.getInstance().getReference("Route");
                Query query = mDatabase.orderByKey();
                String finalStoppages = stoppages;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String routeID = "";
                            boolean isFound = false;
                            String deletedKey = getIntent().getStringExtra("message16");
                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                Route route = dataSnapshot.getValue(Route.class);
                                if(route.getStoppage().equalsIgnoreCase(finalStoppages))
                                {
                                    routeID = route.getID();
                                    isFound = true;
                                    break;
                                }
                            }
                            if(!isFound)
                            {
                                Route route = new Route(bus_Stoppage.get(0),"",finalStoppages,bus_Stoppage.get(bus_Stoppage.size()-1));
                                routeID = mDatabase.push().getKey();
                                route.setID(routeID);

                                mDatabase.push().setValue(route);

                                BusUpdated busUpdated = new BusUpdated(bus_Stoppage.get(0),routeID,bus_Stoppage.get(bus_Stoppage.size()-1),travelsName,
                                        user,modelName,sittingCapacity,registrationNumber,modelYear,registrationYear,busType,getIntent().getStringExtra("message13"));
                                mDatabase = FirebaseDatabase.getInstance().getReference("BusDetails");
                                busDetailsObjectKey = mDatabase.push().getKey();
                                mDatabase.child(busDetailsObjectKey).setValue(busUpdated);
                                mDatabase = FirebaseDatabase.getInstance().getReference("RequestForAddBus");
                                mDatabase.child(deletedKey).removeValue();
                                Toast.makeText(Show_Detail_Info_Authority.this,"Bus Registered Successfully",Toast.LENGTH_LONG).show();
                                Intent intent2 = new Intent(getApplicationContext(),AuthorityActivity.class);
                                startActivity(intent2);

                            }
                            else
                            {

                                BusUpdated busUpdated = new BusUpdated(bus_Stoppage.get(0),routeID,bus_Stoppage.get(bus_Stoppage.size()-1),travelsName,
                                        user,modelName,sittingCapacity,registrationNumber,modelYear,registrationYear,busType,getIntent().getStringExtra("message13"));
                                mDatabase = FirebaseDatabase.getInstance().getReference("BusDetails");
                                busDetailsObjectKey = mDatabase.push().getKey();
                                mDatabase.child(busDetailsObjectKey).setValue(busUpdated);


                                mDatabase = FirebaseDatabase.getInstance().getReference("RequestForAddBus");
                                mDatabase.child(deletedKey).removeValue();
                                Toast.makeText(Show_Detail_Info_Authority.this,"Bus Registered Successfully",Toast.LENGTH_LONG).show();

                                Intent intent2 = new Intent(getApplicationContext(),AuthorityActivity.class);
                                startActivity(intent2);
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
/*
    */



}
