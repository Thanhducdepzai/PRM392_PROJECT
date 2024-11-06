package com.example.se1731_houserentailproject_group1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.se1731_houserentailproject_group1.Utils.SendOtp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText, phoneNumberEditText, emailEditText, passwordEditText, rePasswordEditText;
    private UserAdapter userAdapter;
    private Button btnRedirectLogin;
    private String otp;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupWindowInsets();

        userAdapter = new UserAdapter(this);
        userAdapter.open();

        initializeViews();
        setupButtonListeners();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        fullNameEditText = findViewById(R.id.fullName);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.Password);
        rePasswordEditText = findViewById(R.id.rePassword);
        btnRedirectLogin = findViewById(R.id.btnRedirectLogin);
    }

    private void setupButtonListeners() {
        Button btnRegister = findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(v -> registerUser());

        btnRedirectLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String rePassword = rePasswordEditText.getText().toString().trim();

        if (!isInputValid(fullName, phoneNumber, email, password, rePassword)) {
            return;
        }

        if (userAdapter.checkEmailExist(email)) {
            showToast("Email đã tồn tại!");
            return;
        }

        user = createUser(fullName, email, password, phoneNumber);
        otp = SendOtp.generateOTP();
        requestSmsPermissionAndSendOTP(phoneNumber, otp);
    }

    private boolean isInputValid(String fullName, String phoneNumber, String email, String password, String rePassword) {
        if (fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            showToast("Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        if (phoneNumber.length() != 10) {
            showToast("Số điện thoại phải có 10 chữ số!");
            return false;
        }
        if (!email.contains("@")) {
            showToast("Email không hợp lệ!");
            return false;
        }
        if (!password.equals(rePassword)) {
            showToast("Mật khẩu không khớp!");
            return false;
        }
        if(userAdapter.checkPhoneNumberExist(phoneNumber)){
            showToast("Số điện thoại đã tồn tại!");
            return false;
        }
        return true;
    }

    private User createUser(String fullName, String email, String password, String phoneNumber) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setPhoneNumber(phoneNumber);
        user.setCreatedAt(getCurrentTimestamp());
        user.setUpdatedAt(getCurrentTimestamp());
        user.setRoles("User");
        return user;
    }

    /**
     * Hàm này kiểm tra xem ứng dụng đã có quyền gửi SMS chưa. Nếu chưa,
     * nó sẽ yêu cầu quyền này từ người dùng.
     * Nếu đã có quyền, nó sẽ gửi OTP và chuyển hướng người dùng tới màn hình OTP.
     * @param phone Số điện thoại người dùng
     * @param otp Mã OTP
     */
    private void requestSmsPermissionAndSendOTP(String phone, String otp) {
        // Kiểm tra xem ứng dụng có quyền SEND_SMS hay không
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Nếu không có quyền, yêu cầu cấp quyền SEND_SMS từ người dùng
            // requestCode = 100 ở đây dùng để xác định yêu cầu cấp quyền sendSms
            // có thể dùng số nguyên khác nhưng phải là duy nhất
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);
        } else {
            sendOTP(phone, otp);
            redirectToOTPActivity();
        }
    }



    private String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void sendOTP(String phone, String otp) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(otp + " là mã OTP của bạn.");
            smsManager.sendMultipartTextMessage(phone, null, parts, null, null);
            showToast("SMS đã được gửi thành công!");
        } catch (Exception e) {
            showToast("Lỗi khi gửi SMS: " + e.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void redirectToOTPActivity() {
        Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
        intent.putExtra("OTP", otp);
        intent.putExtra("ActionType", "REGISTER");
        intent.putExtra("User", user);
        startActivity(intent);
        finish();
    }
    /**
     * Hàm này được gọi khi người dùng trả lời yêu cầu quyền của ứng dụng.
     * ví dụ: đồng ý hoặc từ chối cấp quyền) mà ứng dụng đã gửi.
     * @param requestCode Mã yêu cầu cấp quyền
     * @param permissions Mảng các quyền được yêu cầu
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendOTP(phoneNumberEditText.getText().toString().trim(), otp);
            redirectToOTPActivity();
        } else {
            showToast("Quyền bị từ chối!");
        }
    }
}
