/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.TourismSide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.R;

import java.util.ArrayList;

public class TourismActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayList<String> districts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism);

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


        ArrayList<String> divisions = new ArrayList<>();


        //districts array adapter

        //spinner array adapter

        spinner = (Spinner) findViewById(R.id.spinner);
        divisions.add("Select Any");
        divisions.add("Sylhet");
        divisions.add("Chittagong");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, divisions) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0)
                    textView.setTextColor(Color.GRAY);
                else
                    textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedPosition = i;

                if (selectedPosition== 1) {
                    districts.clear();
                    districts.add("Sylhet");
                    setDistrict();
                } else if (selectedPosition == 2) {
                    districts.clear();
                    districts.add("Cox's Bazar");
                    setDistrict();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //division selected



        //district Selected



    }

    private void setDistrict() {
        spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, districts);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Sylhet"))
                {
                    System.out.println("Dhukse eikhane");
                    //Sylhet selected
                    LinearLayout linearLayout = findViewById(R.id.linear_layout);
                    linearLayout.removeAllViews();
                }
                else if(adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("Cox's bazar"))
                {
                    //Coxs selected
                    System.out.println("IN COXS BAZAR");
                    showCoxsBazarImage();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void showCoxsBazarImage() {
        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        //Set one image
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.coxs_1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DetailsTourismPlaceActivity.class);
                intent.putExtra("place","coxs1");
                startActivity(intent);
            }
        });
        imageView.setLayoutParams(new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(imageView);

        TextView textView = new TextView(getApplicationContext());
        textView.setText("Coxs Bazar 1");
        textView.setGravity(Gravity.CENTER);
        linearLayout.addView(textView);

        //Set another image

        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.coxs_2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DetailsTourismPlaceActivity.class);
                intent.putExtra("place","coxs2");
                startActivity(intent);
            }
        });
        imageView.setLayoutParams(new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(imageView);

        textView = new TextView(getApplicationContext());
        textView.setText("Coxs Bazar 2");
        textView.setGravity(Gravity.CENTER);
        linearLayout.addView(textView);

        //Set another image

        imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.coxs_3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DetailsTourismPlaceActivity.class);
                intent.putExtra("place","coxs3");
                startActivity(intent);
            }
        });
        imageView.setLayoutParams(new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(imageView);

        textView = new TextView(getApplicationContext());
        textView.setText("Coxs Bazar 3");
        textView.setGravity(Gravity.CENTER);
        linearLayout.addView(textView);




    }
}