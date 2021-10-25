/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buskothay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddBusActivity extends AppCompatActivity {
    private EditText busName;
    private EditText modelName;
    private EditText sittingCapacity;
    private EditText registrationNumber;
    private Spinner modelYear;
    private Spinner registrationYear;
    private Spinner busType;
    private Button nextButton;
    private Button button;
    private static final int IMAGE_REQUEST_BUS = 2;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Uri imageuri;
    private String busFileUrl;
    private String currentUser;

    //private DatePickerDialog.OnDateSetListener mDatesetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

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


        busName = (EditText) findViewById(R.id.editTextTextPersonName4);
        modelName = (EditText) findViewById(R.id.editTextTextPersonName5);
        registrationNumber = (EditText) findViewById(R.id.editTextTextPersonName7);
        sittingCapacity = (EditText) findViewById(R.id.editTextTextPersonName9);
        SharedPreferences prefs = this.getSharedPreferences("logInPref", Context.MODE_PRIVATE);
        currentUser = prefs.getString("userId", "");


        //Now Defining Spinner
        modelYear = (Spinner) findViewById(R.id.spinner);
        registrationYear = (Spinner) findViewById(R.id.spinner3);
        busType = (Spinner) findViewById(R.id.spinner2);



        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Model Year");
        for (int i = 1990; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                    return  false;
                else
                    return true;
            }
        };
        modelYear.setAdapter(adapter);
        modelYear.setSelection(0);



        //Set up Registration Year Adapter
        ArrayList<String> years1 = new ArrayList<String>();
        years1.add("Registration Year");
        for (int i = 1990; i <= thisYear; i++) {
            years1.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years1){
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                    return  false;
                else
                    return true;
            }
        };
        registrationYear.setAdapter(adapter2);
        registrationYear.setSelection(0);

        ArrayList<String> busTypes = new ArrayList<String>();
        busTypes.add("AC");
        busTypes.add("Non-AC");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,busTypes);
        busType.setAdapter(adapter1);



        button = (Button) findViewById(R.id.button19);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Uploading Bus Data
                openImageforBus();

            }
        });





        nextButton = (Button) findViewById(R.id.button2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), AddRouteOwner.class);



                //Pass all the values to Add Route Owner side
                intent.putExtra("busName",busName.getText().toString());
                intent.putExtra("modelName",modelName.getText().toString());
                intent.putExtra("registrationNumber",registrationNumber.getText().toString());
                intent.putExtra("sittingCapacity",sittingCapacity.getText().toString());
                intent.putExtra("modelYear",modelYear.getSelectedItem().toString());
                intent.putExtra("registrationYear",registrationYear.getSelectedItem().toString());
                intent.putExtra("busType",busType.getSelectedItem().toString());
                intent.putExtra("busFileName",busFileUrl);
                intent.putExtra("UserID",currentUser);
                startActivity(intent);
                finish();
            }
        });








    }
    private void openImageforBus() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST_BUS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST_BUS && resultCode == RESULT_OK){
            imageuri = data.getData();
            uploadBusData();
        }

    }



    private String getFileExtension(Uri imageuri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
    }


    private void uploadBusData() {
        if(imageuri != null )
        {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            String busFileName = UUID.randomUUID().toString() +"."+ getFileExtension(imageuri);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("BusLicense").child(busFileName);
            storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            busFileUrl = url;
                            Log.d("DownloadUrl" , url);
                            Toast.makeText(AddBusActivity.this,"Image Upload Successful",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }




}


