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

public class TrackBusAdapter extends RecyclerView.Adapter<TrackBusAdapter.ViewHolder>{
    private List<TrackBus.TrackBusclass> trackbusList;
    OnNoteListener onNoteListener;
    private OnNoteListener monNoteListener;

    public TrackBusAdapter(List<TrackBus.TrackBusclass> trackbusList,OnNoteListener onNoteListener) {
        this.trackbusList = trackbusList;
        this.monNoteListener = onNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trackbusitem,parent,false);

        return new ViewHolder(view,monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fromLocation = trackbusList.get(position).getFrom();
        String toLocation = trackbusList.get(position).getTo();
        String busName = trackbusList.get(position).getBusName();
        String busNumber = trackbusList.get(position).getBusRegistrationNumber();
        String condition = trackbusList.get(position).getType();

        holder.setData(fromLocation,toLocation,busName,busNumber,condition);

    }

    @Override
    public int getItemCount() {
        return trackbusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fromLocationTextView;
        private TextView toLocationTextView;
        private TextView busNameTextView;
        private TextView busNumberTextView;
        private TextView busCondition;

        OnNoteListener onNoteListener;


        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            busNameTextView = (TextView) itemView.findViewById(R.id.textView86);
            busNumberTextView = (TextView) itemView.findViewById(R.id.textView87);
            fromLocationTextView = (TextView) itemView.findViewById(R.id.textView88);
            toLocationTextView = (TextView) itemView.findViewById(R.id.textView89);
            busCondition = (TextView) itemView.findViewById(R.id.textView90);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        public void setData(String fromLocation, String toLocation,String busName, String busNumber, String condition) {
            fromLocationTextView.setText(fromLocation);
            toLocationTextView.setText(toLocation);
            busNameTextView.setText(busName);
            busNumberTextView.setText(busNumber);
            busCondition.setText(condition);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener
    {
        void onNoteClick(int position);
    }
}
