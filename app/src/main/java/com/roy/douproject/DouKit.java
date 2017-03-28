package com.roy.douproject;

import android.content.Context;

import com.roy.douproject.bean.movie.Subjects;
import com.roy.douproject.bean.other.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 */

public class DouKit {
    private static Context mContext;

    private static User mUser;

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


}
