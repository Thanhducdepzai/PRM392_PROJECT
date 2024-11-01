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

import com.example.se1731_houserentailproject_group1.Adapter.PropertyAdapter;
import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.Model.PropertyImage;

import java.util.List;

public class HouseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PropertyAdapter propertyAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_list);

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadProperties();

        findViewById(R.id.add_button).setOnClickListener(v -> {
            Intent intent = new Intent(HouseListActivity.this, AddHouseActivity.class);
            startActivity(intent);
        });
    }

    private void loadProperties() {
        List<Property> properties = databaseHelper.getAllProperties();
        propertyAdapter = new PropertyAdapter(properties, databaseHelper, this);
        recyclerView.setAdapter(propertyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProperties(); // Refresh the list after edit or delete
    }
}

