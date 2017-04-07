package com.roy.douproject.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.roy.douproject.view.adapter.ComingMovieRecyclerAdapter;
import com.roy.douproject.bean.movie.JsonMovieBean;
import com.roy.douproject.bean.movie.Subjects;
import com.roy.douproject.presenter.movie.MoviePresenter;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.common.ScreenUtils;
import com.roy.douproject.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class ComingMovieFragment extends Fragment implements MovieInterface, OnProtectModeListener {
    private static final String TAG = ComingMovieFragment.class.getSimpleName();
    private static final int PAGESIZE = 20;


    private RelativeLayout root_comingmovie;
    private RecyclerView movie_recyclerView;
    private TextView comingmovie_tip;
    private SwipeRefreshLayout comingmovie_refresh;


    private ComingMovieRecyclerAdapter mComingMovieRecyclerAdapter;
    private List<Subjects> mSubjectsList = new ArrayList<>();
    private MoviePresenter moviePresenter = new MoviePresenter(this);

    private int pageCount;

    private boolean isLoadMore = false;
    private boolean canLoadMore = true;

    public static ComingMovieFragment newInstance() {

        Bundle args = new Bundle();

        ComingMovieFragment fragment = new ComingMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comingmovie, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        findView();
        initView();
        initData();
        initEvent();
    }

    private void findView() {
        root_comingmovie = (RelativeLayout) getActivity().findViewById(R.id.root_comingmovie);
        comingmovie_tip = (TextView) getActivity().findViewById(R.id.comingmovie_tip);
        comingmovie_refresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.comingmovie_refresh);
        movie_recyclerView = (RecyclerView) getActivity().findViewById(R.id.comingmovie_recyclerView);
    }

    private void initData() {
        moviePresenter.getComingMovie(0, PAGESIZE);
    }


    private void initView() {
        movie_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), 0);
        movie_recyclerView.addItemDecoration(spacesItemDecoration);
        mComingMovieRecyclerAdapter = new ComingMovieRecyclerAdapter(getActivity(), mSubjectsList);
        movie_recyclerView.setAdapter(mComingMovieRecyclerAdapter);

        comingmovie_refresh.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright), getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light), getResources().getColor(android.R.color.holo_red_light));

        if (SharedPreferencesUtil.getSharedPreferencesUtil(DouKit.getContext()).readBoolean("ProtectMode")) {
            root_comingmovie.setBackgroundColor(getResources().getColor(R.color.protect_color));
            movie_recyclerView.setBackgroundColor(getResources().getColor(R.color.protect_color));
        }
    }

    private void initEvent() {
        mComingMovieRecyclerAdapter.setOnRecyclerViewItemClickListener(recyclerViewItemClickListener);
        mComingMovieRecyclerAdapter.setOnRecyclerViewLoadMoreListener(movie_recyclerView, recyclerViewLoadMoreListener);
        comingmovie_refresh.setOnRefreshListener(refreshListener);
        DouKit.addOnProtectModeListener(this);
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            pageCount = 1;
            moviePresenter.getComingMovie(0, PAGESIZE);
        }
    };

    @Override
    public void getMovieSucceed(JsonMovieBean jsonMovieBean) {
        if (comingmovie_refresh.isRefreshing()) {
            mSubjectsList.clear();
            mSubjectsList.addAll(jsonMovieBean.getSubjects());
            comingmovie_refresh.setRefreshing(false);
            canLoadMore = true;
            mComingMovieRecyclerAdapter.setLoaded();
        } else {
            if (isLoadMore) {
                isLoadMore = false;
                LogUtils.log(TAG, "currentSize:" + mSubjectsList.size() + ",total:" + jsonMovieBean.getTotal(), LogUtils.DEBUG);
                if (mSubjectsList.size() >= jsonMovieBean.getTotal()) {
                    canLoadMore = false;
                }
                mSubjectsList.remove(mSubjectsList.size() - 1);
                mComingMovieRecyclerAdapter.notifyDataSetChanged();
                mComingMovieRecyclerAdapter.setLoaded();
            }
            mSubjectsList.addAll(jsonMovieBean.getSubjects());
        }
        mComingMovieRecyclerAdapter.notifyDataSetChanged();
        comingmovie_tip.setText("");
    }

    @Override
    public void getDetailSucceed(JsonDetailBean jsonDetailBean) {

    }

    @Override
    public void getStarSucceed(JsonStarBean jsonStarBean) {

    }

    @Override
    public void getFailed() {
        Toast.makeText(getActivity(), "没有请求导数据", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getError(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        LogUtils.log(TAG, throwable.getLocalizedMessage(), LogUtils.DEBUG);
        String reason = "";
        if (throwable.getLocalizedMessage().contains("Unable to resolve host")) {
            reason = "没有网络";
        } else if (throwable.getLocalizedMessage().contains("timeout")) {
            reason = "请求超时";
        } else {
            reason = "请求失败";
        }
        if (comingmovie_refresh.isRefreshing()) {
            comingmovie_refresh.setRefreshing(false);
            Toast.makeText(getActivity(), "刷新失败，" + reason, Toast.LENGTH_SHORT).show();
        }
        if (mSubjectsList.size() <= 0) {
            comingmovie_tip.setText(reason + "，点击重新尝试");
            comingmovie_tip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moviePresenter.getComingMovie(0, PAGESIZE);
                }
            });
        } else {
            if (isLoadMore) {
                isLoadMore = false;
                mSubjectsList.remove(mSubjectsList.size() - 1);
                mComingMovieRecyclerAdapter.notifyDataSetChanged();
                mComingMovieRecyclerAdapter.setLoaded();
            }
            Toast.makeText(getActivity(), "加载失败，" + reason, Toast.LENGTH_SHORT).show();
        }

    }

    private ComingMovieRecyclerAdapter.OnRecyclerViewItemClickListener recyclerViewItemClickListener = new ComingMovieRecyclerAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            LogUtils.log(TAG, mSubjectsList.get(position).getId(), LogUtils.DEBUG);
            Intent intent = new Intent();
            intent.putExtra("movieId", mSubjectsList.get(position).getId());
            intent.setClass(getActivity(), MovieDetailsActivity.class);
            startActivity(intent);
        }
    };

    private ComingMovieRecyclerAdapter.OnRecyclerViewLoadMoreListener recyclerViewLoadMoreListener = new ComingMovieRecyclerAdapter.OnRecyclerViewLoadMoreListener() {
        @Override
        public void loadMore() {
            LogUtils.log(TAG, "canLoadMore:" + canLoadMore + "pageCount:" + pageCount, LogUtils.DEBUG);
            if (canLoadMore) {
                isLoadMore = true;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mSubjectsList.add(null);
                        mComingMovieRecyclerAdapter.notifyDataSetChanged();
                    }
                });
                pageCount++;
                moviePresenter.getComingMovie(pageCount * PAGESIZE, PAGESIZE);
            } else {
/*                mSubjectsList.add(null);
                mComingMovieRecyclerAdapter.notifyDataSetChanged();*/
                mComingMovieRecyclerAdapter.setLoaded();
                Toast.makeText(getActivity(), getString(R.string.no_more), Toast.LENGTH_LONG).show();
            }
        }
    };

    public void scrollToTop() {
        if (((LinearLayoutManager) (movie_recyclerView.getLayoutManager())).findLastVisibleItemPosition() > 30) {
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
        LogUtils.log(TAG, "added:" + isAdded(), LogUtils.DEBUG);
        if (isAdded()) {
            if (isOpen) {
                root_comingmovie.setBackgroundColor(getResources().getColor(R.color.protect_color));
                movie_recyclerView.setBackgroundColor(getResources().getColor(R.color.protect_color));
            } else {
                root_comingmovie.setBackgroundColor(getResources().getColor(android.R.color.white));
                movie_recyclerView.setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        }
    }
}
