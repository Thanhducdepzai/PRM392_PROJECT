package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.Model.PropertyImage;

import java.io.File;
import java.util.List;

public class HouseDetailActivity extends AppCompatActivity {
    private TextView propertyName, propertyAddress, propertyCity, propertyState, propertyPostalCode,
            propertyPhone, propertyFax, propertyOwner, propertyUnit, propertyType;
    private ImageView propertyImage;
    private DatabaseHelper databaseHelper;
    private int propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        databaseHelper = new DatabaseHelper(this);
        propertyId = getIntent().getIntExtra("propertyId", -1);

        initializeViews();
        loadPropertyDetails();
    }

    private void initializeViews() {
        propertyName = findViewById(R.id.property_name);
        propertyAddress = findViewById(R.id.property_address);
        propertyCity = findViewById(R.id.property_city);
        propertyState = findViewById(R.id.property_state);
        propertyPostalCode = findViewById(R.id.property_postal_code);
        propertyPhone = findViewById(R.id.property_phone);
        propertyFax = findViewById(R.id.property_fax);
        propertyOwner = findViewById(R.id.property_owner);
        propertyUnit = findViewById(R.id.property_unit);
        propertyType = findViewById(R.id.property_type);
        propertyImage = findViewById(R.id.property_image);
    }

    private void loadPropertyDetails() {
        Property property = databaseHelper.getPropertyById(propertyId);
        if (property != null) {
            propertyName.setText(property.getName());
            propertyAddress.setText(property.getAddress());
            propertyCity.setText(property.getCity());
            propertyState.setText(property.getState());
            propertyPostalCode.setText(property.getPostalCode());
            propertyPhone.setText(property.getMainPhone());
            propertyFax.setText(property.getFaxNumber());
            propertyOwner.setText(databaseHelper.getOwnerName(property.getOwnerId()));
            propertyUnit.setText(String.valueOf(property.getUnitCount()));
            propertyType.setText(property.getPropertyType());

            byte[] decodedString = Base64.decode(property.getImageBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            propertyImage.setImageBitmap(decodedByte);
        } else {
            Toast.makeText(this, "Property not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}


