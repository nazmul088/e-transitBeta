/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.DriverSide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.buskothay.BusDetails;
import com.example.buskothay.DistanceInMetreJsonParser;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.R;
import com.example.buskothay.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DriverMapActivityUpdated extends FragmentActivity {




    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    private Button tripButton;
    private TextView textView;
    private DatabaseReference databaseReference;
    private ArrayList<LatLng> locationArrayList;
    private ArrayList<String> locationName;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private LocationCallback locationCallback, locationCallback1;
    boolean startLocationUpdate=false;
    Location driverStartLocation;
    LocationDetails nearbyLocation , previousLocation = null;
    private String FromLocation;
    boolean checkIfBeforePreviousLocation = false;
    private ArrayList<LocationDetails> Stoppages = new ArrayList<>();

    private Double distance1,distance2;
    boolean updateDistance2 = false;
    boolean updateDistance1 = false;

    private String busId ;

    int compDistanceInd = 0;
    boolean checkIfBeforeStoppage =false;
    boolean downTrip = false;

    ArrayList<LocationDetails> locationDetailsArrayList = new ArrayList<>();
    HashMap<String,LocationDetails> reverseStoppageHashmap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map_updated);

        tripButton = (Button) findViewById(R.id.button13);
        textView = (TextView) findViewById(R.id.textView7);


        databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");
        locationArrayList = new ArrayList<LatLng>();
        locationName = new ArrayList<String>();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);


        client = LocationServices.getFusedLocationProviderClient(this);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {  //This callback is written to update Starting location


                if (locationResult == null) {
                    return;
                }
                Location driverLocation = locationResult.getLastLocation();
                if(startLocationUpdate){
                    driverStartLocation = driverLocation;
                    startLocationUpdate = false;
                    DriverStartPointUpdate();
                }
                // Toast.makeText(DriverMapActivityUpdated.this, driverLocation.getLatitude() + ", " + driverLocation.getLongitude()
                //       , Toast.LENGTH_SHORT).show();



            }
        };


        //Callback function to update Realtime location data in database


        locationCallback1 = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {  //This callback is written to update current location


                if (locationResult == null) {
                    return;
                }
                Location driverLocation = locationResult.getLastLocation();
                System.out.println("BUS ID CHECK: "+busId);

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("DriverTrip").child(busId).child("currentLocationLatitude").setValue(String.valueOf(driverLocation.getLatitude()));
                databaseReference.child("DriverTrip").child(busId).child("currentLocationLongitude").setValue(String.valueOf(driverLocation.getLongitude()));
                databaseReference.child("DriverTrip").child(busId).child("bearing").setValue(String.valueOf(driverLocation.getBearing()));
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(driverLocation.getLatitude(),driverLocation.getLongitude()),18));

                CameraPosition currentPlace = new CameraPosition.Builder()
                        .target(new LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()))
                        .bearing(driverLocation.getBearing()).tilt(65.5f).zoom(18f).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
                // Toast.makeText(DriverMapActivityUpdated.this, driverLocation.getLatitude() + ", " + driverLocation.getLongitude()
                //       , Toast.LENGTH_SHORT).show();


                LocationDetails loc = Stoppages.get(compDistanceInd);
                double distance = 0.0;
                if(downTrip)
                    distance = SphericalUtil.computeDistanceBetween(new LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()), new LatLng(Double.parseDouble(reverseStoppageHashmap.get(loc.getPlaceName()).getLatitude()), Double.parseDouble(reverseStoppageHashmap.get(loc.getPlaceName()).getLongitude())));
                else
                    distance = SphericalUtil.computeDistanceBetween(new LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()), new LatLng(Double.parseDouble(loc.getLatitude()), Double.parseDouble(loc.getLongitude())));
                System.out.println("Current Location :" + driverLocation.getLatitude() + "\t" + driverLocation.getLongitude());
                System.out.println("distance:" + distance);
                System.out.println("Current Comparison: " + loc.getPlaceName());
                if (distance < 50) {
                    checkIfBeforeStoppage = true;
                }
                if(distance>50 && checkIfBeforeStoppage){
                    databaseReference.child("DriverTrip").child(busId).child("lastKnownLocation").setValue(Stoppages.get(compDistanceInd).getPlaceName());
                    System.out.println("Check Place Name" + Stoppages.get(compDistanceInd).getPlaceName());
                    compDistanceInd++;
                    if (compDistanceInd == Stoppages.size())
                        compDistanceInd = Stoppages.size() - 1;
                    checkIfBeforeStoppage =false;
                }
            }
        };




        if (ActivityCompat.checkSelfPermission(DriverMapActivityUpdated.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(DriverMapActivityUpdated.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


    }


    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        //locationRequest.setInterval(10000);
        //locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }


    protected void createLocationRequesttoUpdateData() {
        LocationRequest locationRequest = LocationRequest.create();
        //  locationRequest.setInterval(10000);
        locationRequest.setInterval(500);
        //locationRequest.setFastestInterval(5000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback1, Looper.getMainLooper());

    }











    protected double calculateDistance(LatLng latLng, LatLng lng){
        double distance = SphericalUtil.computeDistanceBetween(latLng,lng);
        return distance;
    }





    protected void DriverStartPointUpdate(){
        FirebaseUser currentFirebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("BusDetails");
        final Query query = databaseReference.orderByChild("driverId").equalTo(String.valueOf(currentFirebaseUser.getUid()));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        BusDetails busDetails = dataSnapshot.getValue(BusDetails.class);
                        String routeId = busDetails.getRoute_Id();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Route");
                        Query query1 = databaseReference.orderByChild("id").equalTo(routeId);
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                        Route route = dataSnapshot1.getValue(Route.class);
                                        System.out.println("Route Id: " + route.getID());
                                        String stoppage = route.getStoppage();
                                        String[] allStoppage = stoppage.split(",");
                                        System.out.println("Stoppage:" + stoppage);
                                        for(int i=0;i<allStoppage.length;i++)
                                        {
                                            String currentstoppage = allStoppage[i];
                                            //find this Stoppage Coordinates
                                            databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");
                                            Query query2 = databaseReference.orderByChild("placeName").equalTo(currentstoppage);
                                            query2.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        for(DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                                            LocationDetails locationDetails = dataSnapshot2.getValue(LocationDetails.class);
                                                            locationDetailsArrayList.add(locationDetails);
                                                        }
                                                    }

                                                    if(locationDetailsArrayList.size() == allStoppage.length)
                                                    {
                                                        int nearbyLocationIndex = 0;
                                                        double minValue = 99999999999.0;
                                                        for(int j=0;j<locationDetailsArrayList.size();j++)
                                                        {

                                                            double distance = calculateDistance(new LatLng(driverStartLocation.getLatitude(),driverStartLocation.getLongitude()) ,new LatLng(Double.parseDouble(locationDetailsArrayList.get(j).getLatitude()),
                                                                    Double.parseDouble(locationDetailsArrayList.get(j).getLongitude())));
                                                            if(distance<minValue)
                                                            {
                                                                minValue = distance;
                                                                nearbyLocation = locationDetailsArrayList.get(j);
                                                                nearbyLocationIndex = j;
                                                            }

                                                        }
                                                        System.out.println("Place Name1 :" +nearbyLocation.getPlaceName());
                                                        for(int j=0;j< Math.floor(locationDetailsArrayList.size()/2);j++)
                                                        {
                                                            if(allStoppage[j].equalsIgnoreCase(nearbyLocation.getPlaceName()))
                                                            {
                                                                FromLocation = allStoppage[0];
                                                                break;
                                                            }
                                                        }

                                                        for(int j = (int) Math.floor(locationDetailsArrayList.size()/2); j<locationDetailsArrayList.size(); j++)
                                                        {
                                                            if(allStoppage[j].equalsIgnoreCase(nearbyLocation.getPlaceName()))
                                                            {
                                                                FromLocation = allStoppage[allStoppage.length-1];
                                                                Collections.reverse(locationDetailsArrayList);
                                                                nearbyLocationIndex = locationDetailsArrayList.size()-nearbyLocationIndex-1;
                                                                downTrip = true;
                                                                break;
                                                            }
                                                        }


                                                        if(nearbyLocationIndex>0)
                                                            previousLocation = locationDetailsArrayList.get(nearbyLocationIndex-1);

                                                        if(downTrip)
                                                        {
                                                            //For DownTrip
                                                            databaseReference = FirebaseDatabase.getInstance().getReference("ReverseStoppage");
                                                            if(previousLocation!=null){ //That  means car start trip from different location in down trip

                                                                Query query3 = databaseReference.orderByChild("placeName").equalTo(previousLocation.getPlaceName());
                                                                query3.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                                                        if(snapshot.exists())
                                                                        {
                                                                            for(DataSnapshot dataSnapshot2 :snapshot.getChildren()){
                                                                                LocationDetails locationDetails = dataSnapshot2.getValue(LocationDetails.class);
                                                                                previousLocation = locationDetails;
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                                query3 = databaseReference.orderByChild("placeName").equalTo(nearbyLocation.getPlaceName());
                                                                int finalNearbyLocationIndex = nearbyLocationIndex;
                                                                query3.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if(snapshot.exists())
                                                                        {
                                                                            for(DataSnapshot dataSnapshot2: snapshot.getChildren())
                                                                            {
                                                                                LocationDetails locationDetails = dataSnapshot2.getValue(LocationDetails.class);
                                                                                nearbyLocation = locationDetails;
                                                                            }
                                                                            if(finalNearbyLocationIndex >0) {
                                                                                String url1 = getRequestedUrl(new LatLng(Double.parseDouble(previousLocation.getLatitude()), Double.parseDouble(previousLocation.getLongitude())),
                                                                                        new LatLng(Double.parseDouble(nearbyLocation.getLatitude()), Double.parseDouble(nearbyLocation.getLongitude())));
                                                                                String url2 = getRequestedUrl(new LatLng(Double.parseDouble(previousLocation.getLatitude()), Double.parseDouble(previousLocation.getLongitude())),
                                                                                        new LatLng(driverStartLocation.getLatitude(), driverStartLocation.getLongitude()));
                                                                                System.out.println("Distance between two location");
                                                                                updateDistance1 = true;
                                                                                new DownloadTask().execute(url1);
                                                                                System.out.println("Distance between previous Location and Car");
                                                                                new DownloadTask().execute(url2);
                                                                            }

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull  DatabaseError error) {

                                                                    }
                                                                });



                                                            }
                                                            else{ // That means car start location from source in down Trip

                                                                addDriverTrip("");
                                                                Stoppages = locationDetailsArrayList;
                                                                Stoppages.remove(0);
                                                            }

                                                        }
                                                        else{
                                                            //for UpTrip
                                                            if(nearbyLocationIndex>0)
                                                            {
                                                                String url1 = getRequestedUrl(new LatLng(Double.parseDouble(previousLocation.getLatitude()) ,Double.parseDouble(previousLocation.getLongitude()) ),
                                                                        new LatLng(Double.parseDouble(nearbyLocation.getLatitude()) ,Double.parseDouble(nearbyLocation.getLongitude())));

                                                                String url2 = getRequestedUrl(new LatLng(Double.parseDouble(previousLocation.getLatitude()) ,Double.parseDouble(previousLocation.getLongitude()) ),
                                                                        new LatLng(driverStartLocation.getLatitude() ,driverStartLocation.getLongitude()));
                                                                System.out.println("Distance between two location");
                                                                updateDistance1 = true;
                                                                new DownloadTask().execute(url1);
                                                                System.out.println("Distance between previous Location and Car");
                                                                new DownloadTask().execute(url2);
                                                            }
                                                            else{
                                                                addDriverTrip("");
                                                                Stoppages = locationDetailsArrayList;
                                                                Stoppages.remove(0);
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


    protected void addDriverTrip(String status){
        databaseReference = FirebaseDatabase.getInstance().getReference("BusDetails");
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = databaseReference.orderByChild("driverId").equalTo(String.valueOf(currentFirebaseUser.getUid()));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        final BusDetails busDetails = dataSnapshot.getValue(BusDetails.class);
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        String rideId = getRandomString(6);
                        DriverTrip driverTrip = new DriverTrip();
                        if(busDetails.getFrom().equalsIgnoreCase(nearbyLocation.getPlaceName()))
                        {

                            driverTrip = new DriverTrip(currentDate, currentTime, "", FromLocation, busDetails.getTo(),String.valueOf(driverStartLocation.getLatitude()),String.valueOf(driverStartLocation.getLongitude()),
                                    String.valueOf(driverStartLocation.getBearing()) , rideId, busDetails.getRegistrationNumber(), "Running",busDetails.getTravelsName(),nearbyLocation.getPlaceName(),busDetails.getRoute_Id(),busDetails.getBusType());
                        }
                        else if (busDetails.getTo().equalsIgnoreCase(nearbyLocation.getPlaceName()))
                        {
                            driverTrip = new DriverTrip(currentDate, currentTime, "", FromLocation, busDetails.getFrom(),String.valueOf(driverStartLocation.getLatitude()),String.valueOf(driverStartLocation.getLongitude()),
                                    String.valueOf(driverStartLocation.getBearing()) , rideId, busDetails.getRegistrationNumber(), "Running",busDetails.getTravelsName(),nearbyLocation.getPlaceName(),busDetails.getRoute_Id(),busDetails.getBusType());
                        }
                        else if(status.equalsIgnoreCase("A"))
                        {
                            if(FromLocation.equalsIgnoreCase(busDetails.getFrom()))
                            {
                                driverTrip = new DriverTrip(currentDate, currentTime, "", FromLocation, busDetails.getTo(),String.valueOf(driverStartLocation.getLatitude()),String.valueOf(driverStartLocation.getLongitude()),
                                        String.valueOf(driverStartLocation.getBearing()) , rideId, busDetails.getRegistrationNumber(), "Running",busDetails.getTravelsName(),nearbyLocation.getPlaceName(),busDetails.getRoute_Id(),busDetails.getBusType());
                            }
                            else{
                                driverTrip = new DriverTrip(currentDate, currentTime, "", FromLocation, busDetails.getFrom(),String.valueOf(driverStartLocation.getLatitude()),String.valueOf(driverStartLocation.getLongitude()),
                                        String.valueOf(driverStartLocation.getBearing()) , rideId, busDetails.getRegistrationNumber(), "Running",busDetails.getTravelsName(),nearbyLocation.getPlaceName(),busDetails.getRoute_Id(),busDetails.getBusType());

                            }

                        }
                        else
                        {
                            if(FromLocation.equalsIgnoreCase(busDetails.getFrom()))
                            {
                                driverTrip = new DriverTrip(currentDate, currentTime, "", FromLocation, busDetails.getTo(),String.valueOf(driverStartLocation.getLatitude()),String.valueOf(driverStartLocation.getLongitude()),
                                        String.valueOf(driverStartLocation.getBearing()) , rideId, busDetails.getRegistrationNumber(), "Running",busDetails.getTravelsName(),previousLocation.getPlaceName(),busDetails.getRoute_Id(),busDetails.getBusType());
                            }
                            else
                            {
                                driverTrip = new DriverTrip(currentDate, currentTime, "", FromLocation, busDetails.getFrom(),String.valueOf(driverStartLocation.getLatitude()),String.valueOf(driverStartLocation.getLongitude()),
                                        String.valueOf(driverStartLocation.getBearing()) , rideId, busDetails.getRegistrationNumber(), "Running",busDetails.getTravelsName(),previousLocation.getPlaceName(),busDetails.getRoute_Id(),busDetails.getBusType());
                            }


                        }

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        busId = busDetails.getRegistrationNumber();
                        databaseReference.child("DriverTrip").child(busId).setValue(driverTrip);
                        Toast.makeText(DriverMapActivityUpdated.this, "Trip Started Successfully", Toast.LENGTH_LONG).show();
                        textView = (TextView) findViewById(R.id.textView7);
                        textView.setText("Trip Started Successfully");

                        //Find reverse Stoppage Coordinates in down trip using Stoppage ArrayList

                        System.out.println("In Add Driver Trip");
                        if(downTrip)
                        {
                            for(int i=0 ; i<Stoppages.size();i++) {
                                System.out.println(Stoppages.get(i).getPlaceName());
                                databaseReference = FirebaseDatabase.getInstance().getReference("ReverseStoppage");
                                Query query1 = databaseReference.orderByChild("placeName").equalTo(Stoppages.get(i).getPlaceName());
                                query1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                                LocationDetails locationDetails = dataSnapshot1.getValue(LocationDetails.class);
                                                reverseStoppageHashmap.put(locationDetails.getPlaceName(),locationDetails);
                                            }
                                            if(reverseStoppageHashmap.size() == Stoppages.size())
                                            {
                                                createLocationRequesttoUpdateData();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull  DatabaseError error) {

                                    }
                                });
                            }
                        }
                        else{
                            createLocationRequesttoUpdateData();
                        }







                    }
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }











    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            mMap.setTrafficEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            System.out.println("Location: "+latLng.latitude+"\t"+latLng.longitude);
                            mMap.setMyLocationEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));



                            //Show All Markers

                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        LocationDetails location = dataSnapshot.getValue(LocationDetails.class);
                                        LatLng lng = new LatLng(Double.parseDouble(location.getLatitude()), Double.parseDouble(location.getLongitude()));
                                        locationArrayList.add(lng);
                                        locationName.add(location.getPlaceName());
                                    }


                                    for (int i = 0; i < locationArrayList.size(); i++) {
                                        mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(locationName.get(i)).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_image1)));
                                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
                                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }


                            });

                            tripButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(tripButton.getText().toString().equalsIgnoreCase("Start Trip")){ //For Starting a trip
                                        reverseStoppageHashmap.clear();
                                        locationDetailsArrayList.clear();
                                        Stoppages.clear();
                                        startLocationUpdate = true;
                                        createLocationRequest();
                                        tripButton.setText("Stop Trip");
                                    }
                                    else{
                                        downTrip = false; //To Stop Trip

                                        client.removeLocationUpdates(locationCallback1);
                                        databaseReference = FirebaseDatabase.getInstance().getReference("DriverTrip");
                                        Query query = databaseReference.orderByChild("registration_Number").equalTo(busId);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                                                {
                                                    String tripKey = dataSnapshot.getKey();
                                                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                                    databaseReference.child(tripKey).removeValue();
                                                    DriverTrip driverTrip = dataSnapshot.getValue(DriverTrip.class);
                                                    DriverTrip driverTrip1 = new DriverTrip(driverTrip.getDate(),driverTrip.getTripStartTime(),currentTime,driverTrip.getFrom(),driverTrip.getTo(),driverTrip.getCurrentLocationLatitude(),driverTrip.getCurrentLocationLongitude(),driverTrip.getBearing(),driverTrip.getRide_id(),driverTrip.getRegistration_Number(),"Completed",driverTrip.getTravelsName(),driverTrip.getLastKnownLocation(),driverTrip.getRoute_id(),driverTrip.getBusType());
                                                    databaseReference = FirebaseDatabase.getInstance().getReference("DriverTripHistory");
                                                    databaseReference.push().setValue(driverTrip1);
                                                    tripButton.setText("Start Trip");
                                                    textView.setText("Trip Completed");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });



                                    }
                                }
                            });

                        }
                    });
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getCurrentLocation();
            }
        }
    }



    //draw custom marker
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }




    //Distance and Time Calculation

    private String getRequestedUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = getResources().getString(R.string.google_maps_key);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key=" + APIKEY;
        System.out.println(url);
        return url;
    }

    //For Measuring Distance and Time
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    private class DownloadTask extends AsyncTask<String, Void, String> {



        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread



        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DistanceInMetreJsonParser parser = new DistanceInMetreJsonParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            // ArrayList<LatLng> points = null;
            //PolylineOptions polylineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            String distance = "";
            String duration = "";



            // Traversing through all the routes
            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                // points = new ArrayList<LatLng>();
                //polylineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                }
                System.out.println("Distance :"+distance);
                if(updateDistance2)
                {
                    distance2 = Double.parseDouble(distance);
                    updateDistance2 = false;

                    if(distance1>distance2)
                    {
                        System.out.println("Car is Before nearby Stoppage");

                        int position = 0;
                        for(int j=0;j<locationDetailsArrayList.size();j++)
                        {
                            if(locationDetailsArrayList.get(j).getPlaceName().equalsIgnoreCase(nearbyLocation.getPlaceName()))
                            {
                                position = j;
                            }
                        }
                        for(int j=position;j<locationDetailsArrayList.size();j++)
                        {
                            Stoppages.add(locationDetailsArrayList.get(j));
                        }
                        for(int k=0;k<Stoppages.size();k++)
                        {
                            System.out.println(Stoppages.get(k).getPlaceName());
                        }
                        addDriverTrip("B");

                    }
                    else if (distance1<distance2)
                    {
                        System.out.println("Car is After Nearby Stoppage");


                        int position = 0;
                        for(int j=0;j<locationDetailsArrayList.size();j++)
                        {
                            if(locationDetailsArrayList.get(j).getPlaceName().equalsIgnoreCase(nearbyLocation.getPlaceName()))
                            {
                                position = j+1;
                            }
                        }
                        for(int j=position;j<locationDetailsArrayList.size();j++)
                        {
                            Stoppages.add(locationDetailsArrayList.get(j));
                        }
                        for(int k=0;k<Stoppages.size();k++)
                        {
                            System.out.println(Stoppages.get(k).getPlaceName());
                        }
                        addDriverTrip("A");

                    }
                }

                if(updateDistance1)
                {
                    distance1 = Double.parseDouble(distance);
                    updateDistance2 = true;
                    updateDistance1 = false;
                }


            }


        }
    }




    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }



}