package com.roy.douproject.support.movie;

import com.roy.douproject.bean.movie.JsonMovieBean;
import com.roy.douproject.bean.movie.details.JsonDetailBean;
import com.roy.douproject.bean.movie.star.JsonStarBean;

/**
 * Created by Administrator on 2017/3/23.
 */

public interface MovieInterface {
    void getMovieSucceed(JsonMovieBean jsonMovieBean);

    void getDetailSucceed(JsonDetailBean jsonDetailBean);

    void getStarSucceed(JsonStarBean jsonStarBean);

    void getFailed();

    void getError(Throwable throwable);
}
