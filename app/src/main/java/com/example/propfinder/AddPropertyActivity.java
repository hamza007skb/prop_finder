package com.example.propfinder;


import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.propfinder.Firebase.DatabaseStorage;
import com.example.propfinder.Firebase.FirebaseDB;
import com.example.propfinder.Firebase.FirebaseSignIn;
import com.example.propfinder.businessLogic.Location;
import com.example.propfinder.businessLogic.PROPERTY_STATUS;
import com.example.propfinder.businessLogic.PROPERTY_TYPE;
import com.example.propfinder.businessLogic.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AddPropertyActivity extends AppCompatActivity {

    private final String[] PROPERTY_TYPES = {"House", "Apartment", "Plot"};
    private final String[] PROPERTY_STATUS = {"For Sale", "Rental"};
    private final String[] PROPERTY_AREA = {"3 Marla", "5 Marla", "7 Marla", "10 Marla", "12 Marla", "15 Marla", "20 Marla"};
    private ArrayAdapter<String> propertyTypeItems, propertyStatusItems, propertyAreaItems;
    private AutoCompleteTextView propertyTypeAutoComplete, propertyStatusAutoComplete, propertyAreaAutoComplete;
    private EditText PropertyPrice, Longitude, Latitude, City, PostalCode, NoOfRooms;
    private Button attachImgsBtn;
    double propertyArea;
    private PROPERTY_TYPE propertyType;
    private PROPERTY_STATUS propertyStatus;
    private ActivityResultLauncher<String[]> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_property);

        PropertyPrice = findViewById(R.id.price_txt);
        Longitude = findViewById(R.id.longitude_txt);
        Latitude = findViewById(R.id.latitude_txt);
        City = findViewById(R.id.city_txt);
        PostalCode = findViewById(R.id.postalCode_txt);
        NoOfRooms = findViewById(R.id.roomCount_txt);
        attachImgsBtn = findViewById(R.id.attachImg_btn);

        launcher = registerForActivityResult(
                new ActivityResultContracts.OpenMultipleDocuments(),
                new ActivityResultCallback<List<Uri>>() {
                    @Override
                    public void onActivityResult(List<Uri> uris) {
                        handleImageSelectionAndUpload(uris);
                    }
                }
        );

        attachImgsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });

        propertyTypeAutoComplete = findViewById(R.id.propertyType_auto_complete_txt);
        propertyTypeItems = new ArrayAdapter<>(this, R.layout.list_item, PROPERTY_TYPES);
        propertyTypeAutoComplete.setAdapter(propertyTypeItems);
        propertyTypeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                propertyType = Property.propertyTypeSetter(parent.getItemAtPosition(position).toString());
            }
        });

        propertyStatusAutoComplete = findViewById(R.id.propertyStatus_auto_compplete_txt);
        propertyStatusItems = new ArrayAdapter<>(this, R.layout.list_item, PROPERTY_STATUS);
        propertyStatusAutoComplete.setAdapter(propertyStatusItems);
        propertyStatusAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                propertyStatus = Property.propertyStatusSetter(parent.getItemAtPosition(position).toString());
            }
        });

        propertyAreaAutoComplete = findViewById(R.id.propertyArea_auto_complete_txt);
        propertyAreaItems = new ArrayAdapter<>(this, R.layout.list_item, PROPERTY_AREA);
        propertyAreaAutoComplete.setAdapter(propertyAreaItems);
        propertyAreaAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                propertyArea = calculateArea(parent.getItemAtPosition(position).toString());
            }
        });
    }
    private void selectImg() {
        String[] mimeTypes = {"image/*"};
        launcher.launch(mimeTypes);
    }
    private double calculateArea(String area){
        if (area != null && !area.isEmpty()){
            String[] parts = area.split(" ");
            double marla = Double.parseDouble(parts[0]);
            return marla * 272.25;
        }
        return -1;
    }
    private void handleImageSelectionAndUpload(List<Uri> uris) {
        if (uris == null || uris.isEmpty()) {
            return;
        }

        List<Uri> uploadedUris = new ArrayList<>();
        AtomicInteger uploadCounter = new AtomicInteger(uris.size());

        for (Uri uri : uris) {
            DatabaseStorage.storeImage(uri, new DatabaseStorage.UploadCallback() {
                @Override
                public void onUploadSuccess(Uri uri) {
                    synchronized (uploadedUris) {
                        uploadedUris.add(uri);
                        if (uploadCounter.decrementAndGet() == 0) {
                            proceedWithPropertyUpload(uploadedUris);
                        }
                    }
                }

                @Override
                public void onUploadFailure(Exception e) {
                    Log.e(TAG, "Upload failed", e);
                    if (uploadCounter.decrementAndGet() == 0) {
                        proceedWithPropertyUpload(uploadedUris);
                    }
                }
            });
        }
    }

    private void proceedWithPropertyUpload(List<Uri> uploadedUris) {
        runOnUiThread(() -> {
            String propertyPrice_txt, long_txt, lat_txt, city_txt, postalCode_txt, noOfRooms_txt;
            propertyPrice_txt = PropertyPrice.getText().toString();
            long_txt = Longitude.getText().toString();
            lat_txt = Latitude.getText().toString();
            city_txt = City.getText().toString();
            postalCode_txt = PostalCode.getText().toString();
            noOfRooms_txt = NoOfRooms.getText().toString();

            if (!propertyPrice_txt.isEmpty() && !long_txt.isEmpty() && !lat_txt.isEmpty() &&
                    !city_txt.isEmpty() && !postalCode_txt.isEmpty() && !noOfRooms_txt.isEmpty()) {
                Location location = new Location(Long.parseLong(long_txt), Long.parseLong(lat_txt), city_txt, Integer.parseInt(postalCode_txt));

                String email = FirebaseSignIn.getEmail();
                if (email != null) {
                    ArrayList<String> storageUris = new ArrayList<>();
                    for (Uri uri : uploadedUris) {
                        storageUris.add(uri.toString());
                    }

                    Property property = new Property(email, Double.parseDouble(propertyPrice_txt), propertyType, propertyStatus, propertyArea, location, noOfRooms_txt, storageUris);
                    FirebaseDB.addProperty(property);
                } else {
                    Toast.makeText(getApplicationContext(), "Not Signed In", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter all Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
