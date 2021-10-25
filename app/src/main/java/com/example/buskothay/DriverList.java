/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DriverList extends ArrayAdapter<Driver> {

    private Activity context;
    private List<Driver> driverList;

    public DriverList(Activity context, List<Driver> driverList) {
        super(context, R.layout.driver_list, driverList);
        this.context = context;
        this.driverList = driverList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem2 = inflater.inflate(R.layout.driver_list, null, true);


        TextView textViewDriverName = (TextView) listViewItem2.findViewById(R.id.text_view_driverName);
        TextView textViewDriverId = (TextView) listViewItem2.findViewById(R.id.text_view_driver_Id);
        TextView textViewLicenseNo = (TextView) listViewItem2.findViewById(R.id.text_view_license_No);
        TextView textViewExperience = (TextView) listViewItem2.findViewById(R.id.text_view_Experience);
       // TextView textViewTo = (TextView) listViewItem.findViewById(R.id.text_view_to);
       // TextView textViewCondition = (TextView) listViewItem.findViewById(R.id.text_view_condition);


       // Bus bus = busList.get(position);
        Driver driver=driverList.get(position);

        textViewDriverName.setText(driver.getName());
        textViewDriverId.setText("Driver Id      : "+driver.getID());
        textViewLicenseNo.setText("License No     : "+driver.getLicense_No());
        textViewExperience.setText("Experience            : "+driver.getExperience());


        return listViewItem2;
    }



}
