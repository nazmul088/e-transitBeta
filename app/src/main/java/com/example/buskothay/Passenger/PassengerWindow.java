/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.buskothay.LoginActivity;
import com.example.buskothay.R;
import com.example.buskothay.TourismSide.TourismActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PassengerWindow extends AppCompatActivity {

    //private Button button;
    private ImageView profilePic;
    public Uri imageUrl;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private CardView button,cardView2;
    private TextView val;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppp);


        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


        val =(TextView)findViewById(R.id.textView50);


        //val.setText(0);
        mDatabase = FirebaseDatabase.getInstance().getReference("PassengerBalance");
        FirebaseUser crrntuSER = FirebaseAuth.getInstance().getCurrentUser();
        Query query1=mDatabase.orderByChild("passengerID").equalTo(crrntuSER.getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot11:snapshot.getChildren()){


                        PassengerBalance passengerBalance=snapshot11.getValue(PassengerBalance.class);
                        String am =String.valueOf( passengerBalance.getBalance());
                        val.setText(am);


                    }
                }
                else
                {
                    PassengerBalance passengerBalance = new PassengerBalance(currentFirebaseUser.getUid(),0);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        button=(CardView)findViewById(R.id.card3) ;
        cardView2=(CardView)findViewById(R.id.card3);
        //button = (Button) findViewById(R.id.button11);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity8();
            }
        });


        button=(CardView)findViewById(R.id.card3) ;
       // button = (Button) findViewById(R.id.button11);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity8();
            }
        });

        button=(CardView)findViewById(R.id.card2);

        //button=(Button) findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity7();
            }
        });
        button=(CardView)findViewById(R.id.card4) ;

        //button=(Button) findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityp7();
            }
        });
        button = (CardView) findViewById(R.id.card5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BarCodeScanner.class);
                startActivity(intent);
            }
        });


        button=(CardView)findViewById(R.id.balance_card) ;

        //button=(Button) findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_balance();
            }
        });

        button = (CardView) findViewById(R.id.card6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerWindow.this,TripHistoryActivity.class);
                startActivity(intent);
            }
        });

        button = (CardView) findViewById(R.id.card7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TourismActivity.class));
            }
        });

    }

    public void openActivity7() {
        //Intent intent=new Intent(this,LoginActivity.class);
        Intent intent=new Intent(this, showNearbyBusDistanceAndTime.class);
        startActivity(intent);

    }
    public void openActivityp7() {
        //Intent intent=new Intent(this,LoginActivity.class);

        SharedPreferences prefs = this.getSharedPreferences("logInPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", "");     //RESET TO DEFAULT VALUE
        editor.putString("type","");
        editor.commit();
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    public void openActivity8(){
        Intent intent = new Intent(this, TrackBus.class);
        startActivity(intent);
    }

    public void openActivity_balance(){
        Intent intent = new Intent(this, ActivityRecharge.class);
        startActivity(intent);
    }
}
