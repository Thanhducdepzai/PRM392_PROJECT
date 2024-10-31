// AddHouseActivity.java
package com.example.se1731_houserentailproject_group1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;

public class AddHouseActivity extends AppCompatActivity {

    private EditText propertyName, propertyAddress, propertyCity, propertyType;
    private ImageView propertyImage;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        propertyName = findViewById(R.id.property_name);
        propertyAddress = findViewById(R.id.property_address);
        propertyCity = findViewById(R.id.property_city);
        propertyType = findViewById(R.id.property_type);
        propertyImage = findViewById(R.id.property_image);
        saveButton = findViewById(R.id.save_button);

        dbHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHouse();
            }
        });
    }

    private void saveHouse() {
        String name = propertyName.getText().toString();
        String address = propertyAddress.getText().toString();
        String city = propertyCity.getText().toString();
        String type = propertyType.getText().toString();
        String imageUrl = "@drawable/default_image";  // Placeholder for image URL

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues propertyValues = new ContentValues();
        propertyValues.put("name", name);
        propertyValues.put("address", address);
        propertyValues.put("city", city);
        propertyValues.put("property_type", type);

        long propertyId = db.insert("properties", null, propertyValues);

        if (propertyId != -1) {
            ContentValues imageValues = new ContentValues();
            imageValues.put("property_id", propertyId);
            imageValues.put("image_url", imageUrl);
            imageValues.put("is_primary", true);
            db.insert("property_images", null, imageValues);

            Toast.makeText(this, "House added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding house", Toast.LENGTH_SHORT).show();
        }
    }
}
