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
    private Button btnedit, btnChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Khởi tạo các TextView
        fullname = findViewById(R.id.txfullname);
        phoneNum = findViewById(R.id.tvPhoneNum);
        email = findViewById(R.id.tvEmail);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Lấy vai trò người dùng từ session
            User user = sessionManager.getUser();
            if (user != null) {
                // Hiển thị thông tin User lên giao diện
                fullname.setText(user.getFullName());
                phoneNum.setText(user.getPhoneNumber());
                email.setText(user.getEmail());
            }
        }
    }
}