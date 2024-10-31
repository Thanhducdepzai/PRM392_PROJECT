package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

    private TextView nameTextView, addressTextView, cityTextView, stateTextView, postalCodeTextView, mainPhoneTextView, faxNumberTextView, unitCountTextView, propertyTypeTextView;
    private ImageView propertyImageView;
    private DatabaseHelper databaseHelper;
    private static final int PICK_IMAGE_REQUEST = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        cityTextView = findViewById(R.id.cityTextView);
        stateTextView = findViewById(R.id.stateTextView);
        postalCodeTextView = findViewById(R.id.postalCodeTextView);
        mainPhoneTextView = findViewById(R.id.mainPhoneTextView);
        faxNumberTextView = findViewById(R.id.faxNumberTextView);
        unitCountTextView = findViewById(R.id.unitCountTextView);
        propertyTypeTextView = findViewById(R.id.propertyTypeTextView);
        propertyImageView = findViewById(R.id.propertyImageView);

        databaseHelper = new DatabaseHelper(this);

        int propertyId = getIntent().getIntExtra("propertyId", -1);

        Property property;
        if (propertyId != -1) {
            // Load specific property
            property = databaseHelper.getPropertyById(propertyId);
        } else {
            // Load first property if no propertyId is provided
            property = databaseHelper.getFirstProperty();
        }

        if (property != null) {
            displayPropertyDetails(property);
         //   loadFirstPropertyImage(property.getId());
        } else {
            Toast.makeText(this, "No property found.", Toast.LENGTH_SHORT).show();
        }

        // Set up click listener for changing image
      //  propertyImageView.setOnClickListener(v -> openGallery());
    }
/*
    private void loadFirstPropertyImage(int propertyId) {
        PropertyImage propertyImage = databaseHelper.getFirstPropertyImage(propertyId);
        if (propertyImage != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(propertyImage.getImageUrl());
            propertyImageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "No images found for this property.", Toast.LENGTH_SHORT).show();
        }
    }
*/
    private void displayPropertyDetails(Property property) {
        nameTextView.setText(property.getName());
        addressTextView.setText("Địa Chỉ: " + property.getAddress());
        cityTextView.setText("Thành phố: " + property.getCity());
        stateTextView.setText("Tỉnh/Thành Phố: " + property.getState());
        postalCodeTextView.setText("Mã bưu chính: " + property.getPostalCode());
        mainPhoneTextView.setText("Số Điện Thoại Chính: " + property.getMainPhone());
        faxNumberTextView.setText("Số Fax: " + property.getFaxNumber());
        unitCountTextView.setText("Số Đơn Vị: " + String.valueOf(property.getUnitCount()));
        propertyTypeTextView.setText("Loại bất động sản: " + property.getPropertyType());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String imagePath = getRealPathFromURI(imageUri);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            propertyImageView.setImageBitmap(bitmap);
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }
/*
    private void loadPropertyImage(int propertyId) {
        List<PropertyImage> propertyImages = databaseHelper.getPropertyImagesByPropertyId(propertyId);
        if (!propertyImages.isEmpty()) {
            // Display the primary image or the first image found
            String imageUrl = propertyImages.get(0).getImageUrl();
            Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);
            propertyImageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "No images found for this property.", Toast.LENGTH_SHORT).show();
        }
    }
    */
}
