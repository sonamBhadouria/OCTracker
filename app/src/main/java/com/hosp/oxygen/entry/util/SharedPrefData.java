package com.hosp.oxygen.entry.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefData {

    public final static String PREF_NAME = "com.oc.prefs";
    public final static String DEFAULT_VALUE = "";
    public final static int DEFAULT_VALUE_NUM = 0;


    public final static String ACCESS_Token = "Access_token";
    public final static String refreshToken = "refreshToken";
    public final static String userName = "userName";
    public final static String userId = "userId";
    public final static String email = "email";
    public final static String roleName = "roleName";
    public final static String roleId = "roleId";
    public final static String isactive = "isactive";
    public final static String message = "message";


    public static void saveIntoPrefs(Context context, String key, String value,int numberValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        if (value != null)
            edit.putString(key, value);
        if (numberValue > 0)
            edit.putInt(key, numberValue);
        edit.commit();
    }

    public static String getFromStringPrefs(Context context,String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        return prefs.getString(key, DEFAULT_VALUE);
    }

    public static int getFromIntPrefs(Context context,String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        return prefs.getInt(key, DEFAULT_VALUE_NUM);
    }
}
