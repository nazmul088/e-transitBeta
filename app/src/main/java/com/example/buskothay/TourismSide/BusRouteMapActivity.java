/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.TourismSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.example.buskothay.DriverSide.DriverMapActivityUpdated;
import com.example.buskothay.DriverSide.DriverTrip;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.R;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BusRouteMapActivity extends FragmentActivity {


    private DatabaseReference databaseReference;
    private ArrayList<TouristRoute> touristRoutes = new ArrayList<>();
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        client = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(BusRouteMapActivity.this,
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
                            System.out.println("Location: " + latLng.latitude + "\t" + latLng.longitude);
                            mMap.setMyLocationEnabled(true);
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));


                            //Show All Markers

                            String district = getIntent().getStringExtra("district");

                            databaseReference = FirebaseDatabase.getInstance().getReference("TouristStoppage");
                            Query query = databaseReference.orderByChild("district").equalTo(district);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            TouristRoute touristRoute = dataSnapshot.getValue(TouristRoute.class);
                                            touristRoutes.add(touristRoute);
                                        }
                                        //get all stoppages
                                        //Now showing marker

                                        for (int i = 0; i < touristRoutes.size(); i++) {
                                            LatLng lng = new LatLng(Double.parseDouble(touristRoutes.get(i).latitude), Double.parseDouble(touristRoutes.get(i).longitude));
                                            mMap.addMarker(new MarkerOptions().position(lng).title(touristRoutes.get(i).placeName).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_image1)));

                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(2.0f));
                                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
                                        }
                                        LatLng coordinate = new LatLng(Double.parseDouble(touristRoutes.get(0).latitude), Double.parseDouble(touristRoutes.get(0).longitude)); //Store these lat lng values somewhere. These should be constant.
                                        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                                coordinate, 10);
                                        mMap.animateCamera(location);

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
