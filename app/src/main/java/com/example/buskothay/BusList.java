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


public class BusList extends ArrayAdapter<Bus> {

    private Activity context;
    private List<Bus> busList;

    public BusList(Activity context, List<Bus> busList) {
        super(context, R.layout.list_bus, busList);
        this.context = context;
        this.busList = busList;
    }




    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_bus, null, true);


        TextView textViewTravelsName = (TextView) listViewItem.findViewById(R.id.text_view_busName);
        TextView textViewBusNumber = (TextView) listViewItem.findViewById(R.id.text_view_busNumber);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.text_view_owner_Id);
        TextView textViewFrom = (TextView) listViewItem.findViewById(R.id.text_view_from);
        TextView textViewTo = (TextView) listViewItem.findViewById(R.id.text_view_to);
        TextView driverId = (TextView) listViewItem.findViewById(R.id.text_view_condition);


        Bus bus = busList.get(position);

        textViewTravelsName.setText(bus.getTravelsName());
        textViewBusNumber.setText("Bus Id       : "+bus.getBusId());

        textViewDate.setText("Owner_ID     : "+bus.getOwnerId());
        textViewFrom.setText("Bus From            : "+bus.getFrom());
        textViewTo.setText("Bus To                : "+bus.getTo());
        driverId.setText("Driver Id    : "+bus.getDriver_Id());

        return listViewItem;
    }
}