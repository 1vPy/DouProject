package com.roy.douproject.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.roy.douproject.DouKit;
import com.roy.douproject.R;
import com.roy.douproject.support.listener.OnProtectModeListener;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.common.SharedPreferencesUtil;
import com.roy.douproject.utils.common.ThemePreference;
import com.roy.douproject.widget.SwitchButton;

import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2017/4/1.
 */

public class SettingActivity extends SwipeBackActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();

    private LinearLayout root_setting;
    private SwipeBackLayout mSwipeBackLayout;

    private Toolbar toolbar;

    private SwitchButton synchronization;
    private SwitchButton protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        init();
    }

    private void init() {
        findView();
        initToolBar();
        initView();
        initEvent();
    }

    private void findView() {
        root_setting = (LinearLayout) findViewById(R.id.root_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        synchronization = (SwitchButton) findViewById(R.id.synchronization);
        protect = (SwitchButton) findViewById(R.id.protect);
    }

    private void initView(){
        synchronization.setCheck(false);
        protect.setCheck(false);
        if (SharedPreferencesUtil.getSharedPreferencesUtil(DouKit.getContext()).readBoolean("ProtectMode")) {
            root_setting.setBackgroundColor(getResources().getColor(R.color.protect_color));
            protect.setCheck(true);
        }
    }

    private void initEvent() {
        synchronization.setOnChangedListener(onChangedListener);
        protect.setOnChangedListener(onChangedListener);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.setting));
        toolbar.setBackgroundColor(ThemePreference.getThemePreference(DouKit.getContext()).readTheme());
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.back_btn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });

    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, boolean checkState) {
            switch (v.getId()) {
                case R.id.synchronization:

                    break;
                case R.id.protect:
                    openProtectMode(checkState);
                    break;
            }
        }
    };

    private void openProtectMode(boolean isOpen) {
        List<OnProtectModeListener> onProtectModeListenerList = DouKit.getOnProtectModeListener();
        if (isOpen) {
            root_setting.setBackgroundColor(getResources().getColor(R.color.protect_color));
            SharedPreferencesUtil.getSharedPreferencesUtil(DouKit.getContext()).saveBoolean("ProtectMode", true);
            if (onProtectModeListenerList != null) {
                LogUtils.log(TAG, "onProtectModeListener size:" + onProtectModeListenerList.size(), LogUtils.DEBUG);
                for (OnProtectModeListener onProtectModeListener : onProtectModeListenerList) {
                    onProtectModeListener.modeChanged(isOpen);
                }
            }
        } else {
            SharedPreferencesUtil.getSharedPreferencesUtil(DouKit.getContext()).saveBoolean("ProtectMode", false);
            root_setting.setBackgroundColor(getResources().getColor(android.R.color.white));
            if (onProtectModeListenerList != null) {
                LogUtils.log(TAG, "onProtectModeListener size:" + onProtectModeListenerList.size(), LogUtils.DEBUG);
                for (OnProtectModeListener onProtectModeListener : onProtectModeListenerList) {
                    onProtectModeListener.modeChanged(isOpen);
                }
            }
        }
    }
}
