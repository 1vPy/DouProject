package com.roy.douproject.api;

import com.roy.douproject.api.movie.DouMovieApi;
import com.roy.douproject.api.movie.DouRetrofit;
import com.roy.douproject.api.other.OtherApi;
import com.roy.douproject.api.other.OtherRetrofit;

/**
 * Created by Administrator on 2017/3/8.
 */

public class ApiFactory {
    public static DouMovieApi mDouMovieApi;

    public static OtherApi mOtherApi;

    public static synchronized DouMovieApi getDouMovieApi(){
        if(mDouMovieApi == null){
            mDouMovieApi = new DouRetrofit().getDouMovieApi();
        }
        return mDouMovieApi;
    }


    public static synchronized  OtherApi getOtherApi(){
        if(mOtherApi == null){
            mOtherApi = new OtherRetrofit().getOtherApi();
        }
        return mOtherApi;
    }


}
