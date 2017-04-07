package com.roy.douproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.douproject.DouKit;
import com.roy.douproject.R;
import com.roy.douproject.utils.common.ThemePreference;
import com.roy.douproject.utils.common.UserPreference;
import com.roy.douproject.utils.image.ImageUtils;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2017/3/23.
 */

public class PersonCenterActivity extends SwipeBackActivity {
    private RelativeLayout user_info_setting;
    private Toolbar toolbar;
    private ImageView user_header_img;
    private TextView user_name;
    private Button logout;

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcenter);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);


        init();
    }

    private void init(){
        findView();
        initToolBar();
        initData();
        initEvent();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.personal_center));
        toolbar.setBackgroundColor(ThemePreference.getThemePreference(DouKit.getContext()).readTheme());
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
        logout = (Button) findViewById(R.id.logout);
    }

    private void initEvent(){
        logout.setOnClickListener(clickListener);
    }

    private void initData(){
        ImageUtils.getInstance().displayCircleImage(PersonCenterActivity.this,R.drawable.user_header,user_header_img);
        //user_header_img.setBackgroundResource(R.drawable.user_header);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logout:
                    logout();
                    break;
            }
        }
    };

    private void logout(){
        UserPreference.getUserPreference(DouKit.getContext()).clearUserInfo();
        startActivity(new Intent(PersonCenterActivity.this,LoginActivity.class));
        PersonCenterActivity.this.finish();
    }
}
