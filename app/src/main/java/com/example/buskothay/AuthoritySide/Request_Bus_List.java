/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.AuthoritySide;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.buskothay.BusDetails;
import com.example.buskothay.OwnerSide.RequestAddBus;
import com.example.buskothay.R;

import java.util.List;

public class Request_Bus_List extends ArrayAdapter<RequestAddBus> {

    private Activity context;
    private List<RequestAddBus> bus_details_List;

    public Request_Bus_List(Activity context, List<RequestAddBus> bus_details_List) {
        super(context, R.layout.check_request_list, bus_details_List);
        this.context = context;
        this.bus_details_List = bus_details_List;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.check_request_list, null, true);


        TextView textViewTravelsName = (TextView) listViewItem.findViewById(R.id.text_view_BusName_pending);
        TextView reg_no = (TextView) listViewItem.findViewById(R.id.text_view_reg_no);
       // TextView textViewDate = (TextView) listViewItem.findViewById(R.id.text_view_owner_Id);
        TextView textViewFrom = (TextView) listViewItem.findViewById(R.id.text_view_From_Location);
        TextView textViewTo = (TextView) listViewItem.findViewById(R.id.text_view_Last_Stoppage);
        //TextView driverId = (TextView) listViewItem.findViewById(R.id.text_view_condition);
        TextView textView_sit = (TextView) listViewItem.findViewById(R.id.text_view_Last_cap);
       // TextView textView_dr = (TextView) listViewItem.findViewById(R.id.text_view_drivr);

        RequestAddBus bus = bus_details_List.get(position);


        textViewTravelsName.setText(bus.getBusName());
        reg_no.setText("Bus Number : "+bus.getRegistrationNumber());
       // textViewDate.setText("Owner_ID     : "+bus.getOwnerId());
        textViewFrom.setText("From: "+bus.getFromLocation());
        textViewTo.setText("To: "+bus.getToLocation());
        textView_sit.setText("Capacity: "+bus.getSittingCapacity());
        //textView_dr.setText("Driver Name : "+bus.getDriver().getName());


        return listViewItem;
    }


}
