package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.Utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView fullname, phoneNum, email;
    private Button btnEdit, btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Initialize TextViews and Buttons
        fullname = findViewById(R.id.tvFullName);
        phoneNum = findViewById(R.id.tvPhoneNum);
        email = findViewById(R.id.tvEmail);
        btnEdit = findViewById(R.id.btnEdit);
        btnChangePassword = findViewById(R.id.id_ChangePassword);

        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Retrieve user details from session and display
            User user = sessionManager.getUser();
            if (user != null) {
                fullname.setText(user.getFullName());
                phoneNum.setText(user.getPhoneNumber());
                email.setText(user.getEmail());
            }
        }

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);

            // Retrieve the user ID from the session and pass it to ChangePasswordActivity
            if (sessionManager.getUser() != null) {
                int userId = sessionManager.getUser().getId(); // Assuming getId() retrieves the correct user ID
                intent.putExtra("USER_ID", userId); // Pass user ID as an extra
            }

            startActivity(intent);
        });

        // Set up Edit Profile button click event (if needed)
        btnEdit.setOnClickListener(v -> {
            // Navigate to EditProfileActivity (assuming you have one)
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }
}
