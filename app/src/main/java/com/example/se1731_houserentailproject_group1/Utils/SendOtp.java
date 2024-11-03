package com.example.se1731_houserentailproject_group1.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class SendOtp { // Renamed for clarity
    // Method to generate a 4-digit OTP
    public static String generateOTP() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }


    // Method to send the OTP via SMS
    public static void sendOTP(Context context, String phone, String otp) {
        // Check if SMS permission is granted
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(otp + " là mã OTP của bạn.");
            smsManager.sendMultipartTextMessage(phone, null, parts, null, null);
            Toast.makeText(context, "SMS đã được gửi thành công!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Improved error handling
            Toast.makeText(context, "Lỗi khi gửi SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
