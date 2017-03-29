package com.roy.douproject.view.activity.common;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.douproject.R;
import com.roy.douproject.bean.collection.MovieCollection;
import com.roy.douproject.db.manager.DBManager;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.common.ScreenUtils;
import com.roy.douproject.view.activity.movie.detail.MovieDetailsActivity;
import com.roy.douproject.view.adapter.common.CollectionRecyclerAdapter;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class CollectionActivity extends SwipeBackActivity {
    private static final String TAG = CollectionActivity.class.getSimpleName();
    private Toolbar toolbar;
    private Button collect_menu;
    private RecyclerView collection_list;
    private TextView collection_tip;

    private TopRightMenu mTopRightMenu;

    private CollectionRecyclerAdapter mCollectionRecyclerAdapter;
    private List<MovieCollection> mMovieCollectionList = new ArrayList<>();
    private List<MovieCollection> mLoadMovieCollectionList = new ArrayList<>();

    List<MenuItem> menuItems = new ArrayList<>();

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        init();
    }

    private void init() {
        findView();
        initToolBar();
        initEvent();
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collect_menu = (Button) findViewById(R.id.collect_menu);
        collection_tip = (TextView) findViewById(R.id.collection_tip);
        collection_list = (RecyclerView) findViewById(R.id.collection_list);
        collection_list.setLayoutManager(new LinearLayoutManager(CollectionActivity.this));
        mCollectionRecyclerAdapter = new CollectionRecyclerAdapter(CollectionActivity.this, mMovieCollectionList);
        collection_list.setAdapter(mCollectionRecyclerAdapter);

        mTopRightMenu = new TopRightMenu(CollectionActivity.this);
        menuItems.add(new MenuItem("清空收藏"));

        mTopRightMenu
                .setHeight((int) (ScreenUtils.getScreenHeightDp(CollectionActivity.this) * 0.07813) * menuItems.size())     //默认高度480
                .setWidth((int) (ScreenUtils.getScreenWidthDp(CollectionActivity.this) * 0.44445))      //默认宽度wrap_content
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(menuItems);
    }

    private void initToolBar() {
        //toolbar.setBackgroundColor(preferencesUtil.readInt("app_color"));
        //toolbar.setSubtitleTextColor(preferencesUtil.readInt("app_color"));
        toolbar.setTitle(getString(R.string.local_collection));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionActivity.this.finish();
            }
        });
    }

    private void initEvent() {
        mCollectionRecyclerAdapter.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
        collect_menu.setOnClickListener(clickListener);
        mTopRightMenu.setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position) {
                Toast.makeText(CollectionActivity.this, "点击菜单:" + menuItems.get(position).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMovieCollectionList.clear();
        mLoadMovieCollectionList = DBManager.getInstance(CollectionActivity.this).searchCollection();
        if (mLoadMovieCollectionList != null && mLoadMovieCollectionList.size() > 0) {
            LogUtils.log(TAG, "size:" + mLoadMovieCollectionList.size(), LogUtils.DEBUG);
            collection_tip.setVisibility(View.GONE);
            mMovieCollectionList.addAll(mLoadMovieCollectionList);
            mCollectionRecyclerAdapter.notifyDataSetChanged();
        } else {
            collection_tip.setVisibility(View.VISIBLE);
        }
    }

    private CollectionRecyclerAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = new CollectionRecyclerAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            LogUtils.log(TAG, mMovieCollectionList.get(position).getMovieId(), LogUtils.DEBUG);
            Intent intent = new Intent();
            intent.putExtra("movieId", mMovieCollectionList.get(position).getMovieId());
            intent.setClass(CollectionActivity.this, MovieDetailsActivity.class);
            startActivity(intent);
        }
    };


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.collect_menu:
                    showMenu();
                    break;
            }
        }
    };

    private void showMenu() {
        mTopRightMenu.showAsDropDown(collect_menu, -(int) (ScreenUtils.getScreenWidthDp(CollectionActivity.this) * 0.36111), (int) (ScreenUtils.getScreenHeightDp(CollectionActivity.this) * 0.01953));
    }

}
