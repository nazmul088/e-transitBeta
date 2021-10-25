/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.Passenger;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buskothay.DriverSide.DriverTrip;
import com.example.buskothay.LocationDetails;
import com.example.buskothay.OwnerSide.FarePay;
import com.example.buskothay.R;
import com.example.buskothay.Route;
import com.example.buskothay.StoppageList;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class showNearbyBusDistanceAndTime extends AppCompatActivity implements SearchBusAdapter.OnNoteListener {


    private static final String TAG = "showNearbyBusDistance";

    private DatabaseReference databaseReference;
    private Button button;
    private LocationDetails passengerStartLocation;
    private ProgressDialog progressDialog;
    private String sourceLocation,destinationLocation;

    private ArrayList<BusDistanceTime> busDistanceTimeArrayList = new ArrayList<BusDistanceTime>();
    private int totalBus=0;
    private static int busCounter =0;
    private boolean oppositeSide = false;
    private LocationDetails initialLocation;
    private ArrayList<Boolean> directionState = new ArrayList<>();

    RecyclerView recyclerView;
    SearchBusAdapter searchBusAdapter;
    LinearLayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nearby_bus_distance_and_time);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();



        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout1);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCurrentFocus()!=null && getCurrentFocus() instanceof AutoCompleteTextView)
                {
                    InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
        button = (Button) findViewById(R.id.button12);

      //  progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        final ArrayList<String> allLocations = new ArrayList<String>();


        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView1);
        final AutoCompleteTextView textView2 = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView2);



        databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");
        //List all the available Location
        final Query query1 = databaseReference.orderByKey();
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        LocationDetails location = dataSnapshot.getValue(LocationDetails.class); //get all the locaations from database
                        if(location.getPlaceName()!=null)
                        allLocations.add(location.getPlaceName());
                    }
                    progressDialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(showNearbyBusDistanceAndTime.this,
                android.R.layout.simple_dropdown_item_1line, allLocations);

        textView.setAdapter(adapter);

        textView2.setAdapter(adapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setCursorVisible(false);
                if(getCurrentFocus()!=null && getCurrentFocus() instanceof AutoCompleteTextView)
                {
                    InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setCursorVisible(true);
            }
        });

        textView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView2.setCursorVisible(false);
                if(getCurrentFocus()!=null && getCurrentFocus() instanceof AutoCompleteTextView)
                {
                    InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }

            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setCursorVisible(true);
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int size = busDistanceTimeArrayList.size();
                busDistanceTimeArrayList.clear();

                recyclerView = findViewById(R.id.RecyclerView2);
                layoutManager = new LinearLayoutManager(showNearbyBusDistanceAndTime.this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                searchBusAdapter = new SearchBusAdapter(busDistanceTimeArrayList,showNearbyBusDistanceAndTime.this);
                recyclerView.setAdapter(searchBusAdapter);
                searchBusAdapter.notifyDataSetChanged();


                progressDialog.show();

            //Hide Keyboard
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);

                //TextView textView1= (TextView) findViewById(R.id.textView12);
                //textView1.setVisibility(View.INVISIBLE);
                busDistanceTimeArrayList.clear();
                passengerStartLocation = new LocationDetails();
                totalBus = 0;
                busCounter = 0;
                oppositeSide=false;

                final String fromLocation = textView.getText().toString();//input of fromLocation
                final String toLocation = textView2.getText().toString();//input of to location

                sourceLocation = fromLocation;
                destinationLocation = toLocation;


                databaseReference = FirebaseDatabase.getInstance().getReference("Route");
                Query query = databaseReference.orderByKey();
                final ArrayList<ArrayList<String>> allpassedLocations = new ArrayList<>();
                final ArrayList<String> sourceLocation = new ArrayList<>();
                final ArrayList<String> routes = new ArrayList<>();
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                boolean startStoppages = true;
                                boolean endStoppages = false;
                                String MainSource = "" ;
                                String MainSource1 = "";
                                ArrayList<String> Stoppages = new ArrayList<String>();
                                ArrayList<String> Stoppages1 = new ArrayList<String>();
                                int fromSerial = 0, toSerial = 0;
                                Route route = dataSnapshot.getValue(Route.class);
                                String[] allStoppage = route.getStoppage().split(",");
                                for(int i=0;i<allStoppage.length;i++){
                                    MainSource = allStoppage[0];
                                    MainSource1 = allStoppage[allStoppage.length-1];
                                    if(startStoppages)
                                        Stoppages.add(allStoppage[i]);
                                    if(endStoppages)
                                        Stoppages1.add(allStoppage[i]);
                                    if(fromLocation.equalsIgnoreCase(allStoppage[i])){
                                        fromSerial = i+1;
                                        Stoppages.remove(i);
                                        startStoppages = false;
                                        endStoppages = true;
                                    }
                                    else if(toLocation.equalsIgnoreCase(allStoppage[i]))
                                    {
                                        toSerial = i+1;
                                    }
                                }
                                if(fromSerial>0 && toSerial>0)
                                {
                                    routes.add(route.getID());
                                    if(fromSerial>toSerial) // to location comes first
                                    {
                                        oppositeSide = true;
                                        directionState.add(true);
                                        sourceLocation.add(MainSource1);

                                        allpassedLocations.add(Stoppages1);
                                    }
                                    else if(toSerial>fromSerial) //from Location comes first
                                    {
                                        directionState.add(false);
                                        sourceLocation.add(MainSource);
                                        allpassedLocations.add(Stoppages);

                                    }

                                }
                            }


                            //Now Find Those bus who is currently travelling from these Source Location

                            if(routes.size()==0)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(showNearbyBusDistanceAndTime.this);
                                builder.setMessage("No Bus Available")
                                        .setTitle("");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button

                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();



                               // TextView textView1= (TextView) findViewById(R.id.textView12);
                                //textView1.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                                return;
                            }

                            final ArrayList<Vehicle> vehicles = new ArrayList<>();
                            boolean checkIfCompleted= false;

                            for(int i=0 ; i< sourceLocation.size();i++)
                            {
                                databaseReference = FirebaseDatabase.getInstance().getReference("DriverTrip");
                                if(i+1 == sourceLocation.size())
                                {
                                    checkIfCompleted = true;
                                }
                                Query query1 = databaseReference.orderByChild("from").equalTo(sourceLocation.get(i));
                                final boolean finalCheckIfCompleted = checkIfCompleted;
                                final int finalI = i;
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                DriverTrip driverTrip = dataSnapshot.getValue(DriverTrip.class);
                                                if(routes.get(finalI).equalsIgnoreCase(driverTrip.getRoute_id()))
                                                {
                                                    for(int k=0;k<allpassedLocations.get(finalI).size();k++)
                                                    {
                                                        String stop = allpassedLocations.get(finalI).get(k);
                                                        if(stop.equalsIgnoreCase(driverTrip.getLastKnownLocation()))
                                                        {

                                                            Vehicle vehicle = new Vehicle(driverTrip.getCurrentLocationLatitude(),driverTrip.getCurrentLocationLongitude(),driverTrip.getTravelsName(),driverTrip.getFrom(),driverTrip.getTo(),driverTrip.getLastKnownLocation(),driverTrip.getRoute_id(),driverTrip.getRegistration_Number(),driverTrip.getBusType());
                                                            vehicles.add(vehicle);//capture all the bus based on source and destination.
                                                        }

                                                    }
                                                }
                                            }






                                        }


                                        if(finalCheckIfCompleted)
                                        {
                                            totalBus = vehicles.size();
                                            if(totalBus==0)
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(showNearbyBusDistanceAndTime.this);
                                                builder.setMessage("No Bus Available")
                                                        .setTitle("");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User clicked OK button

                                                    }
                                                });

                                                AlertDialog dialog = builder.create();
                                                dialog.show();



                                               //TextView textView3 = (TextView) findViewById(R.id.textView12);
                                                //textView3.setVisibility(View.VISIBLE);
                                                progressDialog.dismiss();
                                                return;
                                            }
                                            if(!oppositeSide)
                                                databaseReference = FirebaseDatabase.getInstance().getReference("Stoppage");
                                            else
                                                databaseReference = FirebaseDatabase.getInstance().getReference("ReverseStoppage");

                                            Query query2 = databaseReference.orderByChild("placeName").equalTo(fromLocation);
                                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                            LocationDetails  locationDetails = dataSnapshot.getValue(LocationDetails.class);
                                                            initialLocation = new LocationDetails();
                                                            initialLocation.setLatitude(locationDetails.getLatitude());
                                                            initialLocation.setLongitude(locationDetails.getLongitude());
                                                            initialLocation.setPlaceName(locationDetails.getPlaceName());
                                                            for(int k=0;k<vehicles.size();k++)
                                                            {

                                                                String disUrl = getDirectionsUrl(new LatLng(Double.parseDouble(vehicles.get(k).getLatitude()) ,Double.parseDouble(vehicles.get(k).getLongitude())),new LatLng(Double.parseDouble(locationDetails.getLatitude()) , Double.parseDouble(locationDetails.getLongitude()) ));
                                                                new DownloadTask(vehicles.get(k).getBusName(),vehicles.get(k).getBusId(),vehicles.get(k).getBusType(),vehicles.get(k).getFrom(),vehicles.get(k).getTo(),vehicles.get(k).getRouteId()).execute(disUrl);
                                                            }
                                                            //got all the buses distance and time from bus's current location to from
                                                            //Now need to find distance from from to to Location
                                                            Query query3 = databaseReference.orderByChild("placeName").equalTo(toLocation);
                                                            query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists())
                                                                    {
                                                                        for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                                                        {
                                                                            LocationDetails locationDetails1 = dataSnapshot1.getValue(LocationDetails.class);
                                                                            String disUrl = getDirectionsUrl(new LatLng(Double.parseDouble(locationDetails.getLatitude()),Double.parseDouble(locationDetails.getLongitude())),
                                                                                    new LatLng(Double.parseDouble(locationDetails1.getLatitude()),Double.parseDouble(locationDetails1.getLongitude())));
                                                                            new DownloadTask("testBus","testBus","testBus","testBus","testBus","testBus").execute(disUrl);
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
        });


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    Set<String> removeDuplicates(ArrayList<String> data)
    {
        final Set<String> setToReturn = new HashSet<>();
        final Set<String> set1 = new HashSet<>();

        for (String yourInt : data)
        {
            if (!set1.add(yourInt))
            {
                setToReturn.add(yourInt);
            }
        }
        return setToReturn;
    }


    //Find Distance and Time
    private String getDirectionsUrl(LatLng origin, LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

       // String mode = "mode=transit&transit_mode=bus";

        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;

        // Output format
        String output = "json";
        String APIKEY = getResources().getString(R.string.google_maps_key);

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key=" +APIKEY;

        return url;
    }


    /** A method to download json data from url */
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


        String busName ;
        String busId;
        String busType;
        String from;
        String to;
        String routeId;
        public DownloadTask(String busName,String busId,String busType,String from,String to,String routeId) {
            this.busName = busName;
            this.busId = busId;
            this.busType = busType;
            this.from = from;
            this.to = to;
            this.routeId = routeId;
        }
        public DownloadTask(){

        }


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

            ParserTask parserTask = new ParserTask(busName,busId,busType,from,to,routeId);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread


        String busName;
        String busId;
        String busType;
        String from;
        String to;
        String routeId;

        public ParserTask(String busName,String busId,String busType,String from,String to,String routeId) {
            this.busName = busName;
            this.busId = busId;
            this.busType = busType;
            this.from = from;
            this.to = to;
            this.routeId = routeId;
        }

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

            String distance = "";
            String duration = "";



            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
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

            }
            System.out.println(busName);
            busCounter++;
            System.out.println("Distance"+distance +"duration"+ duration);
            //Now Calculate Fare for this bus

            databaseReference = FirebaseDatabase.getInstance().getReference("FareList");
            Query query = databaseReference.orderByChild("bus_reg_Id").equalTo(busId);
            String finalDistance = distance;
            String finalDuration = duration;
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        for(DataSnapshot dataSnapshot :snapshot.getChildren())
                        {
                            FarePay farePay = dataSnapshot.getValue(FarePay.class);
                            System.out.println("Bus Check: "+busName);
                            int fare = CalculateFare(farePay.getBus_reg_Id(),sourceLocation,destinationLocation,farePay.getStoppageLists(),oppositeSide);
                            System.out.println("Fare: "+fare);
                            BusDistanceTime busDistanceTime = new BusDistanceTime(busName,busId,busType,from,to, finalDistance, finalDuration,fare,routeId);
                            busDistanceTimeArrayList.add(busDistanceTime);
                        }
                    }
                    else{
                        for(int i=0;i<busDistanceTimeArrayList.size();i++)
                        {
                            busDistanceTimeArrayList.get(i).setTimeFromSourcetoDestination(finalDuration);
                        }
                        System.out.println("Size:"+busDistanceTimeArrayList.size());


                        recyclerView = findViewById(R.id.RecyclerView2);
                        layoutManager = new LinearLayoutManager(showNearbyBusDistanceAndTime.this);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        searchBusAdapter = new SearchBusAdapter(busDistanceTimeArrayList,showNearbyBusDistanceAndTime.this::onNoteClick);
                        recyclerView.setAdapter(searchBusAdapter);
                        searchBusAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();








                        /*final CustomBusAdapter customBusAdapter = new CustomBusAdapter(showNearbyBusDistanceAndTime.this,busDistanceTimeArrayList);
                        progressDialog.dismiss();
                        listView.setAdapter(customBusAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                BusDistanceTime selectedItemPosition = (BusDistanceTime) customBusAdapter.getItem(position);
                                Intent intent = new Intent(showNearbyBusDistanceAndTime.this,LiveBusLocation.class);
                                intent.putExtra("BusId",selectedItemPosition.getRegistrationNumber());
                                intent.putExtra("PassengerLocationLatitude",initialLocation.getLatitude());
                                intent.putExtra("PassengerLocationLongitude",initialLocation.getLongitude());
                                intent.putExtra("PassengerLocationName",initialLocation.getPlaceName());
                                intent.putExtra("BusName",selectedItemPosition.getName());
                                startActivity(intent);
                            }
                        });*/

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }
    }



    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(showNearbyBusDistanceAndTime.this,LiveBusLocation.class);
        intent.putExtra("BusId",busDistanceTimeArrayList.get(position).getRegistrationNumber());
        intent.putExtra("PassengerLocationLatitude",initialLocation.getLatitude());
        intent.putExtra("PassengerLocationLongitude",initialLocation.getLongitude());
        intent.putExtra("PassengerLocationName",initialLocation.getPlaceName());
        intent.putExtra("Destination",destinationLocation);
        intent.putExtra("direction",oppositeSide);
        intent.putExtra("Route",busDistanceTimeArrayList.get(position).getRouteId());
        intent.putExtra("BusName",busDistanceTimeArrayList.get(position).getName());
        startActivity(intent);

    }




    class BusDistanceTime{
        String name;
        String registrationNumber;
        String type;
        String from;
        String to;
        String distanceFromBus;
        String timeFromBustoSource;
        String timeFromSourcetoDestination;
        int fare;
        String routeId;


        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public int getFare() {
            return fare;
        }

        public void setFare(int fare) {
            this.fare = fare;
        }

        public BusDistanceTime() {
        }

        public BusDistanceTime(String name, String registrationNumber, String type, String from, String to, String distanceFromBus, String timeFromBustoSource, String timeFromSourcetoDestination,String routeId) {
            this.name = name;
            this.registrationNumber = registrationNumber;
            this.type = type;
            this.from = from;
            this.to = to;
            this.distanceFromBus = distanceFromBus;
            this.timeFromBustoSource = timeFromBustoSource;
            this.timeFromSourcetoDestination = timeFromSourcetoDestination;
            this.routeId = routeId;
        }

        public BusDistanceTime(String name, String registrationNumber, String type, String from, String to, String distanceFromBus, String timeFromBustoSource,int fare,String routeId) {
            this.name = name;
            this.registrationNumber = registrationNumber;
            this.type = type;
            this.from = from;
            this.to = to;
            this.distanceFromBus = distanceFromBus;
            this.timeFromBustoSource = timeFromBustoSource;
            this.fare = fare;
            this.routeId = routeId;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }

        public void setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getDistanceFromBus() {
            return distanceFromBus;
        }

        public void setDistanceFromBus(String distanceFromBus) {
            this.distanceFromBus = distanceFromBus;
        }

        public String getTimeFromBustoSource() {
            return timeFromBustoSource;
        }

        public void setTimeFromBustoSource(String timeFromBustoSource) {
            this.timeFromBustoSource = timeFromBustoSource;
        }

        public String getTimeFromSourcetoDestination() {
            return timeFromSourcetoDestination;
        }

        public void setTimeFromSourcetoDestination(String timeFromSourcetoDestination) {
            this.timeFromSourcetoDestination = timeFromSourcetoDestination;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    class Vehicle{
        String latitude;
        String longitude;
        String busName;
        String from;
        String to;
        String lastKnownLocation;
        String routeId;
        String busId;
        String busType;

        public Vehicle() {
        }


        public Vehicle(String latitude, String longitude, String busName, String from,String to ,String lastKnownLocation, String routeId,String busId,String busType) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.busName = busName;
            this.from = from;
            this.to = to;
            this.lastKnownLocation = lastKnownLocation;
            this.routeId = routeId;
            this.busId = busId;
            this.busType = busType;
        }

        public String getBusId() {
            return busId;
        }

        public void setBusId(String busId) {
            this.busId = busId;
        }

        public String getLastKnownLocation() {
            return lastKnownLocation;
        }

        public void setLastKnownLocation(String lastKnownLocation) {
            this.lastKnownLocation = lastKnownLocation;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getBusName() {
            return busName;
        }

        public void setBusName(String busName) {
            this.busName = busName;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getBusType() {
            return busType;
        }

        public void setBusType(String busType) {
            this.busType = busType;
        }
    }



    public class CustomBusAdapter extends BaseAdapter{
        Context context;
        ArrayList<BusDistanceTime> BusArrayList = new ArrayList<>();

        public CustomBusAdapter(Context context, ArrayList<BusDistanceTime> BusArrayList) {
            this.context = context;
            this.BusArrayList = BusArrayList;
        }

        @Override
        public int getCount() {
            return BusArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return BusArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.show_bus_distance_time_list,parent,false);
            }

            BusDistanceTime busDistanceTime = (BusDistanceTime)getItem(position);
            TextView busName = (TextView) convertView.findViewById(R.id.textView8);
            TextView busDistance = (TextView) convertView.findViewById(R.id.textView9);
            TextView busDuration = (TextView) convertView.findViewById(R.id.textView10);
            busName.setText(busDistanceTime.getName());
            busDistance.setText(busDistanceTime.getDistanceFromBus());
            busDuration.setText(busDistanceTime.getTimeFromBustoSource());
            return convertView;
        }
    }


    int CalculateFare(String busId, String source, String destination , ArrayList<StoppageList> stoppageLists, boolean direction)
    {
        int totalFare = 0;
        int startIndex = 0;
        boolean startLocationFareZero = false;
        if(!direction)
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


}