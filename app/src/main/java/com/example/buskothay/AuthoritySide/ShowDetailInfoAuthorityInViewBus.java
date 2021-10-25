/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.AuthoritySide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buskothay.R;
import com.example.buskothay.Route;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowDetailInfoAuthorityInViewBus extends AppCompatActivity {

    private TextView textView;
    private String busId,busNumber,from,ownerId,route_Id,to,travelsName,driver_Id;
    private String modelName,sittingCapacity,registrationNumber,modelYear,registrationYear,busType;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_info_authority_in_view_bus);

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

        String str;

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

        textView=(TextView) findViewById(R.id.button15) ;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String routeId = intent9.getStringExtra("message11");
                databaseReference = FirebaseDatabase.getInstance().getReference("Route");
                Query query =  databaseReference.orderByChild("id").equalTo(routeId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot dataSnapshot:snapshot.getChildren())
                            {
                                Route route = dataSnapshot.getValue(Route.class);
                                String[] stoppages = route.getStoppage().split(",");
                                ArrayList<String> stoppagesArrayList = new ArrayList<>();
                                for(int i=0;i<stoppages.length;i++)
                                {
                                    stoppagesArrayList.add(stoppages[i]);
                                }
                                Intent intent=new Intent(getApplicationContext(), Check_Route_Stoppage.class);

                                intent.putExtra("message__1",stoppagesArrayList);

                                startActivity(intent);


                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        textView = (TextView) findViewById(R.id.button20) ;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getApplicationContext(),ShowLicenseActivity.class);
                if(intent1.getStringExtra("message13")==null)
                {
                    Toast.makeText(getApplicationContext(), "No Document Available", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent2.putExtra("BusLicense",intent1.getStringExtra("message13"));//message13 is file name
                startActivity(intent2);
            }
        });


    }
}