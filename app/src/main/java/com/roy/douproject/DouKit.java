package com.roy.douproject;

import android.content.Context;

import com.roy.douproject.bean.other.User;
import com.roy.douproject.support.listener.OnProtectModeListener;
import com.roy.douproject.support.listener.OnThemeChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/2/16.
 */

public class DouKit {
    private static Context mContext;

    private static User mUser;

    private static List<OnThemeChangeListener> mOnThemeChangeListenerList = new ArrayList<>();

    private static List<OnProtectModeListener> mOnProtectModeListenerList = new ArrayList<>();

    public static void setContext(Context context){
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }

    public static void setUser(User user){
        mUser = user;
    }

    public static User getUser(){
        return mUser;
    }

    public static void addOnThemeChangeListener(OnThemeChangeListener onThemeChangeListener){
        mOnThemeChangeListenerList.add(onThemeChangeListener);
    }

    public static List<OnThemeChangeListener> getOnThemeChangeListener(){
        return mOnThemeChangeListenerList;
    }

    public static void removeOnThemeChangeListener(OnThemeChangeListener onThemeChangeListener){
        mOnThemeChangeListenerList.remove(onThemeChangeListener);
    }

    public static void addOnProtectModeListener(OnProtectModeListener onProtectModeListener){
        mOnProtectModeListenerList.add(onProtectModeListener);
    }

    public static List<OnProtectModeListener> getOnProtectModeListener(){
        return mOnProtectModeListenerList;
    }

    public static void removeOnProtectModeListener(OnProtectModeListener onProtectModeListener){
        mOnProtectModeListenerList.remove(onProtectModeListener);
    }

}
