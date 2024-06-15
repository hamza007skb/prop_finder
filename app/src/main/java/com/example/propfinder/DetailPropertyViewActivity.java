package com.example.propfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.propfinder.businessLogic.Property;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DetailPropertyViewActivity extends AppCompatActivity {
    private ImageSlider imageSlider;
    private TextView detail;
    private Button showOnMapBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_property_view);

        detail = findViewById(R.id.detail_txt);
        showOnMapBtn = findViewById(R.id.map_btn);

        imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        String propertyJson = getIntent().getStringExtra("property");
        Property property = new Gson().fromJson(propertyJson, Property.class);

        for (String uri: property.getPropertyImages()){
            slideModels.add(new SlideModel(uri, ScaleTypes.FIT));
        }
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        detail.setText(property.toString());

        showOnMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPropertyViewActivity.this, MapsActivity.class);
                intent.putExtra("LAT", property.getLocation().getLATITUDE());
                intent.putExtra("LONG", property.getLocation().getLONGITUDE());
                startActivity(intent);
            }
        });

    }
}