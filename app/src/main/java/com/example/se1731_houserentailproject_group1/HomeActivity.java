package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1731_houserentailproject_group1.Adapter.HomeAdapter;
import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.Property;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
///////
        // Load the properties list
        loadProperties();

        // Set click listener for profile icon
        findViewById(R.id.profileIcon).setOnClickListener(v -> goToProfile());

        // Set click listener for logout text
        findViewById(R.id.logoutText).setOnClickListener(v -> logout());

        // Set click listener for "Xem danh sách nhà" button
        findViewById(R.id.viewHouseListButton).setOnClickListener(v -> goToHouseList());
    }

    private void loadProperties() {
        List<Property> properties = databaseHelper.getAllProperties();
        homeAdapter = new HomeAdapter(properties, this);
        recyclerView.setAdapter(homeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProperties(); // Refresh the list after returning from detail page
    }

    // Method to navigate to Profile Activity
    private void goToProfile() {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    // Method to handle logout logic
    private void logout() {
        // Implement your logout logic here, e.g., clearing user session
        finish(); // For simplicity, finish this activity to log out
    }

    // Method to navigate to House List Activity
    private void goToHouseList() {
        Intent intent = new Intent(HomeActivity.this, HouseListActivity.class);
        startActivity(intent);
    }
}

