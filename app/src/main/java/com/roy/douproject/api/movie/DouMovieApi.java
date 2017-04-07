package com.roy.douproject.api.movie;

import com.roy.douproject.bean.movie.JsonMovieBean;
import com.roy.douproject.bean.movie.details.JsonDetailBean;
import com.roy.douproject.bean.movie.star.JsonStarBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/3/8.
 */

public interface DouMovieApi {

    /**
     * 正在上映
     * @param start
     * @return
     */
    @GET("v2/movie/in_theaters")
    Observable<JsonMovieBean> getHotMovie(@Query("start") int start);

    /**
     * 即将上映
     * @param start
     * @param count
     * @return
     */
    @GET("v2/movie/coming_soon")
    Observable<JsonMovieBean> getComingMovie(@Query("start") int start,@Query("count") int count);

    /**
     * 电影详情
     * @param id
     * @return
     */
    @GET("v2/movie/subject/{id}")
    Observable<JsonDetailBean> getMovieDetail(@Path("id") String id);

    /**
     * 电影查询
     * @param query
     * @return
     */
    @GET("v2/movie/search")
    Observable<JsonMovieBean> searchMovie(@Query("q") String query);

    /**
     * 演员查询
     * @param id
     * @return
     */
    @GET("/v2/movie/celebrity/{id}")
    Observable<JsonStarBean> getStarDetail(@Path("id") String id);
}
