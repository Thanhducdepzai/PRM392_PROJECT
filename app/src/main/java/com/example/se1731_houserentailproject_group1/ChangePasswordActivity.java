package com.example.se1731_houserentailproject_group1;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import org.mindrot.jbcrypt.BCrypt;
public class ChangePasswordActivity extends AppCompatActivity {
    private EditText currentPasswordEditText, newPasswordEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        currentPasswordEditText = findViewById(R.id.currentPassword);
        newPasswordEditText = findViewById(R.id.newPassword);
        Button saveButton = findViewById(R.id.saveButton);

        databaseHelper = new DatabaseHelper(this);

        // Retrieve user ID from intent extras
        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID not found. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        saveButton.setOnClickListener(v -> changePassword(userId));
    }

    private void changePassword(int userId) {
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();

        String hashedPassword = databaseHelper.getUserPasswordHash(userId); // Pass user ID here
        Log.d("ChangePasswordActivity", "Retrieved hashed password: " + hashedPassword);

        if (hashedPassword != null) {
            boolean passwordMatch = BCrypt.checkpw(currentPassword, hashedPassword);
            Log.d("ChangePasswordActivity", "Password match result: " + passwordMatch);

            if (passwordMatch) {
                String hashedNewPassword = hashPassword(newPassword);
                databaseHelper.updatePassword(userId, hashedNewPassword);
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("ChangePasswordActivity", "Failed to retrieve the hashed password.");
            Toast.makeText(this, "Error retrieving password. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }



    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}

