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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.Passenger.PassengerBalance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp;
    private FirebaseAuth firebaseAuth;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //Get firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        btnSignUp = (Button) findViewById(R.id.signUpButton);
        inputEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        inputPassword = (EditText) findViewById(R.id.editTextTextPassword);




        //test
        mDatabase = FirebaseDatabase.getInstance().getReference("Test");
        mDatabase.push().setValue("sajib");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString().trim() + "@gmail.com";
                String password = inputPassword.getText().toString().trim();
                System.out.println(email+"\t"+password);

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                }
                radioGroup = (RadioGroup) findViewById(R.id.userType);
                int checkedButtonId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(checkedButtonId);
                //System.out.println("Check Radio Button :  "+radioButton.getText());
                //Create User
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "Create User with Email:Complete" + task.isComplete(), Toast.LENGTH_SHORT).show();
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                } else {
                                    User user = new User(currentFirebaseUser.getUid(), email, (String) radioButton.getText());

                                    if (radioButton.getText().toString().equalsIgnoreCase("driver")) {

                                        /*mDatabase = FirebaseDatabase.getInstance().getReference().child("User");
                                        mDatabase.push().setValue(user);


                                        Intent intent = new Intent(SignupActivity.this, driver_bus_relation.class);
                                        intent.putExtra("id", currentFirebaseUser.getUid());
                                        intent.putExtra("email", email);
                                        intent.putExtra("type", (String) radioButton.getText());
                                        startActivity(intent);*/
                                    } else if (radioButton.getText().toString().equalsIgnoreCase("owner")) {

                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("User");

                                        mDatabase.push().setValue(user);
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Owner");
                                        mDatabase.push().setValue(user);
                                        Toast.makeText(SignupActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                                        // startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        mDatabase.push().setValue(user);

                                        PassengerBalance passengerBalance=new PassengerBalance();
                                        passengerBalance.setPassengerID(user.getID());
                                        passengerBalance.setBalance(0);
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("PassengerBalance");
                                        mDatabase.push().setValue(passengerBalance);


                                        Toast.makeText(SignupActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                                        // startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }


                                }

                            }
                        });
            }
        });
    }
}
