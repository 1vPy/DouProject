package com.roy.douproject.presenter.other;

import com.roy.douproject.api.ApiFactory;
import com.roy.douproject.api.other.OtherApi;
import com.roy.douproject.bean.other.Results;
import com.roy.douproject.datainterface.other.OtherInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/23.
 */

public class OtherPresenter {
    private OtherApi mOtherApi;
    private OtherInterface mOtherInterface;

    public OtherPresenter(OtherInterface otherInterface) {
        mOtherInterface = otherInterface;
        mOtherApi = ApiFactory.getOtherApi();
    }

    public void login(String username, String password) {
        mOtherApi.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Results>() {
                    @Override
                    public void accept(Results results) throws Exception {
                        loginResultsBack(results);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errorBack(throwable);
                    }
                });
    }

    public void register(String username, String password){
        mOtherApi.register(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Results>() {
                    @Override
                    public void accept(Results results) throws Exception {
                        registerResultsBack(results);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errorBack(throwable);
                    }
                });
    }

    private void loginResultsBack(Results results) {
        if (mOtherInterface == null) {
            return;
        }
        if (results != null) {
            mOtherInterface.loginResultSucceed(results);
        }else{
            mOtherInterface.resultFailed();
        }
    }

    private void registerResultsBack(Results results) {
        if (mOtherInterface == null) {
            return;
        }
        if (results != null) {
            mOtherInterface.registerResultSucceed(results);
        }else{
            mOtherInterface.resultFailed();
        }
    }

    private void errorBack(Throwable throwable){
        if (mOtherInterface == null) {
            return;
        }
        if (throwable != null) {
            mOtherInterface.resultError(throwable);
        }

    }

}
