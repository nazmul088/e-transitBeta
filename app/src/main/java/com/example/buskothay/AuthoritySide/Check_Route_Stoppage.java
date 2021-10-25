/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.AuthoritySide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.R;

import java.util.ArrayList;

public class Check_Route_Stoppage extends AppCompatActivity {


    private ArrayList<String> bus_Stoppage;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_route_stoppage_xml);

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
        listView = (ListView) findViewById(R.id.fullRouteListView2);

        Intent intent11=getIntent();
        bus_Stoppage=intent11.getStringArrayListExtra("message__1");


        ArrayList<String> locations = new ArrayList<String>();
        for(int i=0; i <bus_Stoppage.size();i++)
        {
            locations.add((i+1)+"."+bus_Stoppage.get(i));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,locations);
        listView.setAdapter(arrayAdapter);

    }
}
