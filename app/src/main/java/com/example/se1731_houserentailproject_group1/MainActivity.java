package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chuyển hướng đến AuthenticationActivity
        Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
        startActivity(intent);

        // Kết thúc MainActivity để không quay lại
        finish();
    }
}
