package com.example.se1731_houserentailproject_group1.Adapter;

import android.app.AlertDialog;
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

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.EditActivity;
import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.R;

import java.util.List;

// PropertyAdapter.java
public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private List<Property> propertyList;
    private DatabaseHelper databaseHelper;
    private Context context;

    public PropertyAdapter(List<Property> propertyList, DatabaseHelper databaseHelper, Context context) {
        this.propertyList = propertyList;
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

        // Lấy tên của chủ sở hữu
        String ownerName = databaseHelper.getOwnerName(property.getOwnerId());
        holder.propertyOwner.setText("Chủ sở hữu: " + ownerName);

        holder.propertyUnit.setText("Số Đơn Vị: " + property.getUnitCount());
        holder.propertyType.setText("Loại Bất Động Sản: " + property.getPropertyType());

        // Chuyển đổi image_base64 thành Bitmap
        byte[] decodedString = Base64.decode(property.getImageBase64(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageBase64.setImageBitmap(decodedByte);

        // Nút Edit
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("propertyId", property.getId());
            context.startActivity(intent);
        });

        // Nút Delete
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác Nhận Xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa nhà này không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        databaseHelper.deleteProperty(property.getId());
                        propertyList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView propertyName, propertyAddress, propertyCity, propertyState, propertyPostalCode, propertyPhone, propertyFax, propertyOwner, propertyUnit, propertyType;
        ImageView imageBase64;
        Button editButton, deleteButton;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyName = itemView.findViewById(R.id.property_name);
            propertyAddress = itemView.findViewById(R.id.property_address);
            propertyCity = itemView.findViewById(R.id.property_city);
            propertyState = itemView.findViewById(R.id.property_state);
            propertyPostalCode = itemView.findViewById(R.id.property_postal_code);
            propertyPhone = itemView.findViewById(R.id.property_phone);
            propertyFax = itemView.findViewById(R.id.property_fax);
            propertyOwner = itemView.findViewById(R.id.property_owner);
            propertyUnit = itemView.findViewById(R.id.property_unit);
            propertyType = itemView.findViewById(R.id.property_type);
            imageBase64 = itemView.findViewById(R.id.image_display);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
