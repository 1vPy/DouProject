package com.roy.douproject.view.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.roy.douproject.DouKit;
import com.roy.douproject.R;
import com.roy.douproject.api.ApiFactory;
import com.roy.douproject.bean.other.Results;
import com.roy.douproject.bean.other.SmsResults;
import com.roy.douproject.bean.other.User;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.common.SmsErrorUtils;
import com.roy.douproject.utils.common.UserPreference;
import com.roy.douproject.utils.json.JsonUtils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.concurrent.TimeUnit;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/16.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private User user;
    private boolean login_mode = true;

    private Toolbar toolbar;

    private RelativeLayout login_view;
    private RelativeLayout register_view;
    private TextView switch_mode;

    ////
    private String username;
    private String password;

    ////login
    private EditText login_username;
    private EditText login_password;
    private Button login;

    ////register
    private EditText register_username;
    private TextView register_username_tip;
    private EditText register_password;
    private EditText register_confirm_password;
    private TextView register_password_tip;
    private EditText phone_num;
    private EditText confirm_num;
    private Button get_confirm_num;
    private Button register;

    private boolean canUsername = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initToolBar();
        initEvent();
        if (isLogin()) {
            LogUtils.log(TAG, "has login", LogUtils.DEBUG);
            DouKit.setUser(user);
            startActivity(new Intent(LoginActivity.this,PersonCenterActivity.class));
            LoginActivity.this.finish();
        } else {
            LogUtils.log(TAG, "no login", LogUtils.DEBUG);
            initViewByMode();
        }
    }

    private void initToolBar() {
        //toolbar.setBackgroundColor(preferencesUtil.readInt("app_color"));
        //toolbar.setSubtitleTextColor(preferencesUtil.readInt("app_color"));
        toolbar.setTitle("用户登录");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        login_view = (RelativeLayout) findViewById(R.id.login_view);
        register_view = (RelativeLayout) findViewById(R.id.register_view);
        switch_mode = (TextView) findViewById(R.id.switch_mode);

        ////login
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login);

        ////register
        register_username = (EditText) findViewById(R.id.register_username);
        register_username_tip = (TextView) findViewById(R.id.register_username_tip);
        register_password = (EditText) findViewById(R.id.register_password);
        register_confirm_password = (EditText) findViewById(R.id.register_confirm_password);
        register_password_tip = (TextView) findViewById(R.id.register_password_tip);
        phone_num = (EditText) findViewById(R.id.phone_num);
        confirm_num = (EditText) findViewById(R.id.confirm_num);
        get_confirm_num = (Button) findViewById(R.id.get_confirm_num);
        register = (Button) findViewById(R.id.register);
    }

    private void initViewByMode() {
        if (login_mode) {
            toolbar.setTitle("用户登录");

            login_view.setVisibility(View.VISIBLE);
            switch_mode.setVisibility(View.VISIBLE);
            register_view.setVisibility(View.GONE);

            switch_mode.setText("没有账号？点击注册");
        } else {
            toolbar.setTitle("用户注册");

            login_view.setVisibility(View.GONE);
            switch_mode.setVisibility(View.VISIBLE);
            register_view.setVisibility(View.VISIBLE);

            switch_mode.setText("已有账号？点击登录");
        }
    }

    private void initEvent() {
        switch_mode.setOnClickListener(clickListener);
        register.setOnClickListener(clickListener);
        login.setOnClickListener(clickListener);
        get_confirm_num.setOnClickListener(clickListener);
/*        RxTextView.textChanges(register_username)
                .debounce(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        ApiFactory.getOtherApi().CheckUser(charSequence.toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Results>() {
                                    @Override
                                    public void accept(Results results) throws Exception {
                                        LogUtils.log(TAG, results.getResult(),LogUtils.DEBUG);
                                    }
                                });
                    }
                });*/

        RxTextView.textChanges(register_username)
                .debounce(3, TimeUnit.SECONDS)
                .flatMap(new Function<CharSequence, Observable<Results>>() {
                    @Override
                    public Observable<Results> apply(CharSequence charSequence) throws Exception {
                        LogUtils.log(TAG, charSequence.toString(), LogUtils.DEBUG);
                        return ApiFactory.getOtherApi().checkUser(charSequence.toString());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Results>() {
                    @Override
                    public void accept(Results results) throws Exception {
                        LogUtils.log(TAG, results.getResult(), LogUtils.DEBUG);
                        switch (Integer.valueOf(results.getStatus())) {
                            case 0:
                                canUsername = true;
                                register_username_tip.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                break;
                            case 1:
                                canUsername = false;
                                register_username_tip.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                                break;
                        }
                        if (!register_username.getText().toString().isEmpty()) {
                            register_username_tip.setText(results.getResult());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //Toast.makeText(LoginActivity.this,)
                        LogUtils.log(TAG,"checkUserError:"+throwable.getLocalizedMessage(),LogUtils.DEBUG);
                    }
                });

        RxTextView.textChanges(register_confirm_password)
                .debounce(3, TimeUnit.SECONDS)
                .flatMap(new Function<CharSequence, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> apply(@NonNull CharSequence charSequence) throws Exception {
                        return Observable.just(TextUtils.equals(register_password.getText(), charSequence));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        LogUtils.log(TAG, "confirm:" + aBoolean, LogUtils.DEBUG);
                        if (aBoolean) {
                            register_password_tip.setText("");
                        }
                    }
                });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.switch_mode:
                    login_mode = !login_mode;
                    initViewByMode();
                    break;
                case R.id.register:
                    LogUtils.log(TAG,"registerValidate:"+registerValidate(),LogUtils.DEBUG);
                    if (registerValidate()) {
                        SMSSDK.submitVerificationCode("86", phone_num.getText().toString(), confirm_num.getText().toString());
                        //register();
                    }
                    break;

                case R.id.login:
                    if (loginValidate()) {
                        login();
                    }
                    break;
                case R.id.get_confirm_num:
                    SMSSDK.registerEventHandler(eventHandler);
                    SMSSDK.getVerificationCode("86", phone_num.getText().toString());
                    break;
            }
        }
    };

    private boolean registerValidate() {
        if (canUsername) {
            if (register_username.getText().toString().isEmpty()) {
                register_username_tip.setText("用户名不能为空");
                register_username_tip.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                return false;
            }
            if (register_password.getText().toString().isEmpty()) {
                register_password_tip.setText("密码不能为空");
                return false;
            }
            if (register_username.getText().toString().length() < 6) {
                register_username_tip.setText("用户名不能少于6位");
                register_username_tip.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                return false;
            }
            if (register_password.getText().toString().length() < 6) {
                register_password_tip.setText("密码不能少于6位");
                return false;
            }
            if (!TextUtils.equals(register_password.getText(), register_confirm_password.getText())) {
                register_password_tip.setText("两次密码不一致");
                return false;
            }

            username = register_username.getText().toString();
            password = register_password.getText().toString();
            return true;
        } else {
            register_password_tip.setText("用户名不可用");
            return false;
        }
    }


    private boolean loginValidate() {
        if (login_username.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (login_password.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        username = login_username.getText().toString();
        password = login_password.getText().toString();
        return true;
    }

    private void register() {
        SMSSDK.unregisterEventHandler(eventHandler);
        ApiFactory.getOtherApi().register(username, new String(Hex.encodeHex(DigestUtils.md5(password))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Results>() {
                    @Override
                    public void accept(Results results) throws Exception {
                        switch (Integer.valueOf(results.getStatus())) {
                            case 0:
                                //login();
                                login_mode = true;
                                //LoginActivity.this.recreate();
                                initViewByMode();
                                login_username.setText(username);
                                Toast.makeText(LoginActivity.this, results.getResult(), Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(LoginActivity.this, results.getResult(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(LoginActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void login() {
        ApiFactory.getOtherApi().login(username, new String(Hex.encodeHex(DigestUtils.md5(password))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Results>() {
                    @Override
                    public void accept(Results results) throws Exception {
                        switch (Integer.valueOf(results.getStatus())) {
                            case 0:
                                UserPreference.getUserPreference(DouKit.getContext()).saveUserInfo(new User(username, password));
                                //LoginActivity.this.recreate();
                                startActivity(new Intent(LoginActivity.this,PersonCenterActivity.class));
                                Toast.makeText(LoginActivity.this, results.getResult(), Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(LoginActivity.this, results.getResult(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(LoginActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    register();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    countDownTimer.start();
                    Message message = new Message();
                    message.what = 0;
                    mHandler.sendMessage(message);

                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
                Message message = new Message();
                message.what = 1;
                message.obj = data;
                mHandler.sendMessage(message);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    get_confirm_num.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "验证码已经发送",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, SmsErrorUtils.getErrorMsg(JsonUtils.newInstance().Json2JavaBean(((Throwable) msg.obj).getLocalizedMessage(), SmsResults.class).getStatus()), Toast.LENGTH_LONG).show();
                    break;
            }

        }
    };

    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //get_confirm_num.setEnabled(false);
            get_confirm_num.setText((int) millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            get_confirm_num.setText("重新获取");
            get_confirm_num.setEnabled(true);
        }
    };

    private boolean isLogin(){
        user = UserPreference.getUserPreference(DouKit.getContext()).readUserInfo();
        if(user != null){
            return true;
        }
        return false;
    }
}
