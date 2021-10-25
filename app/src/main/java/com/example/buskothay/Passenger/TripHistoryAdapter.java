/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buskothay.R;

import java.util.List;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.ViewHolder>{



    private List<FairPayment> fairPaymentList;


    public TripHistoryAdapter(List<FairPayment> fairPaymentList) {
        this.fairPaymentList = fairPaymentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.triphistoryitem,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fromLocation = fairPaymentList.get(position).getFrom();
        String toLocation = fairPaymentList.get(position).getTo();
        int fare = fairPaymentList.get(position).getAmount();
        String busName = fairPaymentList.get(position).getBusName();
        String busNumber = fairPaymentList.get(position).getBusRegistrationId();
        String travelDate = fairPaymentList.get(position).getDate();
        String tripStartTime = fairPaymentList.get(position).getFromTime();
        String tripEndTime = fairPaymentList.get(position).getToTime();

        holder.setData(fromLocation,toLocation,fare,busName,busNumber,travelDate,tripStartTime,tripEndTime);

    }

    @Override
    public int getItemCount() {
        return fairPaymentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView fromLocationTextView;
        private TextView toLocationTextView;
        private TextView fareTextView;
        private TextView busNameTextView;
        private TextView busNumberTextView;
        private TextView travelDateTextView;
        private TextView tripStartTimeTextView;
        private TextView tripEndTimeTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            travelDateTextView = (TextView) itemView.findViewById(R.id.textView65);
            busNameTextView = (TextView) itemView.findViewById(R.id.textView66);
            busNumberTextView = (TextView) itemView.findViewById(R.id.textView67);
            fromLocationTextView = (TextView) itemView.findViewById(R.id.textView68);
            toLocationTextView = (TextView) itemView.findViewById(R.id.textView69);
            tripStartTimeTextView = (TextView)itemView.findViewById(R.id.textView70);
            tripEndTimeTextView = (TextView) itemView.findViewById(R.id.textView71);
            fareTextView = (TextView) itemView.findViewById(R.id.textView72);


        }

        public void setData(String fromLocation, String toLocation, int fare, String busName, String busNumber, String travelDate, String tripStartTime, String tripEndTime) {
            travelDateTextView.setText(travelDate);
            busNameTextView.setText(busName);
            busNumberTextView.setText(busNumber);
            fromLocationTextView.setText(fromLocation);
            toLocationTextView.setText(toLocation);
            tripStartTimeTextView.setText(tripStartTime);
            tripEndTimeTextView.setText(tripEndTime);
            fareTextView.setText(String.valueOf(fare));
        }
    }
}
