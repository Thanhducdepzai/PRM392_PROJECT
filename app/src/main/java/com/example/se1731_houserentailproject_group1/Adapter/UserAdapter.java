package com.example.se1731_houserentailproject_group1.Adapter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import com.example.se1731_houserentailproject_group1.DatabaseHelper.DatabaseHelper;
import com.example.se1731_houserentailproject_group1.Model.User;

import org.mindrot.jbcrypt.BCrypt;

public class UserAdapter {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public UserAdapter(Context context) {
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Đăng ký người dùng
    public long registerUser(User user) {
        // Mã hóa mật khẩu trước khi lưu
        String passwordHash = hashPassword(user.getPasswordHash());
        user.setPasswordHash(passwordHash); // Cập nhật mật khẩu đã mã hóa vào đối tượng User

        ContentValues values = new ContentValues();
        values.put("full_name", user.getFullName());
        values.put("email", user.getEmail());
        values.put("password_hash", user.getPasswordHash());
        values.put("phone_number", user.getPhoneNumber());
        values.put("created_at", user.getCreatedAt());
        values.put("updated_at", user.getUpdatedAt());
        values.put("Roles", "User");

        return database.insert("users", null, values);
    }

    public boolean updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put("full_name", user.getFullName());
        values.put("phone_number", user.getPhoneNumber());
        values.put("email", user.getEmail());

        String whereClause = "id=?";
        String[] whereArgs = { String.valueOf(user.getId()) };

        int rowsUpdated = database.update("users", values, whereClause, whereArgs);
        return rowsUpdated > 0;
    }

    // Đăng nhập người dùng
    public User getUserByEmailAndPassword(String email, String password) {
        // Lấy người dùng theo email
        String query = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = database.rawQuery(query, new String[] {email});

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0), // id
                    cursor.getString(1), // full_name
                    cursor.getString(2), // email
                    cursor.getString(3), // password_hash
                    cursor.getString(4), // phone_number
                    cursor.getString(5), // created_at
                    cursor.getString(6), // updated_at
                    cursor.getString(7) // roles
            );

            // So sánh mật khẩu người dùng nhập với mật khẩu đã mã hóa trong cơ sở dữ liệu
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                // Mật khẩu khớp
                return user;
            } else {
                // Mật khẩu không khớp
                return null;
            }
        }

        // Đảm bảo đóng con trỏ
        if (cursor != null) {
            cursor.close();
        }

        return null; // Người dùng không tồn tại
    }

    public User getUserByPhoneNumber(String phoneNumber) {
        String query = "SELECT * FROM users WHERE phone_number = ?";
        Cursor cursor = database.rawQuery(query, new String[]{phoneNumber});

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0), // id
                    cursor.getString(1), // full_name
                    cursor.getString(2), // email
                    cursor.getString(3), // password_hash
                    cursor.getString(4), // phone_number
                    cursor.getString(5), // created_at
                    cursor.getString(6), // updated_at
                    cursor.getString(7) // roles
            );
            cursor.close();
        }
        return user; // Trả về null nếu không tìm thấy người dùng
    }


    // Mã hóa mật khẩu
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Kiểm tra email đã tồn tại chưa
    public boolean checkEmailExist(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = database.rawQuery(query, new String[] {email});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true; // Email đã tồn tại
        }

        return false; // Email chưa tồn tại
    }
    //kiểm tra số điện thoại đã tồn tại chưa
    public boolean checkPhoneNumberExist(String phoneNumber) {
        String query = "SELECT * FROM users WHERE phone_number = ?";
        Cursor cursor = database.rawQuery(query, new String[] {phoneNumber});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true; // Số điện thoại đã tồn tại
        }

        return false; // Số điện thoại chưa tồn tại
    }
    // sửa lại mật khẩu người dùng
    public long updateUserPassword(User user) {
        // Mã hóa mật khẩu trước khi lưu
        String passwordHash = hashPassword(user.getPasswordHash());
        user.setPasswordHash(passwordHash); // Cập nhật mật khẩu đã mã hóa vào đối tượng User

        ContentValues values = new ContentValues();
        values.put("password_hash", user.getPasswordHash());
        values.put("updated_at", user.getUpdatedAt());

        return database.update("users", values, "id = ?", new String[] {String.valueOf(user.getId())});
    }
}
