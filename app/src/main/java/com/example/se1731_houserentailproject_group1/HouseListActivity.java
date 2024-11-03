package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1731_houserentailproject_group1.Adapter.PropertyAdapter;
import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.Property;

import java.util.ArrayList;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class HouseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PropertyAdapter propertyAdapter;
    private DatabaseHelper databaseHelper;
    private EditText searchField;
    private List<Property> allProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_list);

        // Initialize DatabaseHelper and RecyclerView
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize search field
        searchField = findViewById(R.id.search_field);

        // Load all properties initially
        loadProperties();

        // Set up search functionality
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPropertiesByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Add button listener for adding a new property
        findViewById(R.id.add_button).setOnClickListener(v -> {
            Intent intent = new Intent(HouseListActivity.this, AddHouseActivity.class);
            startActivity(intent);
        });
    }

    private void loadProperties() {
        // Load all properties from the database and store them in allProperties list
        allProperties = databaseHelper.getAllProperties();
        propertyAdapter = new PropertyAdapter(allProperties, databaseHelper, this);
        recyclerView.setAdapter(propertyAdapter);
    }

    private void filterPropertiesByName(String query) {
        List<Property> filteredList = new ArrayList<>();
        for (Property property : allProperties) {
            // Only add properties whose names match the search query
            if (property.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(property);
            }
        }

        // Update RecyclerView with the filtered list
        propertyAdapter = new PropertyAdapter(filteredList, databaseHelper, this);
        recyclerView.setAdapter(propertyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProperties(); // Refresh the list after returning to this activity
    }
}
