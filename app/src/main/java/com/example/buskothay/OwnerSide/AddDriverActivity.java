/*
 * Copyright (c) 2021. Project completed by Nazmul Islam , Sakibul Islam & Binoy Kumar
 */

package com.example.buskothay.OwnerSide;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buskothay.R;
import com.example.buskothay.User;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class AddDriverActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private String name;
    private String MobileNumber;
    private String password;
    private DatabaseReference databaseReference;
    private Uri imageuri;
    private static final int IMAGE_REQUEST_BUS = 2;
    private static final int IMAGE_REQUEST_DRIVER = 3;
    private String busFileUrl,driverFileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        //Capture All items from AddRouteOwner
        Bundle bundle = getIntent().getExtras();
        String busName = bundle.getString("busName");
        String modelName = bundle.getString("modelName");
        String registrationNumber = bundle.getString("registrationNumber");
        String sittingCapacity = bundle.getString("sittingCapacity");
        String modelYear = bundle.getString("modelYear");
        String registrationYear = bundle.getString("registrationYear");
        String busType = bundle.getString("busType");
        String fromLocation = bundle.getString("fromLocation");
        String toLocation = bundle.getString("toLocation");
        ArrayList<String> stoppages = bundle.getStringArrayList("Stoppages");









        button = (Button) findViewById(R.id.button19);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Uploading Bus Data
                openImageforBus();

            }
        });

        button = (Button) findViewById(R.id.button21);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //For Uploading Driver Data
                openImageforDriver();

            }
        });


        button = (Button) findViewById(R.id.button18);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText = (EditText) findViewById(R.id.editTextTextPersonName6);
                name = editText.getText().toString();

                editText = (EditText) findViewById(R.id.editTextTextPersonName8);
                MobileNumber = editText.getText().toString();

                DriverUpdated driverUpdated = new DriverUpdated(name,MobileNumber,"");
                final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference("User");
                Query query = databaseReference.orderByChild("id").equalTo(currentFirebaseUser.getUid().toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                User user = dataSnapshot.getValue(User.class);

                               /* RequestAddBus requestAddBus = new RequestAddBus(user,busName,modelName,registrationNumber,sittingCapacity,modelYear
                                ,registrationYear,busType,fromLocation,toLocation,stoppages,driverUpdated,driverFileUrl,busFileUrl);*/
                                databaseReference = FirebaseDatabase.getInstance().getReference("RequestForAddBus");
                                //System.out.println("CHECK VALUE :" +databaseReference.push().getKey());
                               // databaseReference.push().setValue(requestAddBus);
                                Toast.makeText(getApplicationContext(),
                                        "Request Completed",
                                        Toast.LENGTH_LONG)
                                        .show();
                                Intent intent = new Intent(getApplicationContext(),OwnerWindow.class);
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

    }

    private void openImageforDriver() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST_DRIVER);
    }

    private void openImageforBus() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST_BUS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST_BUS && resultCode == RESULT_OK){
            imageuri = data.getData();
            uploadBusData();
        }
        else if(requestCode == IMAGE_REQUEST_DRIVER && resultCode == RESULT_OK){
            imageuri = data.getData();
            uploadDriverData();
        }

    }

    private void uploadDriverData() {
        if(imageuri != null )
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("RequestForAddBus");
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading");
            progressDialog.show();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            String driverFileName = UUID.randomUUID().toString()+"."+ getFileExtension(imageuri);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("DriverLicense").child(driverFileName);
            storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            driverFileUrl = url;
                            Log.d("DownloadUrl" , url);
                            Toast.makeText(AddDriverActivity.this,"Image Upload Successful",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            });
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
            progressDialog.setMessage("Uploading");
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
                            Toast.makeText(AddDriverActivity.this,"Image Upload Successful",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }
    }




}