package com.roy.douproject.utils.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.roy.douproject.bean.other.User;

/**
 * Created by Administrator on 2017/4/6.
 */

public class ThemePreference {
    private static final String TAG = UserPreference.class.getSimpleName();
    private final static String SP_NAME = "AppTheme";
    private final static int MODE = Context.MODE_PRIVATE;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static ThemePreference instance = null;

    private ThemePreference(Context context) {
        preferences = context.getSharedPreferences(SP_NAME, MODE);
        editor = preferences.edit();
    }

    public static ThemePreference getThemePreference(Context context) {
        if (instance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (instance == null) {
                    instance = new ThemePreference(context);
                }
            }
        }
        return instance;
    }

    public void saveTheme(int color) {
        editor.putInt("theme", color);
        editor.commit();
    }

    public int readTheme() {
        return  preferences.getInt("theme",0);
    }

    public void clearUserInfo() {
        editor.clear();
        editor.commit();
    }
}
