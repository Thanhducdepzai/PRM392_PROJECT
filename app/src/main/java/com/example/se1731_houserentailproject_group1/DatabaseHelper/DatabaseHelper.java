package com.example.se1731_houserentailproject_group1.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "houserental.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng USERS
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "full_name TEXT," +
                "email TEXT," +
                "password_hash TEXT," +
                "phone_number TEXT," +
                "created_at TEXT," +
                "updated_at TEXT," +
                "Roles TEXT)");

        // Tạo bảng PROPERTIES
        db.execSQL("CREATE TABLE properties (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "address TEXT," +
                "city TEXT," +
                "state TEXT," +
                "postal_code TEXT," +
                "main_phone TEXT," +
                "fax_number TEXT," +
                "unit_count INTEGER," +
                "owner_id INTEGER," +
                "property_type TEXT)");

        // Tạo bảng UNITS
        db.execSQL("CREATE TABLE units (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "property_id INTEGER," +
                "unit_number TEXT," +
                "room_count INTEGER," +
                "bathroom_count INTEGER," +
                "square_footage INTEGER," +
                "floor_plan TEXT)");

        // Tạo bảng GUESTS
        db.execSQL("CREATE TABLE guests (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT," +
                "last_name TEXT," +
                "phone_number TEXT," +
                "email TEXT," +
                "user_id INTEGER)");

        // Tạo bảng PAYMENTS
        db.execSQL("CREATE TABLE payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "booking_id INTEGER," +
                "payment_date TEXT," +
                "amount DECIMAL(18,2)," +
                "payment_method TEXT," +
                "status TEXT)");

        // Tạo bảng GUEST_BOOKINGS
        db.execSQL("CREATE TABLE guest_bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "unit_id INTEGER," +
                "guest_id INTEGER," +
                "start_date TEXT," +
                "end_date TEXT," +
                "status TEXT)");

        // Tạo bảng REVIEWS
        db.execSQL("CREATE TABLE reviews (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "property_id INTEGER," +
                "user_id INTEGER," +
                "rating INTEGER," +
                "comment TEXT," +
                "created_at TEXT)");

        // Tạo bảng PROPERTY_IMAGES
        db.execSQL("CREATE TABLE property_images (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "property_id INTEGER," +
                "image_url TEXT," +
                "is_primary BOOLEAN)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ nếu cần nâng cấp
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS properties");
        // Tiếp tục cho các bảng khác
        onCreate(db);
    }
}
