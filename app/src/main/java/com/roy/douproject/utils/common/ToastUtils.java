package com.roy.douproject.utils.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/29.
 */

public class ToastUtils {

    private static ToastUtils instance;
    private Toast mToast;

    public static ToastUtils getInstance(){
        if(instance == null){
            synchronized (ToastUtils.class){
                if(instance == null){
                    instance = new ToastUtils();
                }
            }
        }
        return instance;
    }

    public void showToast(Context context,String content){

        if(mToast != null){
            mToast.setText(content);
        }else{
            mToast = Toast.makeText(context,content,Toast.LENGTH_LONG);
        }
        mToast.show();
    }
}
