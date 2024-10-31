package com.example.se1731_houserentailproject_group1;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.Model.PropertyImage;

public class EditActivity extends AppCompatActivity {
    private EditText propertyName, propertyAddress, propertyCity, propertyType;
    private ImageView propertyImage;
    private Button saveButton;
    private DatabaseHelper databaseHelper;
    private int propertyId = -1;
    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        databaseHelper = new DatabaseHelper(this);

        propertyName = findViewById(R.id.property_name);
        propertyAddress = findViewById(R.id.property_address);
        propertyCity = findViewById(R.id.property_city);
        propertyType = findViewById(R.id.property_type);
        propertyImage = findViewById(R.id.property_image);
        saveButton = findViewById(R.id.save_button);

        propertyId = getIntent().getIntExtra("propertyId", -1);
        if (propertyId != -1) {
            loadPropertyData(propertyId);
        }

        propertyImage.setOnClickListener(v -> selectImage());
        saveButton.setOnClickListener(v -> saveProperty());
    }

    private void loadPropertyData(int propertyId) {
        Property property = databaseHelper.getPropertyById(propertyId);
        if (property != null) {
            propertyName.setText(property.getName());
            propertyAddress.setText(property.getAddress());
            propertyCity.setText(property.getCity());
            propertyType.setText(property.getPropertyType());

            PropertyImage image = databaseHelper.getPropertyImage(propertyId);
            if (image != null && image.getImageUrl() != null) {
                selectedImageUri = Uri.parse(image.getImageUrl());
                propertyImage.setImageURI(selectedImageUri);
            }
        } else {
            Toast.makeText(this, "Property not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void selectImage() {
        // Example image selection code (modify as needed for your app)
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            propertyImage.setImageURI(selectedImageUri);
        }
    }

    private void saveProperty() {
        String name = propertyName.getText().toString().trim();
        String address = propertyAddress.getText().toString().trim();
        String city = propertyCity.getText().toString().trim();
        String type = propertyType.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("city", city);
        values.put("property_type", type);

        if (databaseHelper.updateProperty(propertyId, values)) {
            if (selectedImageUri != null) {
                databaseHelper.updatePropertyImage(propertyId, selectedImageUri.toString());
            }
            Toast.makeText(this, "Property updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update property", Toast.LENGTH_SHORT).show();
        }
    }
}
