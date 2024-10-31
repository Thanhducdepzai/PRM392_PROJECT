package com.example.se1731_houserentailproject_group1.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.EditActivity;
import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.Model.PropertyImage;
import com.example.se1731_houserentailproject_group1.R;

import java.util.List;

// PropertyAdapter.java
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private List<Property> propertyList;
    private List<PropertyImage> propertyImageList;
    private DatabaseHelper databaseHelper;
    private Context context;

    public PropertyAdapter(List<Property> propertyList, List<PropertyImage> propertyImageList, DatabaseHelper databaseHelper, Context context) {
        this.propertyList = propertyList;
        this.propertyImageList = propertyImageList;
        this.databaseHelper = databaseHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.propertyName.setText(property.getName());
        holder.propertyAddress.setText("Địa Chỉ: " + property.getAddress());
        holder.propertyCity.setText("Thành Phố: " + property.getCity());
        holder.propertyState.setText("Tỉnh/Thành Phố: " + property.getState());
        holder.propertyPostalCode.setText("Mã Bưu Chính: " + property.getPostalCode());
        holder.propertyPhone.setText("Số Điện Thoại Chính: " + property.getMainPhone());
        holder.propertyFax.setText("Số Fax: " + property.getFaxNumber());
        holder.propertyUnit.setText("Số Đơn Vị: " + property.getUnitCount());
        holder.propertyType.setText("Loại Bất Động Sản: " +  property.getPropertyType());

        PropertyImage propertyImage = propertyImageList.get(position);
        // Load ảnh
        PropertyImage image = databaseHelper.getPropertyImage(propertyImage.getId());
        if (image != null) {
            String imageUrl = image.getImageUrl();
            String resourceName = imageUrl.replace("@drawable/", "");
            int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
            if (resourceId != 0) {
                holder.propertyImage.setImageResource(resourceId);
            }
        }

        // Nút Edit
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("propertyId", property.getId());
            context.startActivity(intent);
        });

        // Nút Delete
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this house?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        databaseHelper.deleteProperty(property.getId());
                        propertyList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView propertyName;
        TextView propertyAddress; // Add this line
        TextView propertyCity; // Add this line
        TextView propertyState; // Add this line
        TextView propertyPostalCode; // Add this line
        TextView propertyPhone;
        TextView propertyFax;
        TextView propertyUnit;
        TextView propertyType;
        ImageView propertyImage;
        Button editButton, deleteButton;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyName = itemView.findViewById(R.id.property_name);
            propertyAddress = itemView.findViewById(R.id.property_address); // Initialize this
            propertyCity = itemView.findViewById(R.id.property_city);
            propertyState = itemView.findViewById(R.id.property_state);
            propertyPostalCode = itemView.findViewById(R.id.property_postal_code);
            propertyPhone = itemView.findViewById(R.id.property_phone);
            propertyFax = itemView.findViewById(R.id.property_fax);
            propertyUnit = itemView.findViewById(R.id.property_unit_count);
            propertyType = itemView.findViewById(R.id.property_type);
            propertyImage = itemView.findViewById(R.id.property_image);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}