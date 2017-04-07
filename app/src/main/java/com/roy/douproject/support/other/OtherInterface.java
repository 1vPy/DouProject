package com.roy.douproject.support.other;

import com.roy.douproject.bean.other.Results;

/**
 * Created by Administrator on 2017/3/23.
 */

public interface OtherInterface {
    void loginResultSucceed(Results results);

    void registerResultSucceed(Results results);

    void resultFailed();

    void resultError(Throwable throwable);
}
