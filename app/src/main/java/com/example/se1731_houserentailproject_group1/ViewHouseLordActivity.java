package com.example.se1731_houserentailproject_group1;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.User;

public class ViewHouseLordActivity extends AppCompatActivity {
    private TextView ownerName, ownerPhone, ownerEmail;
    private DatabaseHelper databaseHelper;
    private int ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_houselord);

        databaseHelper = new DatabaseHelper(this);
        ownerId = getIntent().getIntExtra("ownerId", -1);

        initializeViews();
        loadOwnerDetails();
    }

    private void initializeViews() {
        ownerName = findViewById(R.id.owner_name);
        ownerPhone = findViewById(R.id.owner_phone);
        ownerEmail = findViewById(R.id.owner_email);
    }

    private void loadOwnerDetails() {
        User owner = databaseHelper.getAccount(ownerId);
        if (owner != null) {
            ownerName.setText(owner.getFullName());
            ownerPhone.setText(owner.getPhoneNumber());
            ownerEmail.setText(owner.getEmail());
        } else {
            ownerName.setText("No details found");
        }
    }
}
