package com.roy.douproject.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.roy.douproject.R;

/**
 * Created by Administrator on 2017/3/9.
 */

public class WebViewActivity extends Activity{
    private WebView mWebView;
    private String mUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        findView();
        initView();
        initData();
    }

    private void findView(){
        mWebView = (WebView) findViewById(R.id.webview);

    }

    private void initView(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    private void initData(){
        if(getIntent() != null){
            mUrl = getIntent().getStringExtra("url");
        }
        if(mUrl != null){
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
                if(mWebView.canGoBack()){
                    mWebView.goBack();
                    return true;
                }
        }
        return super.dispatchKeyEvent(event);
    }
}
