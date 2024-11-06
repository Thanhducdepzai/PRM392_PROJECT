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
import com.example.se1731_houserentailproject_group1.Utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class AddHouseActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText etPropertyName, etPropertyAddress, etPropertyCity, etPropertyState, etPropertyPostalCode,
            etPropertyMainPhone, etPropertyFaxNumber, etPropertyUnitCount, etPropertyType;
    private Button btnChooseImage, btnAddProperty;
    private ImageView imagePreview;
    private String base64Image = "";
    private int ownerId;

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
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnAddProperty = findViewById(R.id.btnAddProperty);
        imagePreview = findViewById(R.id.imagePreview);

        // Retrieve the logged-in user's ID
        SessionManager sessionManager = new SessionManager(this);
        User loggedInUser = sessionManager.getUser();
        if (loggedInUser != null) {
            ownerId = loggedInUser.getId();
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

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
        // (Validation code remains the same)
        // ...
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

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("city", city);
        values.put("state", state);
        values.put("postal_code", postalCode);
        values.put("main_phone", mainPhone);
        values.put("fax_number", faxNumber);
        values.put("unit_count", unitCount);
        values.put("owner_id", ownerId);  // Set the owner ID as the logged-in user's ID
        values.put("property_type", propertyType);
        values.put("image_base64", base64Image);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.insert("properties", null, values);
        db.close();

        Toast.makeText(this, "Property Added Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}



