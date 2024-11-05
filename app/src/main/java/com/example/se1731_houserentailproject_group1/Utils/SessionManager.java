package com.example.se1731_houserentailproject_group1.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.se1731_houserentailproject_group1.Model.User;
import com.google.gson.Gson;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER = "user";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void createSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    public void updateSession(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        return gson.fromJson(userJson, User.class);
    }

    // New logout method
    public void logout() {
        editor.clear();  // Clears all data in SharedPreferences
        editor.apply();
    }
}


