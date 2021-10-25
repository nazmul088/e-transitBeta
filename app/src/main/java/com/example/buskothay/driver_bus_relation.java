/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class driver_bus_relation extends AppCompatActivity {

    private EditText inputName, inputLicense_No,input_experience;
    private Button btnSignIn,btnSignUp;
    private FirebaseAuth firebaseAuth;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DatabaseReference mDatabase ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_bus_route);


        Intent intent=getIntent();
        final String id =intent.getStringExtra("id");
        final String email_id =intent.getStringExtra("email");
        final String type =intent.getStringExtra("type");


        btnSignUp = (Button) findViewById(R.id.button8);
        inputName = (EditText) findViewById(R.id.editTextTextName);
        inputLicense_No = (EditText) findViewById(R.id.editTextTextBus_Id);
        input_experience = (EditText) findViewById(R.id.editTextTextRoute_Id);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Driver");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Name_driver = inputName.getText().toString().trim();
                String License_No = inputLicense_No.getText().toString().trim();
                String experience_year=input_experience.getText().toString().trim();

                if(TextUtils.isEmpty(Name_driver)){
                    Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(License_No)){
                    Toast.makeText(getApplicationContext(),"Enter Bus Id!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(experience_year)){
                    Toast.makeText(getApplicationContext(),"Enter Route Id!",Toast.LENGTH_SHORT).show();
                    return;
                }

                Driver driver=new Driver(id,email_id,type,Name_driver,License_No,experience_year,false);
                final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                DatabaseReference drTravellingPath =FirebaseDatabase.getInstance().getReference().child("Driver");
              //  drTravellingPath.child("status").setValue("Offline");
                drTravellingPath.push().setValue(driver);


                Toast.makeText(driver_bus_relation.this,"Authentication successful",Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(SignupActivity.this,MainActivity.class));
                Intent intent = new Intent(driver_bus_relation.this,LoginActivity.class);
                startActivity(intent);




            }
        });





    }
}
