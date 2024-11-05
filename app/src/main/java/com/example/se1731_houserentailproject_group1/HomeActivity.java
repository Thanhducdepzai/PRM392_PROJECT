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

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private DatabaseHelper databaseHelper;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    // Method to handle logout logic
    private void logout() {
        // Implement your logout logic here, e.g., clearing user session
        finish(); // For simplicity, finish this activity to log out
    }

    // Method to navigate to House List Activity
    private void goToHouseList() {
        // Đảm bảo currentUser không null trước khi truyền
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

