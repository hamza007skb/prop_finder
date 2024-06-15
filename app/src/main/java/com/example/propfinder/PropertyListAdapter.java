package com.example.propfinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.propfinder.businessLogic.Property;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.PropertyViewHolder> {

    private ArrayList<PropertyModel> modelArrayList;
    private ArrayList<Property> properties;
    private Context context;

    public PropertyListAdapter(Context context, ArrayList<PropertyModel> modelArrayList, ArrayList<Property> properties) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.properties = properties;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(modelArrayList.get(position).getImageUrl()).into(holder.imageView);
        String price = "PRICE: " + modelArrayList.get(position).getPrice();
        holder.textView.setText(price);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < properties.size()) {
                    Property property = properties.get(position);
                    Intent intent = new Intent(context, DetailPropertyViewActivity.class);
                    String propertyJson = new Gson().toJson(property);
                    intent.putExtra("property", propertyJson);
                    context.startActivity(intent);
                } else {
                    Log.e("PropertyListAdapter", "Invalid position: " + position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        CardView card;
        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.action_image);
            textView = itemView.findViewById(R.id.price_txt);
            card = itemView.findViewById(R.id.property_card);
        }
    }
}
