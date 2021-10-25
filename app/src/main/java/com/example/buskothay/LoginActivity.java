/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.AuthoritySide.AuthorityActivity;
import com.example.buskothay.DriverSide.DriverWindow;
import com.example.buskothay.OwnerSide.OwnerWindow;
import com.example.buskothay.Passenger.PassengerWindow;
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

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth firebaseAuth;
    private Button btnSignIn;
    private TextView labelSignUp;
    private TextView cardView;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = this.getSharedPreferences("logInPref", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");
        String userType = prefs.getString("type","");
        if(userId.length()>0 && userType.equalsIgnoreCase("owner"))
        {
            Intent intent =  new Intent(LoginActivity.this,OwnerWindow.class);
            startActivity(intent);
        }
        else if(userId.length()>0 && userType.equalsIgnoreCase("driver"))
        {
            startActivity(new Intent(LoginActivity.this,DriverWindow.class));
        }
        else if(userId.length()>0 && userType.equalsIgnoreCase("authority"))
        {
            startActivity(new Intent(LoginActivity.this,AuthorityActivity.class));
        }
        else if(userId.length()>0 && userType.equalsIgnoreCase("passenger"))
        {
            startActivity(new Intent(LoginActivity.this,PassengerWindow.class));
        }


        setContentView(R.layout.log_in);

        labelSignUp = (TextView) findViewById(R.id.signUpLabelInSignIn);
        cardView = (TextView) findViewById(R.id.log_in_bt);
        labelSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


        inputEmail = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        inputPassword = (EditText) findViewById(R.id.editTextTextPassword2);

        firebaseAuth = FirebaseAuth.getInstance();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Signing In");
                progressDialog.show();
                String email = inputEmail.getText().toString() + "@gmail.com";
                final String password = inputPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT);
                    return;
                }

                mDatabase = FirebaseDatabase.getInstance().getReference("User");


                final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Minimum Password length must be 6", Toast.LENGTH_SHORT);
                                    progressDialog.dismiss();

                                } else {
                                    System.out.println("Current User ID:  " + task.getResult().getUser().getUid());

                                    Query query = mDatabase.orderByChild("id").equalTo(task.getResult().getUser().getUid());
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                                            if (snapshot.exists()) {

                                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                    progressDialog.dismiss();
                                                    User user = snapshot1.getValue(User.class);
                                                    if (user.getType().equalsIgnoreCase("Passenger")) {
                                                        SharedPreferences settings = getSharedPreferences("logInPref", 0);
                                                        SharedPreferences.Editor editor = settings.edit();


                                                        editor.putString("userId", user.getID());
                                                        editor.putString("type",user.getType());
                                                        editor.commit();

                                                        Intent intent = new Intent(LoginActivity.this, PassengerWindow.class);
                                                        startActivity(intent);


                                                    } else if (user.getType().equalsIgnoreCase("Driver")) {
                                                        SharedPreferences settings = getSharedPreferences("logInPref", 0);
                                                        SharedPreferences.Editor editor = settings.edit();


                                                        editor.putString("userId", user.getID());
                                                        editor.putString("type",user.getType());
                                                        editor.commit();
                                                        Intent intent = new Intent(LoginActivity.this, DriverWindow.class);
                                                        startActivity(intent);
                                                    } else if (user.getType().equalsIgnoreCase("Owner")) {
                                                        SharedPreferences settings = getSharedPreferences("logInPref", 0);
                                                        SharedPreferences.Editor editor = settings.edit();


                                                        editor.putString("userId", user.getID());
                                                        editor.putString("type",user.getType());
                                                        editor.commit();
                                                        Intent intent = new Intent(LoginActivity.this, OwnerWindow.class);
                                                        intent.putExtra("UserID", task.getResult().getUser().getUid());
                                                        startActivity(intent);
                                                    } else {
                                                        SharedPreferences settings = getSharedPreferences("logInPref", 0);
                                                        SharedPreferences.Editor editor = settings.edit();


                                                        editor.putString("userId", user.getID());
                                                        editor.putString("type",user.getType());
                                                        editor.commit();
                                                        Intent intent = new Intent(LoginActivity.this, AuthorityActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    finish();


                                                }
                                            } else {

                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Please Sign Up First", Toast.LENGTH_SHORT);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    //Toast.makeText(LoginActivity.this,"Sign In Successfully",Toast.LENGTH_LONG).show();


                                }
                            }
                        });
            }
        });
    }


    @Override
    public void onBackPressed() {
        LoginActivity.this.finishAffinity();
    }
}
