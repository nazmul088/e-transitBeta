/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.R;
import com.example.buskothay.Route;
import com.example.buskothay.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddRouteOwner extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ArrayList<String> routes;
    private ArrayList<String> stoppages;
    private ArrayList<String> stoppageArraysforSearch = new ArrayList<>(); // get all stoppages
    private Spinner spinner;
    private ListView listView;
    private Button button;
    private TextView textView;
    private CheckBox checkbox;
    ArrayList<String> stoppages1 = new ArrayList<String>();
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route_owner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Capture item from Add Bus Activity



        Bundle bundle = getIntent().getExtras();
        String busName = bundle.getString("busName");
        String modelName = bundle.getString("modelName");
        String registrationNumber = bundle.getString("registrationNumber");
        String sittingCapacity = bundle.getString("sittingCapacity");
        String modelYear = bundle.getString("modelYear");
        String registrationYear = bundle.getString("registrationYear");
        String busType = bundle.getString("busType");
        String busFileName = bundle.getString("busFileName");
        currentUser = getIntent().getExtras().getString("UserID");


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        routes = new ArrayList<String>();
        stoppages = new ArrayList<String>();
        checkbox = (CheckBox) findViewById(R.id.checkBox);


        databaseReference = FirebaseDatabase.getInstance().getReference("Route");
        Query query = databaseReference.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Route route = dataSnapshot.getValue(Route.class);
                        routes.add(route.getFrom() + "-" + route.getTo());
                        stoppages.add(route.getStoppage());
                    }
                    for (int i = 0; i < stoppages.size(); i++) {
                        String[] items = stoppages.get(i).split(",");
                        for (int j = 0; j < items.length; j++) {
                            if (!stoppageArraysforSearch.contains(items[j]))
                                stoppageArraysforSearch.add(items[j]);
                        }

                    }


                    //After getting All the Routes
                    spinner = (Spinner) findViewById(R.id.spinner5);
                    ArrayAdapter adapter = new ArrayAdapter(AddRouteOwner.this, android.R.layout.simple_spinner_item, routes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    progressDialog.dismiss();
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int selected_Position = position;
                            listView = (ListView) findViewById(R.id.listView);
                            stoppages1.clear();

                            String[] items = stoppages.get(position).split(",");
                            for (int i = 0; i < items.length; i++)
                                stoppages1.add(items[i]);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddRouteOwner.this,
                                    android.R.layout.simple_list_item_multiple_choice, stoppages1);

                            listView.setAdapter(adapter);
                            listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
                            listView.setItemsCanFocus(false);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        textView = (TextView) findViewById(R.id.textView53);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Onclick on search place
                Dialog dialog = new Dialog(AddRouteOwner.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(650, 1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddRouteOwner.this, android.R.layout.simple_list_item_1, stoppageArraysforSearch);

                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Filter arraylist
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //When Item selected from List
                        //set selected item on text view
                        textView.setText(adapter.getItem(position));
                        changeRouteInDropDown(adapter.getItem(position));

                        dialog.dismiss();
                    }
                });
            }
        });


        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int size = 0;

                boolean isChecked = checkbox.isChecked();
                if (isChecked == true) {
                    size = listView.getCount();
                    for (int i = 0; i <= size; i++)
                        listView.setItemChecked(i, true);
                } else if (isChecked == false) {
                    size = listView.getCount();
                    for (int i = 0; i <= size; i++)
                        listView.setItemChecked(i, false);
                }
            }
        });




        button = (Button) findViewById(R.id.button22);//for Adding Raw Route
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRouteOwner.this, AddDriverActivity.class);
                int len = listView.getCount();
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> selectedStoppages = new ArrayList<>();
                for (int i = 0; i < len; i++)
                    if (checked.get(i)) {
                        String item = stoppages1.get(i);
                        selectedStoppages.add(item);
                        /* do whatever you want with the checked item */
                    }



                final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference("User");
                Query query = databaseReference.orderByChild("id").equalTo(currentUser);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                User user = dataSnapshot.getValue(User.class);

                                RequestAddBus requestAddBus = new RequestAddBus(user,busName,modelName,registrationNumber,sittingCapacity,modelYear
                                        ,registrationYear,busType,selectedStoppages.get(0),selectedStoppages.get(selectedStoppages.size() - 1),selectedStoppages,busFileName);
                                databaseReference = FirebaseDatabase.getInstance().getReference("RequestForAddBus");
                                //System.out.println("CHECK VALUE :" +databaseReference.push().getKey());
                                databaseReference.push().setValue(requestAddBus);
                                Toast.makeText(getApplicationContext(),
                                        "Request Completed",
                                        Toast.LENGTH_LONG)
                                        .show();
                                Intent intent = new Intent(getApplicationContext(),OwnerWindow.class);
                                intent.putExtra("UserID",currentUser);
                                startActivity(intent);
                                finish();

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });


            }
        });

        button = (Button) findViewById(R.id.button25); //for Adding More Route
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRouteOwner.this, AddMoreRoute.class);
                int len = listView.getCount();
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> selectedStoppages = new ArrayList<>();
                for (int i = 0; i < len; i++)
                    if (checked.get(i)) {
                        String item = stoppages1.get(i);
                        selectedStoppages.add(item);
                        /* do whatever you want with the checked item */
                    }
                intent.putStringArrayListExtra("selectedStoppage", selectedStoppages);
                intent.putStringArrayListExtra("stoppages", stoppages);
                intent.putExtra("busName", busName);
                intent.putExtra("modelName", modelName);
                intent.putExtra("registrationNumber", registrationNumber);
                intent.putExtra("sittingCapacity", sittingCapacity);
                intent.putExtra("modelYear", modelYear);
                intent.putExtra("registrationYear", registrationYear);
                intent.putExtra("busType", busType);
                intent.putExtra("busFileName",busFileName);
                intent.putExtra("UserID",currentUser);

                startActivity(intent);

            }
        });

    }

    private void changeRouteInDropDown(String item) {
        if(checkbox.isChecked())
            checkbox.toggle();

        ArrayList<String> routesafterSearch = new ArrayList<>();
        ArrayList<String> stoppagesAfterSearch = new ArrayList<>();

        for (int i = 0; i < stoppages.size(); i++) {
            String[] items = stoppages.get(i).split(",");
            for (int j = 0; j < items.length; j++) {
                if (item.equalsIgnoreCase(items[j])) {
                    routesafterSearch.add(routes.get(i));
                    stoppagesAfterSearch.add(stoppages.get(i));
                    break;

                }
            }

        }


        ArrayAdapter adapter = new ArrayAdapter(AddRouteOwner.this, android.R.layout.simple_spinner_item, routesafterSearch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listView = (ListView) findViewById(R.id.listView);
                // ArrayList<String> stoppages1 = new ArrayList<String>();
                stoppages1.clear();
                String[] items = stoppagesAfterSearch.get(position).split(",");
                for (int i = 0; i < items.length; i++)
                    stoppages1.add(items[i]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddRouteOwner.this,
                        android.R.layout.simple_list_item_multiple_choice, stoppages1);

                listView.setAdapter(adapter);
                listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
                listView.setItemsCanFocus(false);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}