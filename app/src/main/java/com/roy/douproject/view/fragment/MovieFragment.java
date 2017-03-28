package com.roy.douproject.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roy.douproject.R;
import com.roy.douproject.view.adapter.DouBaseFragmentAdapter;
import com.roy.douproject.view.fragment.movie.ComingMovieFragment;
import com.roy.douproject.view.fragment.movie.HotMovieFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 */

public class MovieFragment extends Fragment {
    private TabLayout movie_tablayout;
    private ViewPager viewPager;
    private DouBaseFragmentAdapter mDouBaseFragmentAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

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
        initData();
    }

    private void findView(){
        movie_tablayout = (TabLayout) getActivity().findViewById(R.id.movie_tablayout);
        viewPager = (ViewPager) getActivity().findViewById(R.id.movie_viewpager);
        mFragmentList.add(HotMovieFragment.newInstance());
        mFragmentList.add(ComingMovieFragment.newInstance());
        mTitleList.add(getString(R.string.recent_hit));
        mTitleList.add(getString(R.string.coming_soon));
        mTitleList.add("高分电影");
        mDouBaseFragmentAdapter = new DouBaseFragmentAdapter(getChildFragmentManager(),mTitleList,mFragmentList);
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
    }

    private void initData(){
        //DouMovieFactory.newInstance().createApi().getMovieListByQuery("西游",1);
    }
}
