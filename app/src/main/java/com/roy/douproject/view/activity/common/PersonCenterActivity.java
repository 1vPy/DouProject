package com.roy.douproject.view.activity.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.douproject.R;
import com.roy.douproject.utils.image.ImageUtils;

/**
 * Created by Administrator on 2017/3/23.
 */

public class PersonCenterActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private RelativeLayout user_info_setting;
    private ImageView user_header_img;
    private TextView user_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcenter);
        init();
    }

    private void init(){
        findView();
        initToolBar();
        initData();
    }

    private void initToolBar() {
        //toolbar.setBackgroundColor(preferencesUtil.readInt("app_color"));
        //toolbar.setSubtitleTextColor(preferencesUtil.readInt("app_color"));
        toolbar.setTitle("个人中心");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonCenterActivity.this.finish();
            }
        });
    }

    private void findView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        user_info_setting = (RelativeLayout) findViewById(R.id.user_info_setting);
        user_header_img = (ImageView) findViewById(R.id.user_header_img);
        user_name = (TextView) findViewById(R.id.user_name);
    }

    private void initEvent(){

    }

    private void initData(){
        ImageUtils.newInstance().displayCircleImage(PersonCenterActivity.this,R.drawable.user_header,user_header_img);
        //user_header_img.setBackgroundResource(R.drawable.user_header);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
