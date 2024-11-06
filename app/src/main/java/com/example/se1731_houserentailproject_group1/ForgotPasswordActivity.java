package com.example.se1731_houserentailproject_group1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.se1731_houserentailproject_group1.Adapter.UserAdapter;
import com.example.se1731_houserentailproject_group1.Model.User;
import com.example.se1731_houserentailproject_group1.Utils.SendOtp;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private Button btnSendOtp;
    private TextView tvBackToLogin;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        userAdapter = new UserAdapter(this);
        userAdapter.open();

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhoneNumber.getText().toString().trim();
                if (phone.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = userAdapter.getUserByPhoneNumber(phone);
                if (user != null) {
                    String otp = SendOtp.generateOTP(); // Tạo mã OTP
                    SendOtp.sendOTP(ForgotPasswordActivity.this, phone, otp);

                    // Chuyển sang OTPActivity và truyền thông tin user
                    Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivity.class);
                    intent.putExtra("user", user); // Truyền User object
                    intent.putExtra("OTP", otp);
                    intent.putExtra("ActionType", "RESET_PASSWORD");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userAdapter.close();
    }
}
