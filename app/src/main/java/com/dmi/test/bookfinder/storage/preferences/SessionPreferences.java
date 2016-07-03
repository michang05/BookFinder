package com.dmi.test.bookfinder.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.dmi.test.bookfinder.DmiTestApplication;

/**
 * Created by Mikey on 7/2/2016.
 */
public class SessionPreferences {

    private final static String SESSION_PREFERENCES = "user_sh_preferences";

    private static final String LOGIN_USERNAME = "login_username";
    private static final String LOGIN_PASSWORD = "login_password";


    public static SharedPreferences getSessionPreferences() {
        return DmiTestApplication.getInstance().getApplicationContext()
                .getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static boolean isUserLoggedIn() {
        return ((getLoginUsername() != null) && (getLoginPassword() != null));
    }

    public static void clearUserSession() {
        getSessionPreferences().edit().clear().apply();
    }


    public static void saveUsername(String username) {
        getSessionPreferences().edit().putString(LOGIN_USERNAME, username).commit();
    }

    public static String getLoginUsername() {
        return getSessionPreferences().getString(LOGIN_USERNAME, null);
    }

    public static void savePassword(String password) {
        getSessionPreferences().edit().putString(LOGIN_PASSWORD, password).commit();
    }

    public static String getLoginPassword() {
        return getSessionPreferences().getString(LOGIN_PASSWORD, null);
    }
}
