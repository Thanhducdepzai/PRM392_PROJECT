package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.Utils.SessionManager;
import com.example.se1731_houserentailproject_group1.Adapter.UserAdapter;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editFullName, editPhoneNumber, editEmail;
    private Button btnSave;
    private SessionManager sessionManager;
    private UserAdapter userAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI components
        editFullName = findViewById(R.id.editFullName);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editEmail = findViewById(R.id.editEmail);
        btnSave = findViewById(R.id.btnSave);

        // Initialize SessionManager and UserAdapter
        sessionManager = new SessionManager(this);
        userAdapter = new UserAdapter(this);
        userAdapter.open();

        // Get the current user from session
        user = sessionManager.getUser();
        if (user != null) {
            // Populate the fields with the user's current data
            editFullName.setText(user.getFullName());
            editPhoneNumber.setText(user.getPhoneNumber());
            editEmail.setText(user.getEmail());
        }

        // Set up the Save button to handle profile updates
        btnSave.setOnClickListener(this::handleSave);

    }

    private void handleSave(View view) {
        // Get the updated data from input fields
        String newFullName = editFullName.getText().toString().trim();
        String newPhoneNumber = editPhoneNumber.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();

        // Check for empty fields
        if (newFullName.isEmpty() || newPhoneNumber.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the user object
        user.setFullName(newFullName);
        user.setPhoneNumber(newPhoneNumber);
        user.setEmail(newEmail);

        // Update the user in the database
        boolean isUpdated = userAdapter.updateUser(user);

        if (isUpdated) {
            // Update the session with new user data
            sessionManager.updateSession(user);

            // Notify the user and redirect to the profile page
            Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userAdapter.close(); // Ensure the database is closed
    }
}
