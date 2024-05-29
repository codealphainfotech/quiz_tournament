package com.example.quizapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

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

}
