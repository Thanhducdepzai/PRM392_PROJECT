package com.example.se1731_houserentailproject_group1.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.Model.PropertyImage;

import java.util.ArrayList;
import java.util.List;

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
        // Insert sample data
        db.execSQL("INSERT INTO users (full_name, email, password_hash, phone_number, created_at, updated_at, Roles) VALUES " +
                "('John Doe', 'john.doe@example.com', 'password123', '1234567890', '2023-01-01', '2023-01-01', 'User')," +
                "('Jane Smith', 'jane.smith@example.com', 'password456', '0987654321', '2023-01-01', '2023-01-01', 'Admin')");

        db.execSQL("INSERT INTO properties (name, address, city, state, postal_code, main_phone, fax_number, unit_count, owner_id, property_type) VALUES " +
                "('Sunshine Apartments', '123 Main St', 'Los Angeles', 'CA', '90001', '1234567890', '1234567890', 10, 1, 'Apartment')," +
                "('Lakeside Villas', '456 Oak Ave', 'San Diego', 'CA', '92037', '0987654321', '0987654321', 15, 2, 'Villa')");

        db.execSQL("INSERT INTO units (property_id, unit_number, room_count, bathroom_count, square_footage, floor_plan) VALUES " +
                "(1, 'A1', 2, 1, 800, '2BHK')," +
                "(2, 'B2', 3, 2, 1200, '3BHK')");

        db.execSQL("INSERT INTO guests (first_name, last_name, phone_number, email, user_id) VALUES " +
                "('Alice', 'Johnson', '1112223333', 'alice.j@example.com', 1)," +
                "('Bob', 'Brown', '4445556666', 'bob.b@example.com', 2)");

        db.execSQL("INSERT INTO payments (booking_id, payment_date, amount, payment_method, status) VALUES " +
                "(1, '2023-05-01', 1000.00, 'Credit Card', 'Paid')," +
                "(2, '2023-06-01', 1200.00, 'PayPal', 'Pending')");

        db.execSQL("INSERT INTO guest_bookings (unit_id, guest_id, start_date, end_date, status) VALUES " +
                "(1, 1, '2023-07-01', '2023-07-10', 'Confirmed')," +
                "(2, 2, '2023-08-01', '2023-08-15', 'Confirmed')");

        db.execSQL("INSERT INTO reviews (property_id, user_id, rating, comment, created_at) VALUES " +
                "(1, 1, 5, 'Excellent place!', '2023-01-02')," +
                "(2, 2, 4, 'Nice and comfortable', '2023-01-02')");

        db.execSQL("INSERT INTO property_images (property_id, image_url, is_primary) VALUES " +
                "(1, '@drawable/house2.jpeg', 1)," +
                "(2, '@drawable/house3.jpeg', 0)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ nếu cần nâng cấp
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS properties");
        // Tiếp tục cho các bảng khác
        onCreate(db);
    }

    // Method to get all properties
    public List<Property> getAllProperties() {
        List<Property> properties = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM properties", null);
        if (cursor.moveToFirst()) {
            do {
                Property property = new Property(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("city")),
                        cursor.getString(cursor.getColumnIndexOrThrow("state")),
                        cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                        cursor.getString(cursor.getColumnIndexOrThrow("main_phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fax_number")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("unit_count")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("owner_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("property_type"))
                );
                properties.add(property);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return properties;
    }

    // Method to get all image properties of the house
    public List<PropertyImage> getAllImageProperties() {
        List<PropertyImage> imageProperties = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the property_images table for all columns
        Cursor cursor = db.rawQuery("SELECT * FROM property_images", null);

        if (cursor.moveToFirst()) {
            do {
                // Create a PropertyImage object using appropriate columns
                PropertyImage propertyImage = new PropertyImage(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("property_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_primary")) == 1 // Convert to boolean
                );
                imageProperties.add(propertyImage);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return imageProperties;
    }


    // Method to get property image by property ID
    public PropertyImage getPropertyImage(int propertyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM property_images WHERE property_id = ? AND is_primary = 1", new String[]{String.valueOf(propertyId)});
        if (cursor.moveToFirst()) {
            return new PropertyImage(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("property_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("is_primary")) == 1
            );
        }
        cursor.close();
        return null;
    }

    public boolean updateProperty(int propertyId, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.update("properties", values, "id = ?", new String[]{String.valueOf(propertyId)});
        return result > 0;
    }

    public void updatePropertyImage(int propertyId, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image_url", imageUrl);
        db.update("property_images", values, "property_id = ? AND is_primary = 1", new String[]{String.valueOf(propertyId)});
    }
    // Method to delete property
    public void deleteProperty(int propertyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("properties", "id = ?", new String[]{String.valueOf(propertyId)});
        db.delete("property_images", "property_id = ?", new String[]{String.valueOf(propertyId)});
        db.close();
    }


    public Property getPropertyById(int propertyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Property property = null;

        try {
            cursor = db.rawQuery("SELECT * FROM properties WHERE id = ?", new String[]{String.valueOf(propertyId)});

            // Kiểm tra nếu cursor không rỗng và di chuyển đến hàng đầu tiên thành công
            if (cursor != null && cursor.moveToFirst()) {
                property = new Property(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("city")),
                        cursor.getString(cursor.getColumnIndexOrThrow("state")),
                        cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                        cursor.getString(cursor.getColumnIndexOrThrow("main_phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fax_number")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("unit_count")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("owner_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("property_type"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return property;
    }

    // Add this method to fetch the first Property in the properties table
    public Property getFirstProperty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Property property = null;

        try {
            cursor = db.rawQuery("SELECT * FROM properties WHERE id = 2", null);
            if (cursor != null && cursor.moveToFirst()) {
                property = new Property(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("city")),
                        cursor.getString(cursor.getColumnIndexOrThrow("state")),
                        cursor.getString(cursor.getColumnIndexOrThrow("postal_code")),
                        cursor.getString(cursor.getColumnIndexOrThrow("main_phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fax_number")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("unit_count")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("owner_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("property_type"))
                );
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return property;
    }

    public PropertyImage getFirstPropertyImage(int propertyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        PropertyImage propertyImage = null;

        try {
            cursor = db.rawQuery("SELECT * FROM property_images WHERE property_id = ? ORDER BY id LIMIT 1",
                    new String[]{String.valueOf(propertyId)});
            if (cursor != null && cursor.moveToFirst()) {
                propertyImage = new PropertyImage(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("property_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_primary")) > 0
                );
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return propertyImage;
    }

    public List<PropertyImage> getPropertyImagesByPropertyId(int propertyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<PropertyImage> propertyImages = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM property_images WHERE property_id = ?", new String[]{String.valueOf(propertyId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    PropertyImage propertyImage = new PropertyImage(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("property_id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("is_primary")) > 0 // Chuyển đổi boolean từ 0/1
                    );
                    propertyImages.add(propertyImage);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return propertyImages;

    }
}
