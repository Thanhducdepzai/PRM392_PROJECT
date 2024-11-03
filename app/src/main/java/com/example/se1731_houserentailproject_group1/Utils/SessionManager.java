package com.example.se1731_houserentailproject_group1.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.se1731_houserentailproject_group1.Model.User;
import com.google.gson.Gson;

public class SessionManager {
    // Khai báo biến để lưu trữ SharedPreferences và Editor
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;
    private static final String KEY_USER = "user";

    // Tên của SharedPreferences và các khóa để lưu trữ thông tin phiên làm việc
    private static final String PREF_NAME = "UserSession"; // Tên của SharedPreferences
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; // Khóa để kiểm tra xem người dùng đã đăng nhập chưa
    private static final String KEY_USER_TYPE = "userType"; // Khóa để lưu trữ loại người dùng ("user" hoặc "admin")

    // Constructor để khởi tạo SessionManager với Context
    public SessionManager(Context context) {
        this.context = context;
        // Khởi tạo SharedPreferences với tên được định nghĩa
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Khởi tạo Editor để chỉnh sửa SharedPreferences
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Phương thức tạo phiên làm việc khi người dùng đăng nhập
    public void createSession(String userType) {
        // Lưu trạng thái đăng nhập
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        // Lưu loại người dùng (user hoặc admin)
        editor.putString(KEY_USER_TYPE, userType);
        // Áp dụng các thay đổi
        editor.apply();
    }

    // Phương thức kiểm tra xem người dùng đã đăng nhập chưa
    public boolean isLoggedIn() {
        // Trả về giá trị của KEY_IS_LOGGED_IN, mặc định là false
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Phương thức lấy loại người dùng từ SharedPreferences
    public String getUserType() {
        // Trả về giá trị của KEY_USER_TYPE, mặc định là null nếu không có
        return sharedPreferences.getString(KEY_USER_TYPE, null);
    }

    // Phương thức để người dùng đăng xuất
    public void logout() {
        // Xóa tất cả dữ liệu trong SharedPreferences
        editor.clear();
        // Áp dụng thay đổi
        editor.apply();
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        // Chuyển JSON thành đối tượng User
        return gson.fromJson(userJson, User.class);
    }
}
