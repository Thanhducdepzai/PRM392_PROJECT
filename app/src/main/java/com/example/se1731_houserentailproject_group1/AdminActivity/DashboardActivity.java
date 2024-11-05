package com.example.se1731_houserentailproject_group1.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se1731_houserentailproject_group1.HouseListActivity;
import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.R;

public class DashboardActivity extends AppCompatActivity {
    private Button btnHouseManagement, btnAccountManagement, btnStatistic;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        // Ánh xạ các nút với ID từ layout
        btnHouseManagement = findViewById(R.id.btnHouseManagement);
        btnAccountManagement = findViewById(R.id.btnAccountManagement);
        btnStatistic = findViewById(R.id.btnStatistic);

        // Xử lý sự kiện nhấn nút House Management
        btnHouseManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, HouseListActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nhấn nút Account Management
        btnAccountManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AccountManagementActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nhấn nút Statistic
        btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });
    }
}
