package com.example.quizapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quizapp.models.UserModel;

public class SharedPrefsHelper {
    private static final String SHARED_PREFS_NAME = "user_prefs";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_USER = "user";

    private final SharedPreferences prefs;

    public SharedPrefsHelper(Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserRole(String role) {
        if (!role.equals(ROLE_ADMIN) && !role.equals(ROLE_USER)) {
            throw new IllegalArgumentException("Invalid user role: " + role);
        }
        prefs.edit().putString(KEY_USER_ROLE, role).apply();
    }

    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, null);
    }

    public boolean isAdmin() {
        return ROLE_ADMIN.equals(getUserRole());
    }

    public boolean isUser() {
        return ROLE_USER.equals(getUserRole());
    }

    public void saveUserModelToSharedPref( UserModel userModel) {
        if (userModel == null) {
            return; // Handle null user case
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userID", userModel.getUserID());
        editor.putString("name", userModel.getName());
        editor.putString("phone", userModel.getPhone());
        editor.putString("email", userModel.getEmail());
        editor.putString("role", userModel.getRole());
        editor.apply(); // Apply changes asynchronously
    }

    public UserModel getUserModelFromSharedPref() {
        String userID = prefs.getString("userID", null);
        String name = prefs.getString("name", null);
        String phone = prefs.getString("phone", null);
        String email = prefs.getString("email", null);
        String role = prefs.getString("role", null);

        if (userID == null || name == null || phone == null || email == null || role == null) {
            return null; // User data not found
        }

        return new UserModel(userID, name, phone, email, role);
    }

    public void clearOnLogout(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
