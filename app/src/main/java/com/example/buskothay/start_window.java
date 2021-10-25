/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class start_window extends AppCompatActivity {

    private  static  int SPLASH_SCREEN=3000;

    private Button button;
    ImageView imageView22,imageView23;
    ImageView start_image;
    TextView logo,slogan;
    Animation topAnim,bottomAnim;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        topAnim= AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim=AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        start_image=findViewById(R.id.imageView2);
        logo=findViewById(R.id.trackYourBus);
        start_image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               //Intent intent= new Intent(this,LoginActivity.class);
               //startActivity(intent);
                openActivity33();
               finish();

            }
        },SPLASH_SCREEN);


        /*button=(Button) findViewById(R.id.click_to_continue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity33();
            }
        });*/



        }


    public void openActivity33() {
        //Intent intent=new Intent(this,LoginActivity.class);
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);

    }
}
