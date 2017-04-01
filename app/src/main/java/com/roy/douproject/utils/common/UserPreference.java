package com.roy.douproject.utils.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.roy.douproject.bean.other.User;

/**
 * Created by Administrator on 2017/3/16.
 */

public class UserPreference {
    private static final String TAG = UserPreference.class.getSimpleName();
    private final static String SP_NAME = "UserInfo";
    private final static int MODE = Context.MODE_PRIVATE;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static UserPreference instance = null;

    private UserPreference(Context context) {
        preferences = context.getSharedPreferences(SP_NAME, MODE);
        editor = preferences.edit();
    }

    public static UserPreference getUserPreference(Context context) {
        if (instance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (instance == null) {
                    instance = new UserPreference(context);
                }
            }
        }
        return instance;
    }

    public void saveUserInfo(User user) {
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.commit();
    }

    public User readUserInfo() {
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        LogUtils.log(TAG, "username:" + username + ",password:" + password, LogUtils.DEBUG);
        if (!username.isEmpty() && !password.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            return user;
        }
        return null;
    }

    public void clearUserInfo() {
        editor.clear();
        editor.commit();
    }

}
