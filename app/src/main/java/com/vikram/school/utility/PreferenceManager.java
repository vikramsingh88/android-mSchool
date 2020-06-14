package com.vikram.school.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private String TAG = "PlayerPreferenceManager";
    private SharedPreferences mSharedPreferences;

    private PreferenceManager() {

    }

    private static class SingletonHolder {
        private static final PreferenceManager INSTANCE = new PreferenceManager();
    }

    public static final PreferenceManager instance() {
        return PreferenceManager.SingletonHolder.INSTANCE;
    }



    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor;
    }

    public void saveLoginPreference(String userName, String password) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString("userName", userName);
        editor.putString("password", password);
        editor.commit();
    }

    public String getSavedUserName() {
        return mSharedPreferences.getString("userName", null);
    }

    public String getSavedPassword() {
        return mSharedPreferences.getString("password", null);
    }

    public String getSession() {
        return mSharedPreferences.getString("session", null);
    }

    public void saveSession(String session) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString("session", session);
        editor.commit();
    }

    public String getToken() {
        return mSharedPreferences.getString("token", null);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString("token", token);
        editor.commit();
    }

    public boolean isAdmin() {
        String str = mSharedPreferences.getString("isAdmin", "No");
        if (str.equalsIgnoreCase("Yes")) {
            return true;
        } else {
            return false;
        }
    }

    public void saveAdmin(String isAdmin) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString("isAdmin", isAdmin);
        editor.commit();
    }

    public String getUsername() {
        String str = mSharedPreferences.getString("username", null);
        return str;
    }

    public void saveUsername(String username) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString("username", username);
        editor.commit();
    }
}

