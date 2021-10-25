/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.buskothay.DriverSide.DriverTrip;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.R;
import com.example.buskothay.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LiveTrackBusActivity extends FragmentActivity {
    private SupportMapFragment supportMapFragment;
    private String BusId;
    private DatabaseReference databaseReference;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private MarkerOptions markerOptions;
    private Marker busLocationMarker;

    private TextView  textView;
    private String route;
    private HashMap<String,LocationDetails> locationDetailsHashMap;
    int iteration = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_track_bus);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.passengermap1);
        client = LocationServices.getFusedLocationProviderClient(this);
        markerOptions = new MarkerOptions();

        if (ActivityCompat.checkSelfPermission(LiveTrackBusActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(LiveTrackBusActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


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
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;

                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            mMap.setMyLocationEnabled(true);


                            //Put Marker

                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_icon));
                            Bundle bundle = getIntent().getExtras();
                            BusId = bundle.getString("BusId");

                            String BusName = bundle.getString("BusName");
                            textView = (TextView) findViewById(R.id.textView91);
                            textView.setText(BusName);

                            textView = (TextView) findViewById(R.id.textView92);
                            textView.setText(BusId);
                            route = bundle.getString("RouteId");
                            putMarker();
                            databaseReference = FirebaseDatabase.getInstance().getReference("DriverTrip");
                            Query query1 = databaseReference.orderByChild("registration_Number").equalTo(BusId);
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        DriverTrip driverTrip = dataSnapshot.getValue(DriverTrip.class);
                                        Double latitude = Double.valueOf(driverTrip.getCurrentLocationLatitude());
                                        Double longitude = Double.valueOf(driverTrip.getCurrentLocationLongitude());
                                        float bearing = Float.parseFloat(driverTrip.getBearing());
                                        LatLng latLng = new LatLng(latitude, longitude);
                                        if (busLocationMarker == null) {

                                            markerOptions.position(latLng);
                                            markerOptions.rotation(bearing);
                                            System.out.println(markerOptions.isFlat());
                                            markerOptions.flat(true);

                                            markerOptions.anchor((float) 0.5, (float) 0.5);
                                            if(markerOptions!=null)
                                            {
                                                busLocationMarker = mMap.addMarker(markerOptions);
                                            }

                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),17));


                                        }
                                        else {
                                            busLocationMarker.setPosition(latLng);
                                            busLocationMarker.setRotation(bearing);
                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),17));
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }
                    });
                }
            }
        });

    }

    public void putMarker()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Route");
        Query query = databaseReference.orderByChild("id").equalTo(route);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String stoppages ="";
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Route route = dataSnapshot.getValue(Route.class);
                        stoppages = route.getStoppage();
                        break;
                    }
                    String[] stoppage = stoppages.split(",");

                    databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");

                    for(int i=0;i<stoppage.length;i++)
                    {
                        iteration = i;
                        Query query1 = databaseReference.orderByChild("placeName").equalTo(stoppage[i]);
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        LocationDetails locationDetails = dataSnapshot.getValue(LocationDetails.class);
                                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locationDetails.getLatitude()),Double.parseDouble(locationDetails.getLongitude()))).title(locationDetails.getPlaceName()).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_image1)));
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










}