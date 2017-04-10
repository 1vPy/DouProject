package com.roy.douproject.api.other;

import com.roy.douproject.bean.other.Results;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface OtherApi {

    /**
     * 账号查询
     * @param username
     * @return
     */
    @GET("check")
    Observable<Results> checkUser(@Query("username") String username);

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    @GET("register")
    Observable<Results> register(@Query("username") String username,@Query("password") String password);

    @GET("login")
    Observable<Results> login(@Query("username") String username,@Query("password") String password);

}
