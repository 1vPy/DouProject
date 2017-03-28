package com.roy.douproject;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.roy.douproject.utils.common.LogUtils;

import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2017/2/16.
 */

public class DouApplication extends Application {

    private static final String TAG = DouApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initKit();
        initSDK();
    }

    private void initKit() {
        DouKit.setContext(getApplicationContext());
    }

    private void initSDK() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String key = appInfo.metaData.getString("sms_app_key");
        String secret = appInfo.metaData.getString("sms_app_secret");
        LogUtils.log(TAG, "key = " + key + ",secret = " + secret, LogUtils.DEBUG);
        SMSSDK.initSDK(getApplicationContext(), key, secret);
    }
}
