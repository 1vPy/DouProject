package com.roy.douproject.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.roy.douproject.DouKit;
import com.roy.douproject.R;
import com.roy.douproject.datainterface.other.OnProtectModeListener;
import com.roy.douproject.datainterface.other.OnThemeChangeListener;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.common.SharedPreferencesUtil;
import com.roy.douproject.utils.common.ThemePreference;
import com.roy.douproject.view.activity.MainActivity;
import com.roy.douproject.view.activity.common.LoginActivity;
import com.roy.douproject.view.adapter.DouBaseFragmentAdapter;
import com.roy.douproject.view.fragment.movie.ComingMovieFragment;
import com.roy.douproject.view.fragment.movie.HotMovieFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 */

public class MovieFragment extends Fragment implements OnThemeChangeListener, OnProtectModeListener {
    private static final String TAG = MovieFragment.class.getSimpleName();

    private CoordinatorLayout coordinatorlayout;
    private TabLayout movie_tablayout;
    private ViewPager viewPager;
    private DouBaseFragmentAdapter mDouBaseFragmentAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    private HotMovieFragment mHotMovieFragment;
    private ComingMovieFragment mComingMovieFragment;

    public static MovieFragment newInstance() {
        Bundle args = new Bundle();
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
        initEvent();
        initData();
    }

    private void findView() {
        coordinatorlayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorlayout);
        movie_tablayout = (TabLayout) getActivity().findViewById(R.id.movie_tablayout);
        viewPager = (ViewPager) getActivity().findViewById(R.id.movie_viewpager);

        mHotMovieFragment = HotMovieFragment.newInstance();
        mComingMovieFragment = ComingMovieFragment.newInstance();

        mFragmentList.add(mHotMovieFragment);
        mFragmentList.add(mComingMovieFragment);
        mTitleList.add(getString(R.string.recent_hit));
        mTitleList.add(getString(R.string.coming_soon));
        mTitleList.add("高分电影");
        mDouBaseFragmentAdapter = new DouBaseFragmentAdapter(getChildFragmentManager(), mTitleList, mFragmentList);
        viewPager.setAdapter(mDouBaseFragmentAdapter);
        viewPager.setOffscreenPageLimit(3);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        //viewPager.addOnPageChangeListener(this);

        movie_tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        movie_tablayout.setTabMode(TabLayout.MODE_FIXED);
        //movie_tablayout.setSelectedTabIndicatorColor(ThemeUtils.getThemeColor());
        //movie_tablayout.setTabTextColors(getResources().getColor(R.color.text_gray_6),ThemeUtils.getThemeColor());
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        movie_tablayout.setupWithViewPager(viewPager);

        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        movie_tablayout.setTabsFromPagerAdapter(mDouBaseFragmentAdapter);
        movie_tablayout.setBackgroundResource(android.R.color.white);
        movie_tablayout.setTabTextColors(getResources().getColor(android.R.color.holo_blue_dark), getResources().getColor(android.R.color.holo_blue_light));
        movie_tablayout.setSelectedTabIndicatorColor(ThemePreference.getThemePreference(DouKit.getContext()).readTheme());
        if (SharedPreferencesUtil.getSharedPreferencesUtil(DouKit.getContext()).readBoolean("ProtectMode")) {
            movie_tablayout.setBackgroundColor(getResources().getColor(R.color.protect_color));
        }
    }

    private void initData() {
        //DouMovieFactory.newInstance().createApi().getMovieListByQuery("西游",1);
    }

    private void initEvent() {
        DouKit.addOnThemeChangeListener(this);
        DouKit.addOnProtectModeListener(this);
    }

    public void backToTop() {
        if(viewPager != null){
            switch (viewPager.getCurrentItem()) {
                case 0:
                    mHotMovieFragment.scrollToTop();
                    break;
                case 1:
                    mComingMovieFragment.scrollToTop();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DouKit.removeOnThemeChangeListener(this);
        DouKit.removeOnProtectModeListener(this);
    }

    @Override
    public void themeChanged(int color) {
        //movie_tablayout.setBackgroundColor(color);
        movie_tablayout.setSelectedTabIndicatorColor(color);
    }

    @Override
    public void modeChanged(boolean isOpen) {
        LogUtils.log(TAG, "added:" + isAdded(), LogUtils.DEBUG);
        if (isAdded()) {
            if (isOpen) {
                movie_tablayout.setBackgroundColor(getResources().getColor(R.color.protect_color));
            } else {
                movie_tablayout.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        }
    }
}
