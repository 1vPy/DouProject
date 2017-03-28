package com.roy.douproject.utils.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
    private final static String SP_NAME = "DouProject";
    private final static int MODE = Context.MODE_PRIVATE;
    private SharedPreferences preferences;
    private Editor editor;
    private static SharedPreferencesUtil instance = null;

    private SharedPreferencesUtil(Context context) {
        preferences = context.getSharedPreferences(SP_NAME, MODE);
        editor = preferences.edit();
    }

    public static SharedPreferencesUtil getSharedPreferencesUtil(Context context) {
        if (instance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (instance == null) {
                    instance = new SharedPreferencesUtil(context);
                }
            }
        }
        return instance;
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String readString(String key) {
        return preferences.getString(key, "");
    }

    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int readInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean readBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void saveLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long readLong(String key) {
        return preferences.getLong(key, 0);
    }

    public void saveFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float readFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    public void cleanSP() {
        editor.clear();
        editor.commit();
    }
}
