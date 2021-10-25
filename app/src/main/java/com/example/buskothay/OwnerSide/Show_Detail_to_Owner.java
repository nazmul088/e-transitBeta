/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Show_Detail_to_Owner extends AppCompatActivity {

    private String msg1;
    private TextView textView;
    private String str;
    private Button button;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String busCondition, busId, busNumber, from, ownerId, route_Id, to, travelsName, driver_Id;
    private String modelName, sittingCapacity, registrationNumber, modelYear, registrationYear, busType;
    private ArrayList<String> bus_Stoppage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_to_owner_xml);


        textView = (TextView) findViewById(R.id.owner_side1);
        Intent intent = getIntent();
        str = intent.getStringExtra("messagep1");
        travelsName = str;
        textView.setText(str);

        textView = (TextView) findViewById(R.id.owner_side2);
        Intent intent1 = getIntent();
        str = intent1.getStringExtra("messagep2");
        busType = str;
        textView.setText(str);


        textView = (TextView) findViewById(R.id.owner_side3);
        Intent intent3 = getIntent();
        str = intent3.getStringExtra("messagep3");
        from = str;
        textView.setText(str);

        textView = (TextView) findViewById(R.id.owner_side5);
        Intent intent4 = getIntent();
        str = intent4.getStringExtra("messagep4");
        modelName = str;
        textView.setText(str);

        textView = (TextView) findViewById(R.id.owner_side6);
        Intent intent5 = getIntent();
        str = intent5.getStringExtra("messagep5");
        modelYear = str;
        textView.setText(str);
/*
        textView=(TextView)findViewById(R.id.owner_side7);
        Intent intent6=getIntent();
        str =intent6.getStringExtra("messagep6");
        ownerId=str;
        textView.setText(str);
*/

        textView = (TextView) findViewById(R.id.owner_side8);
        Intent intent7 = getIntent();
        str = intent7.getStringExtra("messagep7");
        registrationNumber = str;
        textView.setText(str);

        textView = (TextView) findViewById(R.id.owner_side9);
        Intent intent8 = getIntent();
        str = intent8.getStringExtra("messagep8");
        registrationYear = str;
        textView.setText(str);

        textView = (TextView) findViewById(R.id.owner_side10);
        Intent intent9 = getIntent();
        str = intent9.getStringExtra("messagep9");
        sittingCapacity = str;
        textView.setText(str);

        textView = (TextView) findViewById(R.id.owner_side4);
        Intent intent10 = getIntent();
        str = intent10.getStringExtra("messagep10");
        to = str;
        textView.setText(str);

        textView = (TextView) findViewById(R.id.textView58);
        textView.setText(getIntent().getStringExtra("messagep12"));

        textView = (TextView) findViewById(R.id.textView59);
        textView.setText(getIntent().getStringExtra("messagep13"));
        if(textView.getText().toString().equalsIgnoreCase("Not Provided"))
        {
            textView = (TextView) findViewById(R.id.textView60);
            textView.setText("");
        }





    }

}
