// AddHouseActivity.java
package com.example.se1731_houserentailproject_group1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class AddHouseActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText etPropertyName, etPropertyAddress, etPropertyCity, etPropertyState, etPropertyPostalCode,
            etPropertyMainPhone, etPropertyFaxNumber, etPropertyUnitCount, etPropertyType;
    private Spinner spinnerOwner;
    private Button btnChooseImage, btnAddProperty;
    private ImageView imagePreview;
    private String base64Image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        etPropertyName = findViewById(R.id.etPropertyName);
        etPropertyAddress = findViewById(R.id.etPropertyAddress);
        etPropertyCity = findViewById(R.id.etPropertyCity);
        etPropertyState = findViewById(R.id.etPropertyState);
        etPropertyPostalCode = findViewById(R.id.etPropertyPostalCode);
        etPropertyMainPhone = findViewById(R.id.etPropertyMainPhone);
        etPropertyFaxNumber = findViewById(R.id.etPropertyFaxNumber);
        etPropertyUnitCount = findViewById(R.id.etPropertyUnitCount);
        etPropertyType = findViewById(R.id.etPropertyType);
        spinnerOwner = findViewById(R.id.spinnerOwner);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnAddProperty = findViewById(R.id.btnAddProperty);
        imagePreview = findViewById(R.id.imagePreview);

        // Populate Spinner with owner list
        List<User> ownerList = databaseHelper.getOwnerListForSelector();
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ownerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOwner.setAdapter(adapter);

        // Set up image chooser
        btnChooseImage.setOnClickListener(v -> openImageChooser());

        // Add property to database
        btnAddProperty.setOnClickListener(v -> {
            if (validateInputs()) {
                insertProperty();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    private boolean validateInputs() {
        if (etPropertyName.getText().toString().trim().isEmpty()) {
            etPropertyName.setError("Property name is required");
            return false;
        }
        if (etPropertyAddress.getText().toString().trim().isEmpty()) {
            etPropertyAddress.setError("Address is required");
            return false;
        }
        if (etPropertyCity.getText().toString().trim().isEmpty()) {
            etPropertyCity.setError("City is required");
            return false;
        }
        if (etPropertyState.getText().toString().trim().isEmpty()) {
            etPropertyState.setError("State is required");
            return false;
        }
        if (!Pattern.matches("\\d{5}", etPropertyPostalCode.getText().toString())) {
            etPropertyPostalCode.setError("Postal code must be 5 digits");
            return false;
        }
        if (!Pattern.matches("\\d{10}", etPropertyMainPhone.getText().toString())) {
            etPropertyMainPhone.setError("Phone number must be 10 digits");
            return false;
        }
        if (!etPropertyFaxNumber.getText().toString().isEmpty() &&
                !Pattern.matches("\\d{10}", etPropertyFaxNumber.getText().toString())) {
            etPropertyFaxNumber.setError("Fax number must be 10 digits");
            return false;
        }
        try {
            int unitCount = Integer.parseInt(etPropertyUnitCount.getText().toString());
            if (unitCount < 1) {
                etPropertyUnitCount.setError("Unit count must be a positive integer");
                return false;
            }
        } catch (NumberFormatException e) {
            etPropertyUnitCount.setError("Unit count must be a number");
            return false;
        }
        if (etPropertyType.getText().toString().trim().isEmpty()) {
            etPropertyType.setError("Property type is required");
            return false;
        }
        if (spinnerOwner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select an owner", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imagePreview.setImageBitmap(bitmap);
                imagePreview.setVisibility(View.VISIBLE);
                base64Image = encodeToBase64(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private void insertProperty() {
        String name = etPropertyName.getText().toString();
        String address = etPropertyAddress.getText().toString();
        String city = etPropertyCity.getText().toString();
        String state = etPropertyState.getText().toString();
        String postalCode = etPropertyPostalCode.getText().toString();
        String mainPhone = etPropertyMainPhone.getText().toString();
        String faxNumber = etPropertyFaxNumber.getText().toString();
        int unitCount = Integer.parseInt(etPropertyUnitCount.getText().toString());
        String propertyType = etPropertyType.getText().toString();
        int ownerId = ((User) spinnerOwner.getSelectedItem()).getId();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("city", city);
        values.put("state", state);
        values.put("postal_code", postalCode);
        values.put("main_phone", mainPhone);
        values.put("fax_number", faxNumber);
        values.put("unit_count", unitCount);
        values.put("owner_id", ownerId);
        values.put("property_type", propertyType);
        values.put("image_base64", base64Image);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.insert("properties", null, values);
        db.close();

        Toast.makeText(this, "Property Added Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}


