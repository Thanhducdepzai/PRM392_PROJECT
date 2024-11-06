package com.example.se1731_houserentailproject_group1;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.Model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity"; // Tag for Logcat
    private EditText propertyName, propertyAddress, propertyCity, propertyState, propertyPostalCode,
            propertyPhone, propertyFax, propertyUnit, propertyType;
    private Button saveButton, selectImageButton;
    private ImageView imagePreview;
    private DatabaseHelper databaseHelper;
    private int propertyId = -1;
    private Uri selectedImageUri;
    private String imageBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        databaseHelper = new DatabaseHelper(this);

        // Initialize fields
        propertyName = findViewById(R.id.property_name);
        propertyAddress = findViewById(R.id.property_address);
        propertyCity = findViewById(R.id.property_city);
        propertyState = findViewById(R.id.property_state);
        propertyPostalCode = findViewById(R.id.property_postal_code);
        propertyPhone = findViewById(R.id.property_phone);
        propertyFax = findViewById(R.id.property_fax);
        propertyUnit = findViewById(R.id.property_unit);
        propertyType = findViewById(R.id.property_type);
        selectImageButton = findViewById(R.id.select_image_button);
        imagePreview = findViewById(R.id.image_preview);
        saveButton = findViewById(R.id.save_button);

        // Load property data if editing an existing property
        propertyId = getIntent().getIntExtra("propertyId", -1);
        if (propertyId != -1) {
            loadPropertyData(propertyId);
        }

        // Set listeners
        selectImageButton.setOnClickListener(v -> selectImage());
        saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveProperty();
            }
        });
    }

    private void loadPropertyData(int propertyId) {
        Property property = databaseHelper.getPropertyById(propertyId);
        if (property != null) {
            propertyName.setText(property.getName());
            propertyAddress.setText(property.getAddress());
            propertyCity.setText(property.getCity());
            propertyState.setText(property.getState());
            propertyPostalCode.setText(property.getPostalCode());
            propertyPhone.setText(property.getMainPhone());
            propertyFax.setText(property.getFaxNumber());
            propertyUnit.setText(String.valueOf(property.getUnitCount()));
            propertyType.setText(property.getPropertyType());

            // Load image if available
            imageBase64 = property.getImageBase64();
            if (imageBase64 != null && !imageBase64.isEmpty()) {
                imagePreview.setImageBitmap(decodeBase64ToBitmap(imageBase64));
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    private boolean validateInputs() {
        String name = propertyName.getText().toString().trim();
        String address = propertyAddress.getText().toString().trim();
        String city = propertyCity.getText().toString().trim();
        String state = propertyState.getText().toString().trim();
        String postalCode = propertyPostalCode.getText().toString().trim();
        String phone = propertyPhone.getText().toString().trim();
        String fax = propertyFax.getText().toString().trim();
        String unitStr = propertyUnit.getText().toString().trim();
        String type = propertyType.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || type.isEmpty() || unitStr.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!postalCode.matches("\\d{5}")) {
            Toast.makeText(this, "Invalid postal code", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!phone.matches("\\+?\\d{10,}")) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!fax.isEmpty() && !fax.matches("\\+?\\d{10,}")) {
            Toast.makeText(this, "Invalid fax number", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Integer.parseInt(unitStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Unit count must be a valid number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    imageBase64 = encodeBitmapToBase64(bitmap);  // Convert to Base64
                    imagePreview.setImageBitmap(bitmap);  // Show image preview
                    Toast.makeText(this, "Image selected successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProperty() {
        // Fetch and trim input values from EditText fields
        String name = propertyName.getText().toString().trim();
        String address = propertyAddress.getText().toString().trim();
        String city = propertyCity.getText().toString().trim();
        String state = propertyState.getText().toString().trim();
        String postalCode = propertyPostalCode.getText().toString().trim();
        String phone = propertyPhone.getText().toString().trim();
        String fax = propertyFax.getText().toString().trim();
        String type = propertyType.getText().toString().trim();
        String unitStr = propertyUnit.getText().toString().trim();

        int unitCount;
        try {
            unitCount = Integer.parseInt(unitStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Unit count must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log input values for debugging
        Log.d(TAG, "Property Name: " + name);
        Log.d(TAG, "Address: " + address);
        Log.d(TAG, "City: " + city);
        Log.d(TAG, "State: " + state);
        Log.d(TAG, "Postal Code: " + postalCode);
        Log.d(TAG, "Phone: " + phone);
        Log.d(TAG, "Fax: " + fax);
        Log.d(TAG, "Unit Count: " + unitCount);
        Log.d(TAG, "Property Type: " + type);
        Log.d(TAG, "Image Base64: " + (imageBase64 != null ? "Available" : "Not Available"));

        // Prepare ContentValues for database update
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("city", city);
        values.put("state", state);
        values.put("postal_code", postalCode);
        values.put("main_phone", phone);
        values.put("fax_number", fax);
        values.put("unit_count", unitCount);
        values.put("property_type", type);
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            values.put("image_base64", imageBase64);
        }

        // Perform database update
        boolean success = databaseHelper.updateProperty(propertyId, values);
        if (success) {
            Toast.makeText(this, "Property updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Failed to update property", Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap decodeBase64ToBitmap(String base64) {
        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
