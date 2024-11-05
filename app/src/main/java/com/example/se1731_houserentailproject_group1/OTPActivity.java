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
import com.example.se1731_houserentailproject_group1.Model.User;

public class OTPActivity extends AppCompatActivity {

    private EditText otpEditText;
    private String generatedOtp; // Mã OTP được tạo và gửi cho người dùng
    private UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpactivity); // Đảm bảo rằng bạn đã đặt tên tệp XML đúng

        setupWindowInsets();
        userAdapter = new UserAdapter(this);
        userAdapter.open();
        otpEditText = findViewById(R.id.otp_input);
        Button verifyOtpButton = findViewById(R.id.btn_submit_otp);
        final User user = (User) getIntent().getSerializableExtra("User"); // Nhận thông tin người dùng

        // Lấy mã OTP từ Intent nếu cần
        generatedOtp = getIntent().getStringExtra("OTP");

        verifyOtpButton.setOnClickListener(v -> verifyOtp(user));
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void verifyOtp(User user) {
        String enteredOtp = otpEditText.getText().toString().trim();

        if (enteredOtp.isEmpty()) {
            showToast("Vui lòng nhập mã OTP!");
            return;
        }

        if (enteredOtp.equals(generatedOtp)) {
            showToast("Mã OTP xác thực thành công!");

            // Get the action type from the intent
            String actionType = getIntent().getStringExtra("ActionType");

            if ("REGISTER".equals(actionType)) {
                // Lưu thông tin người dùng vào cơ sở dữ liệu
                if (userAdapter.registerUser(user) != -1) {
                    showToast("Đăng ký thành công!");
                    Intent intent = new Intent(OTPActivity.this, AuthenticationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast("Đăng ký thất bại. Vui lòng thử lại!");
                }
            } else if ("RESET_PASSWORD".equals(actionType)) {
                // Cập nhật mật khẩu cho người dùng
                if (userAdapter.updateUserPassword(user) != -1) {
                    showToast("Đặt lại mật khẩu thành công!");
                    Intent intent = new Intent(OTPActivity.this, ResetPasswordActivity.class); // Chuyển hướng đến trang đăng nhập
                    startActivity(intent);
                    finish();
                } else {
                    showToast("Đặt lại mật khẩu thất bại. Vui lòng thử lại!");
                }
            }
        } else {
            showToast("Mã OTP không chính xác!");
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
