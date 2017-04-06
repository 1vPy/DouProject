package com.roy.douproject.view.activity.movie.star;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.douproject.DouKit;
import com.roy.douproject.R;
import com.roy.douproject.bean.movie.JsonMovieBean;
import com.roy.douproject.bean.movie.details.JsonDetailBean;
import com.roy.douproject.datainterface.movie.MovieInterface;
import com.roy.douproject.presenter.movie.MoviePresenter;
import com.roy.douproject.utils.common.SharedPreferencesUtil;
import com.roy.douproject.utils.common.ThemePreference;
import com.roy.douproject.view.activity.common.WebViewActivity;
import com.roy.douproject.view.activity.movie.detail.MovieDetailsActivity;
import com.roy.douproject.view.adapter.movie.star.MajorMovieRecyclerAdapter;
import com.roy.douproject.bean.movie.star.JsonStarBean;
import com.roy.douproject.bean.movie.star.Works;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.common.ScreenUtils;
import com.roy.douproject.utils.image.ImageUtils;
import com.roy.douproject.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2017/3/13.
 */

public class StarDetailsActivity extends SwipeBackActivity implements MovieInterface{
    private static final String TAG = StarDetailsActivity.class.getSimpleName();
    private String starId;
    private JsonStarBean mJsonStarBean;

    private RelativeLayout root_stardetails;

    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    private ImageView star_image;
    private TextView star_name;
    private TextView star_en_name;
    private TextView star_gender;
    private TextView star_born_place;
    private RecyclerView major_movie;
    private Button star_more_details;

    private MajorMovieRecyclerAdapter mMajorMovieRecyclerAdapter;
    private List<Works> mWorksList = new ArrayList<>();
    private MoviePresenter moviePresenter = new MoviePresenter(this);

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stardetails);
        progressDialog = new ProgressDialog(StarDetailsActivity.this);
        progressDialog.show();
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        init();
    }

    private void init() {
        findView();
        initToolBar();
        if (getIntent() != null) {
            starId = getIntent().getStringExtra("starId");
        }
        if (starId != null) {
            moviePresenter.getStarDetail(starId);
        }
    }

    private void initToolBar() {
        //toolbar.setBackgroundColor(preferencesUtil.readInt("app_color"));
        //toolbar.setSubtitleTextColor(preferencesUtil.readInt("app_color"));
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ThemePreference.getThemePreference(DouKit.getContext()).readTheme());
        toolbar.setNavigationIcon(R.drawable.back_btn);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StarDetailsActivity.this.finish();
            }
        });
    }

    private void findView(){
        root_stardetails = (RelativeLayout) findViewById(R.id.root_stardetails);
        if(SharedPreferencesUtil.getSharedPreferencesUtil(DouKit.getContext()).readBoolean("ProtectMode")){
            root_stardetails.setBackgroundColor(getResources().getColor(R.color.protect_color));
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        star_image = (ImageView) findViewById(R.id.star_image);
        star_name = (TextView) findViewById(R.id.star_name);
        star_en_name = (TextView) findViewById(R.id.star_en_name);
        star_gender = (TextView) findViewById(R.id.star_gender);
        star_born_place = (TextView) findViewById(R.id.star_born_place);
        major_movie = (RecyclerView) findViewById(R.id.major_movie);
        star_more_details = (Button) findViewById(R.id.star_more_details);

        major_movie.setLayoutManager(new GridLayoutManager(StarDetailsActivity.this,3));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(StarDetailsActivity.this, 10), ScreenUtils.dipToPx(StarDetailsActivity.this, 10), ScreenUtils.dipToPx(StarDetailsActivity.this, 10), 0);
        major_movie.addItemDecoration(spacesItemDecoration);

        mMajorMovieRecyclerAdapter = new MajorMovieRecyclerAdapter(StarDetailsActivity.this,mWorksList);
        major_movie.setAdapter(mMajorMovieRecyclerAdapter);
    }

    private void initData(){
        toolbar.setTitle(mJsonStarBean.getName());
        ImageUtils.getInstance().displayImage(StarDetailsActivity.this,mJsonStarBean.getAvatars().getLarge(),star_image);
        star_name.setText("中文名："+mJsonStarBean.getName());
        star_en_name.setText("英文名："+mJsonStarBean.getName_en());
        star_gender.setText("性别："+mJsonStarBean.getGender());
        star_born_place.setText("出生地："+mJsonStarBean.getBorn_place());
        mWorksList.addAll(mJsonStarBean.getWorks());
        mMajorMovieRecyclerAdapter.notifyDataSetChanged();
        mMajorMovieRecyclerAdapter.setOnRecyclerViewItemClickListener(new MajorMovieRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                LogUtils.log(TAG,mWorksList.get(position).getSubject().getId(),LogUtils.DEBUG);
                Intent intent = new Intent();
                intent.putExtra("movieId",mWorksList.get(position).getSubject().getId());
                intent.setClass(StarDetailsActivity.this, MovieDetailsActivity.class);
                startActivity(intent);
            }
        });

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (v.getId()) {
                    case R.id.star_more_details:
                        intent.putExtra("url", mJsonStarBean.getMobile_url());
                        break;
                }
                intent.setClass(StarDetailsActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        };

        star_more_details.setOnClickListener(clickListener);
        progressDialog.dismiss();
    }

    @Override
    public void getMovieSucceed(JsonMovieBean jsonMovieBean) {

    }

    @Override
    public void getDetailSucceed(JsonDetailBean jsonDetailBean) {

    }

    @Override
    public void getStarSucceed(JsonStarBean jsonStarBean) {
        LogUtils.log(TAG, "json:" + jsonStarBean.getId(), LogUtils.DEBUG);
        mJsonStarBean = jsonStarBean;
        initData();
    }

    @Override
    public void getFailed() {
        LogUtils.log(TAG, "getMovieFailed", LogUtils.DEBUG);
    }

    @Override
    public void getError(Throwable throwable) {
        LogUtils.log(TAG, throwable, LogUtils.DEBUG);
    }
}
