/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.DriverSide.DriverTrip;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.OwnerSide.FarePay;
import com.example.buskothay.Route;
import com.example.buskothay.StoppageList;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView ScannerView;
    ArrayList<LocationDetails> locationDetailsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ScannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }


    int CalculateFare(String busId,String source,String destination , ArrayList<StoppageList> stoppageLists,boolean direction)
    {
        System.out.println(source+"\t"+destination);
        int totalFare = 0;
        int startIndex = 0;
        boolean startLocationFareZero = false;
        if(direction)
        {
            for(int i=0;i<stoppageLists.size();i++)
            {
                if(stoppageLists.get(i).getExpenseName().equalsIgnoreCase(source))
                {
                    startIndex = i;
                    if(stoppageLists.get(i).getAmount()==0)
                        startLocationFareZero = true;
                    break;

                }
            }
            for(int i=startIndex ; i<stoppageLists.size();i++)
            {
                if(stoppageLists.get(i).getExpenseName().equalsIgnoreCase(destination))
                    break;
                totalFare = totalFare + stoppageLists.get(i).getAmount();
            }
            if(startLocationFareZero)
            {
                for(int i=startIndex;i>=0;i--)
                {
                    if(stoppageLists.get(i).getAmount()!=0)
                    {
                        totalFare = totalFare + stoppageLists.get(i).getAmount();
                        break;
                    }
                }
            }

        }
        else
        {
            for(int i=stoppageLists.size()-1;i>=0;i--)
            {
                if(stoppageLists.get(i).getExpenseName().equalsIgnoreCase(source))
                {
                    startIndex = i;
                    if(stoppageLists.get(i).getAmount()==0)
                        startLocationFareZero = true;
                    break;
                }
            }
            for(int i=startIndex ; i>=0;i--)
            {
                if(stoppageLists.get(i).getExpenseName().equalsIgnoreCase(destination))
                    break;
                totalFare = totalFare + stoppageLists.get(i).getAmount();
            }
            if(startLocationFareZero)
            {
                for(int i=startIndex;i<stoppageLists.size();i++)
                {
                    if(stoppageLists.get(i).getAmount()!=0)
                    {
                        totalFare = totalFare + stoppageLists.get(i).getAmount();
                        break;
                    }
                }
            }


        }




        return totalFare;
    }


    @Override
    public void handleResult(Result rawResult) {
        //Here we get the result
        locationDetailsArrayList = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FarePaymentPartial");
        Query query = databaseReference.orderByChild("passengerId").equalTo(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    //for destination stoppage
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        FairPayment fairPayment = dataSnapshot.getValue(FairPayment.class);
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("DriverTrip");
                        Query query1 = databaseReference1.orderByChild("registration_Number").equalTo(rawResult.getText());
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    for (DataSnapshot dataSnapshot1:snapshot.getChildren())
                                    {
                                        final DriverTrip driverTrip = dataSnapshot1.getValue(DriverTrip.class);
                                        LocationDetails busLocation = new LocationDetails(driverTrip.getCurrentLocationLatitude(),driverTrip.getCurrentLocationLongitude());
                                        final String rideId = driverTrip.getRide_id();
                                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Route");
                                        Query query2 = databaseReference1.orderByChild("id").equalTo(driverTrip.getRoute_id());
                                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                                if(snapshot.exists())
                                                {
                                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                                    {
                                                        Route route = dataSnapshot1.getValue(Route.class);

                                                        String[] stoppages = route.getStoppage().split(",");

                                                        for(int i=0;i<stoppages.length;i++)
                                                        {
                                                            String location = stoppages[i];
                                                            DatabaseReference databaseReference2 = null;
                                                            if(fairPayment.isDirection()) {
                                                                databaseReference2 = FirebaseDatabase.getInstance().getReference("Stoppage");
                                                            }
                                                            else
                                                            {
                                                                databaseReference2 = FirebaseDatabase.getInstance().getReference("ReverseStoppage");
                                                            }
                                                            Query query3 = databaseReference2.orderByChild("placeName").equalTo(location);
                                                            int finalI = i;
                                                            query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists())
                                                                    {
                                                                        for(DataSnapshot dataSnapshot2 : snapshot.getChildren())
                                                                        {
                                                                            LocationDetails locationDetails = dataSnapshot2.getValue(LocationDetails.class);
                                                                            locationDetailsArrayList.add(locationDetails);
                                                                        }
                                                                        if(finalI +1 == stoppages.length)
                                                                        {
                                                                            String nearByLocation =  getNearbyLocation(busLocation);
                                                                            System.out.println("nearby location:"+ nearByLocation);
                                                                            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("FareList");
                                                                            Query query4 = databaseReference3.orderByChild("bus_reg_Id").equalTo(rawResult.getText());
                                                                            query4.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    if(snapshot.exists())
                                                                                    {
                                                                                        for(DataSnapshot dataSnapshot2 : snapshot.getChildren())
                                                                                        {
                                                                                            FarePay farePay = dataSnapshot2.getValue(FarePay.class);
                                                                                            int fare = CalculateFare(farePay.getBus_reg_Id(),fairPayment.getFrom(),nearByLocation,farePay.getStoppageLists(),fairPayment.isDirection());
                                                                                            System.out.println("Fare Is: "+fare);
                                                                                            FairPayment fairPayment1 = new FairPayment(firebaseUser.getUid(),rideId,fairPayment.getFrom(),nearByLocation,fare,fairPayment.getFromTime(),new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()),
                                                                                                    new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()),fairPayment.getBusRegistrationId(),fairPayment.getBusName());
                                                                                            databaseReference.child(dataSnapshot.getKey()).removeValue();
                                                                                            DatabaseReference  databaseReference1 = FirebaseDatabase.getInstance().getReference("FarePayment");
                                                                                            databaseReference1.push().setValue(fairPayment1);


                                                                                            //cut ammount from passenger's account
                                                                                            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("PassengerBalance");
                                                                                            Query query = databaseReference4.orderByChild("passengerID").equalTo(firebaseUser.getUid());
                                                                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                                                                                    if(snapshot.exists())
                                                                                                    {
                                                                                                        for(DataSnapshot dataSnapshot3 : snapshot.getChildren())
                                                                                                        {
                                                                                                            PassengerBalance passengerBalance = dataSnapshot3.getValue(PassengerBalance.class);
                                                                                                            databaseReference4.child(dataSnapshot3.getKey()).child("balance").setValue(passengerBalance.getBalance()-fare);

                                                                                                            Intent intent = new Intent(ScannerView.this,FairSuccessfulActivity.class);
                                                                                                            intent.putExtra("FairPayment",fairPayment1);
                                                                                                            intent.putExtra("Balance",passengerBalance.getBalance()-fare);
                                                                                                            startActivity(intent);
                                                                                                        }
                                                                                                    }

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull  DatabaseError error) {

                                                                                                }
                                                                                            });
                                                                                            //Intent intent = new Intent(ScannerView.this,FairSuccessfulActivity.class);
                                                                                            //startActivity(intent);
                                                                                        }
                                                                                    }

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });


                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                        }


                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
                else
                {
                    //for source stoppage

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("DriverTrip");
                    Query query1 = databaseReference1.orderByChild("registration_Number").equalTo(rawResult.getText());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    final DriverTrip driverTrip = dataSnapshot.getValue(DriverTrip.class);
                                    LocationDetails busLocation = new LocationDetails(driverTrip.getCurrentLocationLatitude(),driverTrip.getCurrentLocationLongitude());
                                    final String rideId = driverTrip.getRide_id();
                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Route");
                                    Query query2 = databaseReference1.orderByChild("id").equalTo(driverTrip.getRoute_id());
                                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                                {
                                                    Route route = dataSnapshot1.getValue(Route.class);
                                                    boolean direction = true;
                                                    if(route.getFrom().equalsIgnoreCase(driverTrip.getFrom()))
                                                    {
                                                        direction = true;
                                                    }
                                                    else
                                                    {
                                                        direction = false;
                                                    }

                                                    String[] stoppages = route.getStoppage().split(",");

                                                        for(int i=0;i<stoppages.length;i++)
                                                        {
                                                            String location = stoppages[i];
                                                            DatabaseReference databaseReference2 = null;
                                                            if(direction) {
                                                                 databaseReference2 = FirebaseDatabase.getInstance().getReference("Stoppage");
                                                            }
                                                            else
                                                            {
                                                                databaseReference2 = FirebaseDatabase.getInstance().getReference("ReverseStoppage");
                                                            }
                                                            Query query3 = databaseReference2.orderByChild("placeName").equalTo(location);
                                                            int finalI = i;
                                                            boolean finalDirection = direction;
                                                            query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists())
                                                                    {
                                                                        for(DataSnapshot dataSnapshot2 : snapshot.getChildren())
                                                                        {
                                                                            LocationDetails locationDetails = dataSnapshot2.getValue(LocationDetails.class);
                                                                            locationDetailsArrayList.add(locationDetails);
                                                                        }
                                                                        if(finalI +1 == stoppages.length)
                                                                        {
                                                                            String nearByLocation =  getNearbyLocation(busLocation);
                                                                            FairPayment fairPayment = new FairPayment(firebaseUser.getUid(),rideId,nearByLocation,"",0,new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()),"",
                                                                                    new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()), finalDirection,driverTrip.getRegistration_Number(),driverTrip.getTravelsName());

                                                                            databaseReference.push().setValue(fairPayment);
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(ScannerView.this);
                                                                            builder.setMessage("Please Scan Second time when you get off from this bus")
                                                                                    .setTitle("Scan Successful");
                                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    // User clicked OK button
                                                                                    Intent intent = new Intent(ScannerView.this,PassengerWindow.class);
                                                                                    startActivity(intent);
                                                                                }
                                                                            });

                                                                            AlertDialog dialog = builder.create();
                                                                            dialog.show();
                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                        }


                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private String getNearbyLocation(LocationDetails busLocation) {
        double minDistance = 99999999;
        String nearestLocation = "";
        for(int i=0;i<locationDetailsArrayList.size();i++)
        {
            double distance = SphericalUtil.computeDistanceBetween(new LatLng(Double.parseDouble(busLocation.getLatitude()) ,Double.parseDouble(busLocation.getLongitude())),
                    new LatLng(Double.parseDouble( locationDetailsArrayList.get(i).getLatitude()),Double.parseDouble(locationDetailsArrayList.get(i).getLongitude())));
            if(distance<minDistance)
            {
                minDistance = distance;
                nearestLocation = locationDetailsArrayList.get(i).getPlaceName();
            }
        }
        return nearestLocation;
    }



    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }

}