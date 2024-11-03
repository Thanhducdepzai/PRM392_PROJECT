package com.example.se1731_houserentailproject_group1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1731_houserentailproject_group1.HouseDetailActivity;
import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private List<Property> propertyList;
    private Context context;

    public HomeAdapter(List<Property> propertyList, Context context) {
        this.propertyList = propertyList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.propertyName.setText(property.getName());
        holder.propertyAddress.setText(property.getAddress());

        // Decode base64 image and display
        byte[] decodedString = Base64.decode(property.getImageBase64(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageBase64.setImageBitmap(decodedByte);

        // Details button click listener
        holder.detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, HouseDetailActivity.class);
            intent.putExtra("propertyId", property.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView propertyName, propertyAddress;
        ImageView imageBase64;
        Button detailsButton;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyName = itemView.findViewById(R.id.property_name);
            propertyAddress = itemView.findViewById(R.id.property_address);
            imageBase64 = itemView.findViewById(R.id.image_display);
            detailsButton = itemView.findViewById(R.id.details_button);
        }
    }
}
