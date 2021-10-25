/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ActivityRecharge extends AppCompatActivity {

    private EditText inputRecharge;
    private Button btnRecharge;
    private FirebaseAuth firebaseAuth;
    private String passenger_ID;

    private DatabaseReference mDatabase;
    int amount_recharged=0;
    private TextView val;
    private String val2;
    int flag=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        passenger_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //Get firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        btnRecharge = (Button) findViewById(R.id.rechargeButton);
        inputRecharge = (EditText) findViewById(R.id.editTextRechargeAmount);
        //textView23
        val =(TextView)findViewById(R.id.textView23);


        //val.setText(0);
        mDatabase = FirebaseDatabase.getInstance().getReference("PassengerBalance");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println("!!!!!!!!!!!!!!!!!!"+passenger_ID);
        Query query1=mDatabase.orderByChild("passengerID").equalTo(currentFirebaseUser.getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot1) {
                if(snapshot1.exists()){
                    for (DataSnapshot snapshot11:snapshot1.getChildren()){


                        PassengerBalance passengerBalance=snapshot11.getValue(PassengerBalance.class);
                        String am =String.valueOf( passengerBalance.getBalance());
                        val.setText(am);


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });






        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ProgressDialog progressDialog = new ProgressDialog(ActivityRecharge.this);
                progressDialog.setMessage("Recharging...");
                progressDialog.show();
                String rech_amount = inputRecharge.getText().toString();

                if (TextUtils.isEmpty(rech_amount)) {
                    Toast.makeText(getApplicationContext(), "Enter coupon", Toast.LENGTH_SHORT).show();
                    return;
                }


                mDatabase=FirebaseDatabase.getInstance().getReference("RechargeToken");
                Query query=mDatabase.orderByChild("tokenID").equalTo(rech_amount);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                RechargeCard rechargeCard =snapshot1.getValue(RechargeCard.class);

                                //RechargeCard rechargeCard =snapshot1.getValue(RechargeCard.class);
                                System.out.println("!!!!!!!!!!!!!!!!!!11"+rechargeCard.getTokenID());

                                if(rechargeCard.isIs_available()==true){
                                    rechargeCard.setIs_available(false);
                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("RechargeToken");;
                                    databaseReference.child(snapshot1.getKey()).child("is_available").setValue(false);
                                    amount_recharged =rechargeCard.getAmount();


                                    val2= String.valueOf(amount_recharged);
                                    mDatabase = FirebaseDatabase.getInstance().getReference("PassengerBalance");
                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    System.out.println("!!!!!!!!!!!!!!!!!!"+passenger_ID);
                                    Query query1=mDatabase.orderByChild("passengerID").equalTo(currentFirebaseUser.getUid());
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull  DataSnapshot snapshot1) {
                                            if(snapshot1.exists()){
                                                for (DataSnapshot snapshot11:snapshot1.getChildren()){


                                                    PassengerBalance passengerBalance=snapshot11.getValue(PassengerBalance.class);
                                                    passengerBalance.setBalance(passengerBalance.getBalance()+amount_recharged);

                                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("PassengerBalance");;
                                                    databaseReference.child(snapshot11.getKey()).child("balance").setValue(passengerBalance.getBalance());

                                                    Toast.makeText(getApplicationContext(),"Recharge succesful!",Toast.LENGTH_SHORT);

                                                    progressDialog.dismiss();
                                                    Intent intent1=  new Intent(ActivityRecharge.this, RechargeSuccessful.class);

                                                    //System.out.println(val2);
                                                    intent1.putExtra("amra",val2);
                                                    startActivity(intent1);
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull  DatabaseError error) {

                                        }
                                    });


                                    //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+amount_recharged);
                                }
                                else
                                {
                                    Toast.makeText(ActivityRecharge.this, "Sorry This Pin is not available. Try another One", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

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



}