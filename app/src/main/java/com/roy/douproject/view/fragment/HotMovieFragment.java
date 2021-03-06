package com.roy.douproject.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.douproject.DouKit;
import com.roy.douproject.R;
import com.roy.douproject.bean.movie.details.JsonDetailBean;
import com.roy.douproject.bean.movie.star.JsonStarBean;
import com.roy.douproject.support.movie.MovieInterface;
import com.roy.douproject.support.listener.OnProtectModeListener;
import com.roy.douproject.utils.common.SharedPreferencesUtil;
import com.roy.douproject.view.MovieDetailsActivity;
import com.roy.douproject.view.adapter.HotMovieRecyclerAdapter;
import com.roy.douproject.bean.movie.JsonMovieBean;
import com.roy.douproject.bean.movie.Subjects;
import com.roy.douproject.presenter.movie.MoviePresenter;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.common.ScreenUtils;
import com.roy.douproject.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class HotMovieFragment extends Fragment implements MovieInterface, OnProtectModeListener {
    private static final String TAG = HotMovieFragment.class.getSimpleName();

    private RelativeLayout root_hotmovie;

    private RecyclerView movie_recyclerView;
    private TextView hotmovie_tip;

    private HotMovieRecyclerAdapter mHotMovieRecyclerAdapter;
    private List<Subjects> mSubjectsList = new ArrayList<>();
    private MoviePresenter moviePresenter = new MoviePresenter(this);

    public static HotMovieFragment newInstance() {
        Bundle args = new Bundle();
        HotMovieFragment fragment = new HotMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotmovie, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        findView();
        initView();
        initData();
        initEvent();
    }

    private void findView(){
        root_hotmovie = (RelativeLayout) getActivity().findViewById(R.id.root_hotmovie);
        hotmovie_tip = (TextView) getActivity().findViewById(R.id.hotmovie_tip);
        movie_recyclerView = (RecyclerView) getActivity().findViewById(R.id.hotmovie_recyclerView);
    }

    private void initData() {
        moviePresenter.getHotMovie(0);
    }

    private void initView() {
        movie_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), 0);
        movie_recyclerView.addItemDecoration(spacesItemDecoration);
        mHotMovieRecyclerAdapter = new HotMovieRecyclerAdapter(getActivity(), mSubjectsList);
        movie_recyclerView.setAdapter(mHotMovieRecyclerAdapter);


        if (SharedPreferencesUtil.getSharedPreferencesUtil(DouKit.getContext()).readBoolean("ProtectMode")) {
            root_hotmovie.setBackgroundColor(getResources().getColor(R.color.protect_color));
            movie_recyclerView.setBackgroundColor(getResources().getColor(R.color.protect_color));
        }


    }

    private void initEvent(){
        mHotMovieRecyclerAdapter.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
        DouKit.addOnProtectModeListener(this);
    }

    private HotMovieRecyclerAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener =  new HotMovieRecyclerAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            LogUtils.log(TAG, mSubjectsList.get(position).getId(), LogUtils.DEBUG);
            Intent intent = new Intent();
            intent.putExtra("movieId", mSubjectsList.get(position).getId());
            intent.setClass(getActivity(), MovieDetailsActivity.class);
            startActivity(intent);
        }
    };


    @Override
    public void getMovieSucceed(JsonMovieBean jsonMovieBean) {
        mSubjectsList.addAll(jsonMovieBean.getSubjects());
        mHotMovieRecyclerAdapter.notifyDataSetChanged();
        hotmovie_tip.setText("");
    }

    @Override
    public void getDetailSucceed(JsonDetailBean jsonDetailBean) {

    }

    @Override
    public void getStarSucceed(JsonStarBean jsonStarBean) {

    }

    @Override
    public void getFailed() {
        Toast.makeText(getActivity(), "没有请求到数据", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getError(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        hotmovie_tip.setText("网络请求超时，点击重新尝试");
        hotmovie_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviePresenter.getHotMovie(0);
            }
        });
    }

    public void scrollToTop() {
        if (((GridLayoutManager) (movie_recyclerView.getLayoutManager())).findLastVisibleItemPosition() > 30) {
            movie_recyclerView.scrollToPosition(0);
        } else {
            movie_recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DouKit.removeOnProtectModeListener(this);
    }

    @Override
    public void modeChanged(boolean isOpen) {
        LogUtils.log(TAG,"added:"+isAdded(),LogUtils.DEBUG);
        if (isAdded()) {
            if (isOpen) {
                root_hotmovie.setBackgroundColor(getResources().getColor(R.color.protect_color));
                movie_recyclerView.setBackgroundColor(getResources().getColor(R.color.protect_color));
            } else {
                root_hotmovie.setBackgroundColor(getResources().getColor(android.R.color.white));
                movie_recyclerView.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        }
    }
}
