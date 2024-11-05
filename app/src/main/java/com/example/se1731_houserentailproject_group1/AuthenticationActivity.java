package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.se1731_houserentailproject_group1.Adapter.UserAdapter;
import com.example.se1731_houserentailproject_group1.AdminActivity.DashboardActivity;
import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.Utils.SessionManager;

public class AuthenticationActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button btnRedirectRegister, btnForgotPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authentication);

        // Initialize UserAdapter and open database
        userAdapter = new UserAdapter(this);
        userAdapter.open();

        // Bind UI components
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        // Set up login button click event
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::handleLogin);

        sessionManager = new SessionManager(this);

        // Set up redirect to Register page
        btnRedirectRegister = findViewById(R.id.btnRedirectRegister);
        btnRedirectRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set up redirect to Forgot Password page
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Adjust padding for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void handleLogin(View view) {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        // Input validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check user credentials
        User user = userAdapter.getUserByEmailAndPassword(email, password);

        if (user != null) {
            if (user.getRoles().equals("Lock")) {
                Toast.makeText(this, "Tài khoản của bạn đã bị khóa!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Successful login
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            // Store user session
            sessionManager.createSession(user);

            Intent intent;
            if (user.getRoles().equals("Admin")) {
                intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("currentUser", user);
            } else {
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("currentUser", user);
            }
            startActivity(intent);
        } else {
            // Login failed
            Toast.makeText(this, "Thông tin đăng nhập không đúng. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userAdapter.close(); // Ensure database is closed
    }
}
