package com.example.propfinder.Firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.propfinder.businessLogic.Location;
import com.example.propfinder.businessLogic.PROPERTY_STATUS;
import com.example.propfinder.businessLogic.PROPERTY_TYPE;
import com.example.propfinder.businessLogic.Property;
import com.example.propfinder.businessLogic.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirebaseDB {
    private static final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    public static void addUserToDB(FirebaseUser currUser, User user) {
        if (currUser != null) {
            db.getReference().child("USERS").child(currUser.getUid()).setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "added to database");
                            } else {
                                Log.d(TAG, "not added");
                            }
                        }
                    });
        } else {
            Log.d(TAG, "currUser is Null");
        }
    }
    public static void addProperty(Property property) {
        String key = db.getReference().child("PROPERTIES").push().getKey();
        if (key != null) {
            db.getReference().child("PROPERTIES").child(key).setValue(property)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Log.d(TAG, "Property Added to Database");
                            else
                                Log.d(TAG, "Error Adding Property to Database");
                        }
                    });
        } else
            Log.d(TAG, "Key for adding Property is NULL!!");
    }
    public interface PropertiesCallback {
        void onPropertiesReceived(ArrayList<Property> properties);

        void onCancelled(String error);
    }

    public static void getPropertiesFromDB(PropertiesCallback callback) {
        ArrayList<Property> properties = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("PROPERTIES");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                properties.clear(); // Clear previous data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Parse each Property from dataSnapshot and add to properties list
                    Property property = parseProperty(dataSnapshot);
                    if (property != null) {
                        properties.add(property);
                    }
                }
                callback.onPropertiesReceived(properties);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCancelled(error.getMessage());
            }
        });
    }

    private static Property parseProperty(DataSnapshot snapshot) {
        try {
            // Parse property fields from snapshot
            String city = getStringFromSnapshot(snapshot, "location/city");
            int postalCode = getIntFromSnapshot(snapshot, "location/postel_CODE");
            long lat = getLongFromSnapshot(snapshot, "location/latitude");
            long lng = getLongFromSnapshot(snapshot, "location/longitude");
            String noOfRooms = getStringFromSnapshot(snapshot, "noOfRooms");
            PROPERTY_STATUS status = getEnumFromSnapshot(snapshot, "propertyStatus", PROPERTY_STATUS.class);
            PROPERTY_TYPE type = getEnumFromSnapshot(snapshot, "propertyType", PROPERTY_TYPE.class);
            double area = getDoubleFromSnapshot(snapshot, "property_AREA");
            double price = getDoubleFromSnapshot(snapshot, "property_PRICE");
            ArrayList<String> propertyImgs = getListFromSnapshot(snapshot, "propertyImages");

            // Create Location object
            Location location = new Location(lng, lat, city, postalCode);

            // Create Property object
            return new Property("123@abc.com", price, type, status, area, location, noOfRooms, propertyImgs);

        } catch (Exception e) {
            Log.e(TAG, "Error parsing Property: " + e.getMessage(), e);
            return null;
        }
    }

    private static String getStringFromSnapshot(DataSnapshot snapshot, String path) {
        DataSnapshot childSnapshot = snapshot.child(path);
        return childSnapshot.exists() ? childSnapshot.getValue(String.class) : null;
    }

    // Helper method to safely retrieve Integer values from snapshot
    private static int getIntFromSnapshot(DataSnapshot snapshot, String path) {
        DataSnapshot childSnapshot = snapshot.child(path);
        return childSnapshot.exists() ? childSnapshot.getValue(Integer.class) != null ? childSnapshot.getValue(Integer.class) : 0 : 0;
    }

    // Helper method to safely retrieve Long values from snapshot
    private static long getLongFromSnapshot(DataSnapshot snapshot, String path) {
        DataSnapshot childSnapshot = snapshot.child(path);
        return childSnapshot.exists() ? childSnapshot.getValue(Long.class) != null ? childSnapshot.getValue(Long.class) : 0L : 0L;
    }

    // Helper method to safely retrieve Double values from snapshot
    private static double getDoubleFromSnapshot(DataSnapshot snapshot, String path) {
        DataSnapshot childSnapshot = snapshot.child(path);
        return childSnapshot.exists() ? childSnapshot.getValue(Double.class) != null ? childSnapshot.getValue(Double.class) : 0.0 : 0.0;
    }

    // Helper method to safely retrieve enum values from snapshot
    private static <T extends Enum<T>> T getEnumFromSnapshot(DataSnapshot snapshot, String path, Class<T> enumClass) {
        DataSnapshot childSnapshot = snapshot.child(path);
        if (childSnapshot.exists()) {
            String enumValue = childSnapshot.getValue(String.class);
            if (enumValue != null) {
                try {
                    return Enum.valueOf(enumClass, enumValue);
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Invalid enum value for " + enumClass.getSimpleName() + ": " + enumValue, e);
                }
            }
        }
        return null;
    }

    // Helper method to safely retrieve list values from snapshot
    private static ArrayList<String> getListFromSnapshot(DataSnapshot snapshot, String path) {
        ArrayList<String> list = new ArrayList<>();
        DataSnapshot childSnapshot = snapshot.child(path);
        if (childSnapshot.exists()) {
            for (DataSnapshot imgSnapshot : childSnapshot.getChildren()) {
                String imageUrl = imgSnapshot.getValue(String.class);
                if (imageUrl != null) {
                    list.add(imageUrl);
                }
            }
        }
        return list;
    }
}
