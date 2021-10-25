/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class LiveBusLocation extends FragmentActivity {
    private SupportMapFragment supportMapFragment;
    private String BusId;
    private DatabaseReference databaseReference;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private MarkerOptions markerOptions;
    private Marker busLocationMarker;
    private Polyline polyline;
    private Polyline polyline1;
    private TextView  textView;
    private boolean isBusReached= false;
    private String fromLocation,toLocation,route;
    private HashMap<String,LocationDetails> locationDetailsHashMap;
    int iteration = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_bus_location);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.passengermap);
        client = LocationServices.getFusedLocationProviderClient(this);
        markerOptions = new MarkerOptions();

        if (ActivityCompat.checkSelfPermission(LiveBusLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(LiveBusLocation.this,
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
                            putMarker();





                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_icon));
                            Bundle bundle = getIntent().getExtras();
                            BusId = bundle.getString("BusId");
                            //System.out.println("Bus Id :" + BusId);
                            final LocationDetails initialLocation = new LocationDetails();
                            initialLocation.setLatitude(bundle.getString("PassengerLocationLatitude"));
                            initialLocation.setLongitude(bundle.getString("PassengerLocationLongitude"));


                            String BusName = bundle.getString("BusName");
                            textView = (TextView) findViewById(R.id.textView17);
                            textView.setText(BusName);
                            String passengerLocation = bundle.getString("PassengerLocationName");
                            textView = (TextView) findViewById(R.id.textView11);
                            String text = "Distance from "+passengerLocation;
                            textView.setText(text);
                            textView = (TextView) findViewById(R.id.textView85);
                            textView.setText(BusId);



                            //textView = (TextView) findViewById(R.id.textView19);
                            //textView.setText(BusId);

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

                                            String lineUrl = getRequestedUrl(latLng,new LatLng(Double.parseDouble(initialLocation.getLatitude()) ,Double.parseDouble(initialLocation.getLongitude())));
                                            new DownloadTask().execute(lineUrl);
                                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                            builder.include(new LatLng(latitude, longitude));
                                            builder.include(new LatLng(Double.parseDouble(initialLocation.getLatitude()), Double.parseDouble(initialLocation.getLongitude())));

                                            LatLngBounds bounds = builder.build();
                                            int padding = 40; // offset from edges of the map in pixels
                                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                            //googleMap.moveCamera(cu);
                                              mMap.moveCamera(cu);


                                          //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                                        }
                                        else {





                                            busLocationMarker.setPosition(latLng);
                                            busLocationMarker.setRotation(bearing);
                                            if(!isBusReached)
                                            {
                                                String lineUrl = getRequestedUrl(latLng,new LatLng(Double.parseDouble(initialLocation.getLatitude()) ,Double.parseDouble(initialLocation.getLongitude())));
                                                new DownloadTask().execute(lineUrl);
                                            }




                                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                            builder.include(new LatLng(latitude, longitude));
                                            builder.include(new LatLng(Double.parseDouble(initialLocation.getLatitude()), Double.parseDouble(initialLocation.getLongitude())));

                                            LatLngBounds bounds = builder.build();
                                            int padding = 40; // offset from edges of the map in pixels
                                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                          //   mMap.animateCamera(cu);
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
        fromLocation = getIntent().getExtras().getString("PassengerLocationName");
        toLocation = getIntent().getExtras().getString("Destination");
        route = getIntent().getExtras().getString("Route");
        boolean direction = getIntent().getExtras().getBoolean("direction");
        locationDetailsHashMap = new HashMap<>();
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

                    if(direction)
                    {
                        databaseReference = FirebaseDatabase.getInstance().getReference("ReverseStoppage");
                    }
                    else{
                        databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");
                    }
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
                                        locationDetailsHashMap.put(locationDetails.getPlaceName(),locationDetails);

                                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(locationDetails.getLatitude()),Double.parseDouble(locationDetails.getLongitude()))).title(locationDetails.getPlaceName()).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_image1)));
                                    }

                                }

                                if(stoppage.length == locationDetailsHashMap.size())
                                {
                                    int startIndex = 0;
                                    for(int i=0;i<stoppage.length;i++)
                                    {
                                        if(stoppage[i].equalsIgnoreCase(fromLocation))
                                        {
                                            startIndex = i;
                                            break;

                                        }
                                    }

                                    LatLng location1 = new LatLng(Double.parseDouble(locationDetailsHashMap.get(fromLocation).getLatitude()),Double.parseDouble(locationDetailsHashMap.get(fromLocation).getLongitude()));
                                    LatLng location2 = new LatLng(Double.parseDouble(locationDetailsHashMap.get(toLocation).getLatitude()),Double.parseDouble(locationDetailsHashMap.get(toLocation).getLongitude()));
                                    String url = getRequestedUrl(location1,location2);
                                    System.out.println("Url: "+url);
                                    new DownloadTask1().execute(url);

                                    /*for(int i=startIndex+1;i<stoppage.length;i++)
                                    {
                                        LatLng location2 = new LatLng(Double.parseDouble(locationDetailsHashMap.get(stoppage[i]).getLatitude()),Double.parseDouble(locationDetailsHashMap.get(stoppage[i]).getLongitude()));
                                        String url = getRequestedUrl(location1,location2);
                                        System.out.println("URL: "+url);
                                        new DownloadTask1().execute(url);
                                        location1 = location2;
                                    }*/
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
                DistanceTimeJsonParser parser = new DistanceTimeJsonParser();

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
            ArrayList<LatLng> points = null;
            PolylineOptions polylineOptions = null;
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
                points = new ArrayList<LatLng>();
                polylineOptions = new PolylineOptions();

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

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                polylineOptions.addAll(points);
                polylineOptions.width(4);
                polylineOptions.color(Color.RED);
            }
            textView = (TextView) findViewById(R.id.textView13);
            textView.setText(distance);
            textView = (TextView) findViewById(R.id.textView15);
            textView.setText(duration);
            String[] distance1 = distance.split("\\s+");
            System.out.println(distance1[0] + "\t"+ distance1[1]);
            if(distance1[1].equalsIgnoreCase("m") && Integer.parseInt(distance1[0])<20)
            {
                isBusReached = true;
                Toast.makeText(getApplicationContext(), "Bus Reached", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(LiveBusLocation.this)
                        .setTitle("Exit")
                        .setMessage("Bus Reached Your Location.Enjoy your journey!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(),showNearbyBusDistanceAndTime.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();

            }
          //  System.out.println("Distance"+distance +"duration"+ duration);

            if (polylineOptions != null) {
                if (polyline!=null)
                {
                    polyline.remove();
                }
                polyline = mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found", Toast.LENGTH_LONG).show();
            }



        }






    }




    //Another copy of downloadtask


    private class DownloadTask1 extends AsyncTask<String, Void, String> {



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

            ParserTask1 parserTask1 = new ParserTask1();

            // Invokes the thread for parsing the JSON data
            parserTask1.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread



        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DistanceTimeJsonParser parser = new DistanceTimeJsonParser();

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
            ArrayList<LatLng> points = null;
            PolylineOptions polylineOptions = null;
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
                points = new ArrayList<LatLng>();
                polylineOptions = new PolylineOptions();

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

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                polylineOptions.addAll(points);
                polylineOptions.width(4);
                polylineOptions.color(Color.BLUE);
            }

            //  System.out.println("Distance"+distance +"duration"+ duration);

            if (polylineOptions != null) {
                if (polyline1!=null)
                {
                    polyline1.remove();
                }
                polyline1 = mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found", Toast.LENGTH_LONG).show();
            }



        }






    }






}