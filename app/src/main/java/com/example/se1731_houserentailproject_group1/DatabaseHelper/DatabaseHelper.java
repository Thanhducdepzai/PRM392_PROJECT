package com.example.se1731_houserentailproject_group1.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.se1731_houserentailproject_group1.Model.Property;
import com.example.se1731_houserentailproject_group1.Model.PropertyImage;
import com.example.se1731_houserentailproject_group1.Model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "houserental.db";
    private static final int DATABASE_VERSION = 4;

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
                "property_type TEXT," +
                "image_base64 TEXT)");

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

        // Add the property_images table creation
        db.execSQL("CREATE TABLE property_images (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "property_id INTEGER," +
                "image_url TEXT," +
                "is_primary INTEGER," +
                "FOREIGN KEY (property_id) REFERENCES properties(id))");

        // Insert sample data
        db.execSQL("INSERT INTO users (full_name, email, password_hash, phone_number, created_at, updated_at, Roles) VALUES " +
                "('John Doe', 'john.doe@example.com', '$2a$10$AXVAwE32Zs1jmxN/P3BZ1.m/KjqDQV0qXrjkHakQffiW7q/YcGSMe', '1234567890', '2023-01-01', '2023-01-01', 'User')," +
                "('Jane Smith', 'jane.smith@example.com', 'password456', '0987654321', '2023-01-01', '2023-01-01', 'Admin')");

        db.execSQL("INSERT INTO properties (name, address, city, state, postal_code, main_phone, fax_number, unit_count, owner_id, property_type, image_base64) VALUES " +
                "('Sunshine Apartments', '123 Main St', 'Los Angeles', 'CA', '90001', '1234567890', '1234567890', 10, 1, 'Apartment','' )," +
                "('Lakeside Villas', '456 Oak Ave', 'San Diego', 'CA', '92037', '0987654321', '0987654321', 15, 2, 'Villa', '')");

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
                "(1, '@drawable/house1.jpeg', 1)," +
                "(2, '@drawable/house2.jpeg', 0)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ nếu cần nâng cấp
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS properties");
        db.execSQL("DROP TABLE IF EXISTS units");
        db.execSQL("DROP TABLE IF EXISTS guests");
        db.execSQL("DROP TABLE IF EXISTS payments");
        db.execSQL("DROP TABLE IF EXISTS guest_bookings");
        db.execSQL("DROP TABLE IF EXISTS reviews");
        db.execSQL("DROP TABLE IF EXISTS property_images");
        // Tiếp tục cho các bảng khác
        onCreate(db);
    }

    public String getOwnerName(int ownerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT full_name FROM users WHERE id = ?", new String[]{String.valueOf(ownerId)});
        if (cursor != null && cursor.moveToFirst()) {
            String ownerName = cursor.getString(0);
            cursor.close();
            return ownerName;
        }
        return "Unknown Owner";
    }

    public List<User> getOwnerListForSelector() {
        List<User> ownerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve user_id and name for each owner
        String query = "SELECT id, full_name FROM users";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String full_name = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));
                ownerList.add(new User(id, full_name));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ownerList;
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

    // GET PROPERTY

    public List<Property> getAllProperties() {
        List<Property> properties = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM properties", null);

            // Check if cursor contains any data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
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
                                cursor.getString(cursor.getColumnIndexOrThrow("property_type")),
                                cursor.getString(cursor.getColumnIndexOrThrow("image_base64"))
                        );
                        properties.add(property);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("DatabaseHelper", "Error reading property data", e);
                    }
                } while (cursor.moveToNext());
            } else {
                Log.d("DatabaseHelper", "No properties found in the database or cursor is empty.");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Failed to execute query to retrieve properties", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return properties;
    }


    public Property getPropertyById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Property property = null;

        Cursor cursor = db.rawQuery("SELECT * FROM properties WHERE id = ?", new String[]{String.valueOf(id)});
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
                    cursor.getString(cursor.getColumnIndexOrThrow("property_type")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_base64"))
            );
            cursor.close();
        }
        db.close();
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
                        cursor.getString(cursor.getColumnIndexOrThrow("property_type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image_base64"))
                );
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return property;
    }



    // GET PROPERTY IMAGE

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

    public User getAccount(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

            // Kiểm tra nếu cursor không rỗng và di chuyển đến hàng đầu tiên thành công
            if (cursor != null && cursor.moveToFirst()) {
                user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password_hash")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("created_at")),
                        cursor.getString(cursor.getColumnIndexOrThrow("updated_at")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Roles"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    public User getAccountByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

            // Kiểm tra nếu cursor không rỗng và di chuyển đến hàng đầu tiên thành công
            if (cursor != null && cursor.moveToFirst()) {
                user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password_hash")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("created_at")),
                        cursor.getString(cursor.getColumnIndexOrThrow("updated_at")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Roles"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    public List<User> getAllAccounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        List<User> userList = new ArrayList<>();

        try {
            cursor = db.rawQuery("SELECT * FROM users", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    User user = new User(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                            cursor.getString(cursor.getColumnIndexOrThrow("email")),
                            cursor.getString(cursor.getColumnIndexOrThrow("password_hash")),
                            cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                            cursor.getString(cursor.getColumnIndexOrThrow("created_at")),
                            cursor.getString(cursor.getColumnIndexOrThrow("updated_at")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Roles"))
                    );
                    userList.add(user);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userList;
    }
    public void updateUserRole(int userId, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Roles", role);
        db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }


    public String getUserPasswordHash(int userId) {
        String passwordHash = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT password_hash FROM users WHERE id = ?";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            Log.d("DatabaseHelper", "Executing query: " + query + " with ID: " + userId);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("password_hash");
                if (columnIndex != -1) {
                    passwordHash = cursor.getString(columnIndex);
                } else {
                    Log.e("DatabaseHelper", "Column 'password_hash' not found");
                }
            } else {
                Log.e("DatabaseHelper", "Cursor is null or empty");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching password hash", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return passwordHash;
    }

    public void updatePassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password_hash", newPassword);

        try {
            int rowsAffected = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
            if (rowsAffected > 0) {
                Log.d("DatabaseHelper", "Password updated successfully");
            } else {
                Log.d("DatabaseHelper", "No rows affected, check if the user ID is correct.");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating password", e);
        }
    }


    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public String getHashedPassword() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password_hash FROM users LIMIT 1", null); // Adjust query as needed
        String hashedPassword = null;
        if (cursor.moveToFirst()) {
            hashedPassword = cursor.getString(0);
        }
        cursor.close();
        return hashedPassword;
    }

    private int getCurrentUserId() {
        return 1;
    }



}
