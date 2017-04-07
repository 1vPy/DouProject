package com.roy.douproject.presenter.movie;

import com.roy.douproject.api.ApiFactory;
import com.roy.douproject.api.movie.DouMovieApi;
import com.roy.douproject.bean.movie.JsonMovieBean;
import com.roy.douproject.bean.movie.details.JsonDetailBean;
import com.roy.douproject.bean.movie.star.JsonStarBean;
import com.roy.douproject.support.movie.MovieInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/8.
 */

public class MoviePresenter {
    private DouMovieApi mDouMovieApi;
    private MovieInterface mMovieInterface;

    public MoviePresenter(MovieInterface movieInterface) {
        mMovieInterface = movieInterface;
        mDouMovieApi = ApiFactory.getDouMovieApi();
    }


    public void getHotMovie(int start) {
        mDouMovieApi.getHotMovie(start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        movieBack(jsonMovieBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errorBack(throwable);
                    }
                });

    }


    public void getComingMovie(int start, int count) {
        mDouMovieApi.getComingMovie(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        movieBack(jsonMovieBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errorBack(throwable);
                    }
                });

    }

    public void getMovieDetail(String id){
        mDouMovieApi.getMovieDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonDetailBean>() {
                    @Override
                    public void accept(JsonDetailBean jsonDetailBean) throws Exception {
                        detailBack(jsonDetailBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errorBack(throwable);
                    }
                });
    }


    public void getStarDetail(String id){
        mDouMovieApi.getStarDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonStarBean>() {
                    @Override
                    public void accept(JsonStarBean jsonStarBean) throws Exception {
                        starBack(jsonStarBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errorBack(throwable);
                    }
                });
    }

    public void searchMovie(String query){
        mDouMovieApi.searchMovie(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        movieBack(jsonMovieBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errorBack(throwable);
                    }
                });
    }

    private void movieBack(JsonMovieBean jsonMovieBean){
        if (mMovieInterface == null) {
            return;
        }
        if (jsonMovieBean != null) {
            mMovieInterface.getMovieSucceed(jsonMovieBean);
        } else {
            mMovieInterface.getFailed();
        }
    }

    private void detailBack(JsonDetailBean jsonDetailBean){
        if (mMovieInterface == null) {
            return;
        }
        if (jsonDetailBean != null) {
            mMovieInterface.getDetailSucceed(jsonDetailBean);
        } else {
            mMovieInterface.getFailed();
        }
    }

    private void starBack(JsonStarBean jsonStarBean){
        if (mMovieInterface == null) {
            return;
        }
        if (jsonStarBean != null) {
            mMovieInterface.getStarSucceed(jsonStarBean);
        } else {
            mMovieInterface.getFailed();
        }
    }

    private void errorBack(Throwable throwable){
        if (mMovieInterface == null) {
            return;
        }
        if (throwable != null) {
            mMovieInterface.getError(throwable);
        }
    }


}
