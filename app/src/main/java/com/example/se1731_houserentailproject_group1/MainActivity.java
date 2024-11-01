package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private View loginForm;
    private View registerForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        loginForm = findViewById(R.id.loginForm);
        registerForm = findViewById(R.id.registerForm);

        // Buttons in login form
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // Buttons in registration form
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);

        // Go to Register Form
        btnGoToRegister.setOnClickListener(v -> {
            loginForm.setVisibility(View.GONE);
            registerForm.setVisibility(View.VISIBLE);
        });

        // Go back to Login Form
        btnGoToLogin.setOnClickListener(v -> {
            registerForm.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
        });

        // Handle Login button click (no authentication for now)
        btnLogin.setOnClickListener(v -> {
            // Navigate to HouseListActivity
            Intent intent = new Intent(MainActivity.this, HouseListActivity.class);
            startActivity(intent);
        });

        // Handle Register button click (you can add your own registration logic here)
        btnRegister.setOnClickListener(v -> {
            // Show a message, e.g., "Registered successfully!"
            // Then switch back to login form
            registerForm.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
        });
    }
}
