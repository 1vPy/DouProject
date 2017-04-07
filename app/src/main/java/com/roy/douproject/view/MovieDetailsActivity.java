package com.roy.douproject.view;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.douproject.DouKit;
import com.roy.douproject.R;
import com.roy.douproject.bean.collection.MovieCollection;
import com.roy.douproject.bean.movie.JsonMovieBean;
import com.roy.douproject.bean.movie.star.JsonStarBean;
import com.roy.douproject.support.movie.MovieInterface;
import com.roy.douproject.db.manager.DBManager;
import com.roy.douproject.presenter.movie.MoviePresenter;
import com.roy.douproject.utils.common.SharedPreferencesUtil;
import com.roy.douproject.utils.common.ThemePreference;
import com.roy.douproject.utils.common.ToastUtils;
import com.roy.douproject.view.adapter.MovieDirectorsRecyclerAdapter;
import com.roy.douproject.view.adapter.MovieStarsRecyclerAdapter;
import com.roy.douproject.bean.movie.Casts;
import com.roy.douproject.bean.movie.Directors;
import com.roy.douproject.bean.movie.details.JsonDetailBean;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 显示电影详情的activity
 * Created by 1vPy(Roy) on 2017/3/2.
 */

public class MovieDetailsActivity extends SwipeBackActivity implements MovieInterface {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private RelativeLayout root_moviedetails;
    private SwipeBackLayout mSwipeBackLayout;

    private ProgressDialog progressDialog;

    private Toolbar toolbar;
    private ImageView collect;
    private ImageView movie_poster;

    private TextView movie_title;
    private TextView movie_original_title;
    private TextView movie_type;
    private TextView movie_year;
    private TextView movie_country;
    private TextView movie_average;
    private TextView movie_watched;
    private TextView movie_want_watch;
    private TextView movie_description;

    private Button movie_more_details;
    private Button buy_movie_tickets;

    private RecyclerView movie_directors;
    private RecyclerView movie_stars;

    private MovieDirectorsRecyclerAdapter mMovieDirectorsRecyclerAdapter;
    private MovieStarsRecyclerAdapter mMovieStarsRecyclerAdapter;
    private MoviePresenter moviePresenter = new MoviePresenter(this);


    private List<MovieCollection> movieCollectionList = new ArrayList<>();
    private List<Directors> mDirectorsList = new ArrayList<>();
    private List<Casts> mCastsList = new ArrayList<>();

    private String movieId;
    private JsonDetailBean mJsonDetailBean;
    private boolean isCollected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetails);

        progressDialog = new ProgressDialog(MovieDetailsActivity.this);
        progressDialog.show();

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        init();
    }

    private void init() {
        findView();
        initToolBar();
        initView();
        if (getIntent() != null) {
            movieId = getIntent().getStringExtra("movieId");
        }
        if (movieId != null) {
            moviePresenter.getMovieDetail(movieId);
        }

        initEvent();
    }

    /**
     * 设置收藏按钮点击事件
     * 判断当前电影是否已收藏(设置对应的图标)
     */
    private void initEvent() {
        collect.setOnClickListener(clickListener);
        if (DBManager.getInstance(this).searchCollectionByMovieId(movieId) != null) {
            collect.setBackgroundResource(R.drawable.star_full);
            isCollected = true;
        }
    }

    /**
     * 设置toolbar
     */
    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ThemePreference.getThemePreference(DouKit.getContext()).readTheme());
        toolbar.setNavigationIcon(R.drawable.back_btn);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsActivity.this.finish();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void findView() {
        //layout
        root_moviedetails = (RelativeLayout) findViewById(R.id.root_moviedetails);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //imageView
        collect = (ImageView) findViewById(R.id.collect);
        movie_poster = (ImageView) findViewById(R.id.movie_poster);

        //textView
        movie_title = (TextView) findViewById(R.id.movie_title);
        movie_original_title = (TextView) findViewById(R.id.movie_original_title);
        movie_type = (TextView) findViewById(R.id.movie_type);
        movie_year = (TextView) findViewById(R.id.movie_year);
        movie_country = (TextView) findViewById(R.id.movie_country);
        movie_average = (TextView) findViewById(R.id.movie_average);
        movie_watched = (TextView) findViewById(R.id.movie_watched);
        movie_want_watch = (TextView) findViewById(R.id.movie_want_watch);
        movie_description = (TextView) findViewById(R.id.movie_description);

        //button
        movie_more_details = (Button) findViewById(R.id.movie_more_details);
        buy_movie_tickets = (Button) findViewById(R.id.buy_movie_tickets);

        //recylcerView
        movie_directors = (RecyclerView) findViewById(R.id.movie_directors);
        movie_stars = (RecyclerView) findViewById(R.id.movie_stars);
    }

    private void initView() {
        collect.setEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movie_directors.setLayoutManager(linearLayoutManager);
        mMovieDirectorsRecyclerAdapter = new MovieDirectorsRecyclerAdapter(MovieDetailsActivity.this, mDirectorsList);
        movie_directors.setAdapter(mMovieDirectorsRecyclerAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        movie_stars.setLayoutManager(linearLayoutManager2);
        mMovieStarsRecyclerAdapter = new MovieStarsRecyclerAdapter(MovieDetailsActivity.this, mCastsList);
        movie_stars.setAdapter(mMovieStarsRecyclerAdapter);

        if (SharedPreferencesUtil.getSharedPreferencesUtil(DouKit.getContext()).readBoolean("ProtectMode")) {
            root_moviedetails.setBackgroundColor(getResources().getColor(R.color.protect_color));
        }
    }

    /**
     * 设置数据
     */
    private void initData() {
        toolbar.setTitle(mJsonDetailBean.getTitle());
        ImageUtils.getInstance().displayImage(MovieDetailsActivity.this, mJsonDetailBean.getImages().getLarge(), movie_poster);
        movie_title.setText(mJsonDetailBean.getTitle());

        movie_original_title.setText(mJsonDetailBean.getOriginal_title() + "(原名)");

        String type = "";
        for (String s : mJsonDetailBean.getGenres()) {
            type = type + s + " ";
        }
        movie_type.setText("类型：" + type);

        movie_year.setText("年代：" + mJsonDetailBean.getYear());
        String country = "";
        for (String s : mJsonDetailBean.getCountries()) {
            country = country + s + " ";
        }
        movie_country.setText("国家：" + country);

        movie_average.setText("评分：" + mJsonDetailBean.getRating().getAverage());
        movie_watched.setText("看过：" + mJsonDetailBean.getCollect_count());
        movie_want_watch.setText("想看：" + mJsonDetailBean.getWish_count());
        movie_description.setText(mJsonDetailBean.getSummary());

        mDirectorsList.addAll(mJsonDetailBean.getDirectors());
        mMovieDirectorsRecyclerAdapter.notifyDataSetChanged();
        mMovieDirectorsRecyclerAdapter.setOnRecyclerViewItemClickListener(new MovieDirectorsRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.log(TAG, mDirectorsList.get(position).getId(), LogUtils.DEBUG);
                Intent intent = new Intent();
                intent.putExtra("starId", mDirectorsList.get(position).getId());
                intent.setClass(MovieDetailsActivity.this, StarDetailsActivity.class);
                startActivity(intent);
            }
        });


        mCastsList.addAll(mJsonDetailBean.getCasts());
        mMovieStarsRecyclerAdapter.notifyDataSetChanged();
        mMovieStarsRecyclerAdapter.setOnRecyclerViewItemClickListener(new MovieStarsRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.log(TAG, mCastsList.get(position).getId(), LogUtils.DEBUG);
                Intent intent = new Intent();
                intent.putExtra("starId", mCastsList.get(position).getId());
                intent.setClass(MovieDetailsActivity.this, StarDetailsActivity.class);
                startActivity(intent);
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (v.getId()) {
                    case R.id.movie_more_details:
                        intent.putExtra("url", mJsonDetailBean.getMobile_url());
                        break;
                    case R.id.buy_movie_tickets:
                        intent.putExtra("url", mJsonDetailBean.getSchedule_url());
                        break;
                }
                intent.setClass(MovieDetailsActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        };
        movie_more_details.setOnClickListener(clickListener);
        buy_movie_tickets.setOnClickListener(clickListener);
        progressDialog.dismiss();
        collect.setEnabled(true);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.collect:
                    if (isCollected) {
                        unCollect();
                    } else {
                        collect();
                    }
                    break;
            }
        }
    };

    /**
     * 收藏
     */
    private void collect() {
        DBManager.getInstance(MovieDetailsActivity.this).insertCollection(mJsonDetailBean);
        collect.setBackgroundResource(R.drawable.star_full);
        isCollected = true;
        //Toast.makeText(MovieDetailsActivity.this,getString(R.string.collect_succeed),Toast.LENGTH_LONG).show();
        ToastUtils.getInstance().showToast(MovieDetailsActivity.this, getString(R.string.collect_succeed));
    }

    /**
     * 取消收藏
     */
    private void unCollect() {
        DBManager.getInstance(MovieDetailsActivity.this).deleteCollectionByMovieId(movieId);
        collect.setBackgroundResource(R.drawable.star_empty);
        isCollected = false;
        //Toast.makeText(MovieDetailsActivity.this,getString(R.string.collect_deleted),Toast.LENGTH_LONG).show();
        ToastUtils.getInstance().showToast(MovieDetailsActivity.this, getString(R.string.collect_deleted));
    }

    @Override
    public void getMovieSucceed(JsonMovieBean jsonMovieBean) {

    }

    @Override
    public void getDetailSucceed(JsonDetailBean jsonDetailBean) {
        LogUtils.log(TAG, "json:" + jsonDetailBean.getId(), LogUtils.DEBUG);
        mJsonDetailBean = jsonDetailBean;
        initData();
    }

    @Override
    public void getStarSucceed(JsonStarBean jsonStarBean) {

    }

    @Override
    public void getFailed() {
        LogUtils.log(TAG, "getMovieFailed", LogUtils.DEBUG);
    }

    @Override
    public void getError(Throwable throwable) {
        LogUtils.log(TAG, throwable, LogUtils.DEBUG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBManager.getInstance(MovieDetailsActivity.this).closeDB();
    }
}
