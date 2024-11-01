package com.example.se1731_houserentailproject_group1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.se1731_houserentailproject_group1.Adapter.UserAdapter;
import com.example.se1731_houserentailproject_group1.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, phoneNumberEditText, emailEditText, passwordEditText, rePasswordEditText;
    private UserAdapter userAdapter;
    private Button btnRedirectLogin;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo UserAdapter
        userAdapter = new UserAdapter(this);
        userAdapter.open();

        // Khởi tạo các EditText
        fullNameEditText = findViewById(R.id.fullName);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.Password);
        rePasswordEditText = findViewById(R.id.rePassword);

        // Thiết lập sự kiện cho nút Đăng ký
        Button btnRegister = findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnRedirectLogin = findViewById(R.id.btnRedirectLogin);
        btnRedirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String rePassword = rePasswordEditText.getText().toString().trim();

        // Kiểm tra mật khẩu
        if (!password.equals(rePassword)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Số điện thoại phải có 10 chữ số!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.contains("@")) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra email đã tồn tại chưa
        if (userAdapter.checkEmailExist(email)) {
            Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng User
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(password); // Mật khẩu sẽ được mã hóa trong UserAdapter
        user.setPhoneNumber(phoneNumber);
        user.setCreatedAt(getCurrentTimestamp());
        user.setUpdatedAt(getCurrentTimestamp());

        // Đăng ký người dùng
        long result = userAdapter.registerUser(user);
        if (result != -1) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            // Gửi OTP đến số điện thoại của người dùng
            otp = generateOTP();
            requestSmsPermissionAndSendOTP(phoneNumber, otp);
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestSmsPermissionAndSendOTP(String phone, String otp) {
        // Kiểm tra và yêu cầu quyền SEND_SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Nếu quyền chưa được cấp, yêu cầu quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);
        } else {
            // Nếu quyền đã được cấp, gửi OTP
            sendOTP(phone, otp);
        }
    }

    private String generateOTP() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userAdapter.close();
    }

    private void sendOTP(String phone, String otp) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(otp + " là mã OTP của bạn.");
            smsManager.sendMultipartTextMessage(phone, null, parts, null, null);
            Toast.makeText(this, "SMS đã được gửi thành công!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi gửi SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu quyền được cấp, gọi lại sendOTP với số điện thoại và OTP đã tạo
                sendOTP(phoneNumberEditText.getText().toString().trim(), otp);
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
