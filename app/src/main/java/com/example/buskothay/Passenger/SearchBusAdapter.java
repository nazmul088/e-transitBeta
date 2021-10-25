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

public class SearchBusAdapter extends RecyclerView.Adapter<SearchBusAdapter.ViewHolder>{
    private List<showNearbyBusDistanceAndTime.BusDistanceTime> searchbusList;
    OnNoteListener onNoteListener;
    private OnNoteListener monNoteListener;

    public SearchBusAdapter(List<showNearbyBusDistanceAndTime.BusDistanceTime> searchbusList,OnNoteListener onNoteListener) {
        this.searchbusList = searchbusList;
        this.monNoteListener = onNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchbusitem,parent,false);

        return new ViewHolder(view,monNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fromLocation = searchbusList.get(position).getFrom();
        String toLocation = searchbusList.get(position).getTo();
        int fare = searchbusList.get(position).getFare();
        String busName = searchbusList.get(position).getName();
        String busNumber = searchbusList.get(position).getRegistrationNumber();
        String timetoArrival = searchbusList.get(position).getTimeFromBustoSource();
        String distancetoArrival = searchbusList.get(position).getDistanceFromBus();
        String timetoReachDestination = searchbusList.get(position).getTimeFromSourcetoDestination();
        String condition = searchbusList.get(position).getType();

        holder.setData(fromLocation,toLocation,fare,busName,busNumber,timetoArrival,distancetoArrival,timetoReachDestination,condition);

    }

    @Override
    public int getItemCount() {
        return searchbusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fromLocationTextView;
        private TextView toLocationTextView;
        private TextView fareTextView;
        private TextView busNameTextView;
        private TextView busNumberTextView;
        private TextView timetoReachSourceTextView;
        private TextView distancetoReachSourceTextView;
        private TextView timetoReachDestinationTextView;
        private TextView busCondition;

        OnNoteListener onNoteListener;


        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            busNameTextView = (TextView) itemView.findViewById(R.id.textView73);
            busNumberTextView = (TextView) itemView.findViewById(R.id.textView74);
            fromLocationTextView = (TextView) itemView.findViewById(R.id.textView75);
            toLocationTextView = (TextView) itemView.findViewById(R.id.textView76);
            timetoReachSourceTextView = (TextView) itemView.findViewById(R.id.textView77);
            distancetoReachSourceTextView = (TextView) itemView.findViewById(R.id.textView78);
            timetoReachDestinationTextView = (TextView) itemView.findViewById(R.id.textView81);
            fareTextView = (TextView) itemView.findViewById(R.id.textView82);
            busCondition = (TextView) itemView.findViewById(R.id.textView83);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        public void setData(String fromLocation, String toLocation, int fare, String busName, String busNumber, String timetoArrival, String distancetoArrival, String timetoReachDest,String condition) {
            fromLocationTextView.setText(fromLocation);
            toLocationTextView.setText(toLocation);
            busNameTextView.setText(busName);
            busNumberTextView.setText(busNumber);
            timetoReachSourceTextView.setText(timetoArrival);
            distancetoReachSourceTextView.setText(distancetoArrival);
            timetoReachDestinationTextView.setText(timetoReachDest);
            busCondition.setText(condition);
            fareTextView.setText(String.valueOf(fare)+" Tk");
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
