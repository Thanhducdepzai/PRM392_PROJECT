package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.Utils.SessionManager;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize SessionManager and get current user
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getUser();

        // Check if the user is logged in
        if (currentUser == null) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components and load properties
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadProperties();

        // Set up UI event listeners
        findViewById(R.id.profileIcon).setOnClickListener(v -> goToProfile());
        findViewById(R.id.logoutText).setOnClickListener(v -> logout());
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
        loadProperties(); // Refresh list when returning from another activity
    }

    // Navigate to Profile Activity
    private void goToProfile() {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    // Handle logout logic
    private void logout() {
        sessionManager.logout(); // Clear session data
        finish(); // Close activity to "log out" user
    }

    // Navigate to House List Activity
    private void goToHouseList() {
        if (currentUser != null) {
            Intent intent = new Intent(HomeActivity.this, HouseListActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        } else {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}


