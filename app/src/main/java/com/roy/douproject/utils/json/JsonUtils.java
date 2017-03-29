package com.roy.douproject.utils.json;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/2/15.
 */

public class JsonUtils {
    private static JsonUtils instance;
    private JsonUtils(){

    }

    public static JsonUtils getInstance(){
        if(instance == null){
            synchronized(JsonUtils.class){
                if(instance == null){
                    instance = new JsonUtils();
                }
            }
        }
        return instance;
    }

    public <T>T Json2JavaBean(String jsonString,Class<T> clazz){
        Gson gson = new Gson();
        T t = gson.fromJson(jsonString,clazz);
        return t;
    }
}
