/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.AuthoritySide;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.buskothay.LoginActivity;
import com.example.buskothay.R;

public class AuthorityActivity extends AppCompatActivity {


    private CardView button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_authority);
        button=(CardView)findViewById(R.id.authority_card111);
        // button=(Button) findViewById(R.id.button9);
       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_auth9();
            }
        });

        button=(CardView)findViewById(R.id.authority_card222);
        // button=(Button) findViewById(R.id.button9);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_auth_09();
            }
        });


       button=(CardView)findViewById(R.id.authority_card555);
        //button=(Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_auth4();
            }
        });

        //Check to add someone


    }

    public void openActivity_auth_09() {
        //Intent intent=new Intent(this,LoginActivity.class);
        Intent intent=new Intent(this, viewApprovedBus.class);
        startActivity(intent);

    }

    public void openActivity_auth9() {
        //Intent intent=new Intent(this,LoginActivity.class);
        Intent intent=new Intent(this, Activity_Request_Bus.class);
        startActivity(intent);

    }
    public void openActivity_auth4() {
        //Intent intent=new Intent(this,LoginActivity.class);
        SharedPreferences prefs = this.getSharedPreferences("logInPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", "");     //RESET TO DEFAULT VALUE
        editor.putString("type","");
        editor.commit();
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

}
