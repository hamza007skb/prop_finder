package com.example.propfinder;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.propfinder.Firebase.FirebaseDB;
import com.example.propfinder.businessLogic.Property;

import java.util.ArrayList;

public class PropertyList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<PropertyModel> list;
    private PropertyListAdapter adapter;
    private ArrayList<Property> properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_property_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        properties = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new PropertyListAdapter(this, list, properties);
        recyclerView.setAdapter(adapter);

        // Fetch properties from Firebase asynchronously
        fetchPropertiesFromFirebase();
    }

    private void fetchPropertiesFromFirebase() {
        FirebaseDB.getPropertiesFromDB(new FirebaseDB.PropertiesCallback() {
            @Override
            public void onPropertiesReceived(ArrayList<Property> props) {
                properties.clear();
                properties.addAll(props);

                // Update RecyclerView with PropertyModel objects
                updateRecyclerView();
            }

            @Override
            public void onCancelled(String error) {
                Log.e(TAG, "FirebaseDB.getPropertiesFromDB onCancelled: " + error);
                // Handle onCancelled if needed
            }
        });
    }

    private void updateRecyclerView() {
        list.clear();
        for (Property p : properties) {
            // Assuming getPropertyImages() returns ArrayList<String> of image URLs
            if (!p.getPropertyImages().isEmpty()) {
                list.add(new PropertyModel(p.getPropertyImages().get(0), Double.toString(p.getPROPERTY_PRICE())));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
