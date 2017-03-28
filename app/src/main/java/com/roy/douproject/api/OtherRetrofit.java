package com.roy.douproject.api;

import com.roy.douproject.DouKit;
import com.roy.douproject.api.movie.DouMovieApi;
import com.roy.douproject.api.other.OtherApi;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/16.
 */

public class OtherRetrofit {

    private static final String API_BASE_URL = "http://192.168.2.250:8080/DouProjectServer/";

    private OtherApi mOtherApi;

    public OtherRetrofit() {
        File httpCacheDirectory = new File(DouKit.getContext().getCacheDir(), "okhttpcache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB

        Cache mCache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient mClient = new OkHttpClient.Builder()
                .cache(mCache)
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mOtherApi = mRetrofit.create(OtherApi.class);
    }

    public OtherApi getOtherApi() {
        return mOtherApi;
    }
}
