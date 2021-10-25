/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.R;

public class RechargeSuccessful extends AppCompatActivity {

    private  static  int SPLASH_SCREEN=2000;

    private Button button;
    ImageView imageView22,imageView23;
    ImageView start_image;
    TextView logo,slogan;
    Animation topAnim,bottomAnim;
    String amount_recharged;
    private TextView val;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_successful);

        start_image=findViewById(R.id.imageView2);
        Intent intent1 = getIntent();
        amount_recharged  = intent1.getStringExtra("amra");
        val =(TextView)findViewById(R.id.textView57) ;
        System.out.println("###########"+amount_recharged);
        val.setText(amount_recharged + "  TK");


        button =(Button) findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_SSUCCESS();
            }
        });




    }


    public void openActivity_SSUCCESS() {
        //Intent intent=new Intent(this,LoginActivity.class);
        Intent intent=new Intent(this, PassengerWindow.class);
        startActivity(intent);

    }
}