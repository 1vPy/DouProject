package com.roy.douproject.api;

import com.roy.douproject.DouKit;
import com.roy.douproject.api.movie.DouMovieApi;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/8.
 */

public class DouRetrofit {
    private static final String API_BASE_URL = "https://api.douban.com/";

    private DouMovieApi mDouMovieApi;

    public DouRetrofit() {
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
        mDouMovieApi = mRetrofit.create(DouMovieApi.class);
    }

    public DouMovieApi getDouMovieApi() {
        return mDouMovieApi;
    }
}
