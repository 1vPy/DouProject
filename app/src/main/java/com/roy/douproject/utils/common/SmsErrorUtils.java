package com.roy.douproject.utils.common;

/**
 * Created by Administrator on 2017/3/17.
 */

public class SmsErrorUtils {
    public static String getErrorMsg(String code) {
        String errorMsg = "";
        switch (Integer.valueOf(code)) {
            case 477:
                errorMsg = "当前手机号发送短信的数量超过限额";
                break;
            case 600:
                errorMsg = "API使用受限制";
                break;
            case 601:
                errorMsg = "短信发送受限";
                break;
            case 602:
                errorMsg = "无法发送此地区短信";
                break;
            case 603:
                errorMsg = "请填写正确的手机号码";
                break;
            case 604:
                errorMsg = "当前服务暂不支持此国家";
                break;
        }
        return errorMsg;

    }
}
