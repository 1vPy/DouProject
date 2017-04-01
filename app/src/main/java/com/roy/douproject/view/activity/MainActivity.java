package com.roy.douproject.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceAlignmentEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.roy.douproject.R;
import com.roy.douproject.utils.common.ScreenUtils;
import com.roy.douproject.utils.common.ToastUtils;
import com.roy.douproject.view.activity.common.CollectionActivity;
import com.roy.douproject.view.activity.common.LoginActivity;
import com.roy.douproject.view.activity.common.SettingActivity;
import com.roy.douproject.view.adapter.DouBaseFragmentAdapter;
import com.roy.douproject.view.fragment.MovieFragment;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout drawerLayout_main;
    private Toolbar toolbar;
    private NavigationView navigationView_main;
    private ImageView user_header;
    private Button setting;
    private Button personal_center;
    //private TabLayout tabs;

    private ViewPager viewPager;
    private DouBaseFragmentAdapter mDouBaseFragmentAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();
    private MovieFragment mMovieFragment;

    private SearchView searchView;

    private BoomMenuButton bmb;
    private int[] imgText = {R.string.back_top, R.string.collect_succeed};
    private int[] imgs = {R.drawable.back_to_top, R.drawable.star_full};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        findView();
        initToolBar();
        initView();
        initFragment();
        initEvent();
        bmbInit();
    }

    private void initToolBar() {
        //toolbar.setBackgroundColor(preferencesUtil.readInt("app_color"));
        //toolbar.setSubtitleTextColor(preferencesUtil.readInt("app_color"));
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    private void findView() {
        //tabs = (TabLayout) findViewById(R.id.tabs);
        drawerLayout_main = (DrawerLayout) findViewById(R.id.drawerLayout_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView_main = (NavigationView) findViewById(R.id.navigationView_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setting = (Button) findViewById(R.id.setting);
        personal_center = (Button) findViewById(R.id.personsl_center);

        bmb = (BoomMenuButton) findViewById(R.id.bmb);
    }

    private void initView() {
        navigationView_main.inflateHeaderView(R.layout.nav_header);
        navigationView_main.inflateMenu(R.menu.sidebar_menu);
        navigationView_main.setItemIconTintList(getResources().getColorStateList(android.R.color.black));
        navigationView_main.setItemBackgroundResource(android.R.color.transparent);
        navigationView_main.setItemTextColor(getResources().getColorStateList(android.R.color.black));
        onNavgationViewMenuItemSelected(navigationView_main);


        user_header = (ImageView) navigationView_main.getHeaderView(0).findViewById(R.id.user_header);
        ImageUtils.getInstance().displayCircleImage(MainActivity.this, R.drawable.user_header, user_header);


        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout_main, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        drawerLayout_main.setDrawerListener(mActionBarDrawerToggle);

    }

    private void initFragment() {
        mMovieFragment = MovieFragment.newInstance();
        mFragmentList.add(mMovieFragment);
        mDouBaseFragmentAdapter = new DouBaseFragmentAdapter(getSupportFragmentManager(), mTitleList, mFragmentList);
        viewPager.setAdapter(mDouBaseFragmentAdapter);
        viewPager.setOffscreenPageLimit(3);
    }

    private void initEvent() {
        LogUtils.log(TAG, "iniEvent", LogUtils.DEBUG);
        personal_center.setOnClickListener(clickListener);
        setting.setOnClickListener(clickListener);
    }

    private void bmbInit(){
        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_1);
        bmb.setButtonPlaceAlignmentEnum(ButtonPlaceAlignmentEnum.BR);
        bmb.setButtonBottomMargin(ScreenUtils.dip2px(20, this));
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.Vertical);
        LogUtils.log(TAG, "num:" + bmb.getPiecePlaceEnum().pieceNumber(), LogUtils.DEBUG);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(imgs[i])
                    .imageRect(new Rect(ScreenUtils.dip2px(5, this), ScreenUtils.dip2px(5, this), ScreenUtils.dip2px(45, this), ScreenUtils.dip2px(45, this)))
                    .rotateImage(true)
                    .pieceColorRes(android.R.color.holo_blue_light)
                    .normalColorRes(android.R.color.holo_blue_light)
                    .textSize(12)
                    .index(i)
                    .buttonRadius(ScreenUtils.dip2px(25, this))
                    .textGravity(Gravity.CENTER_HORIZONTAL)
                    .normalTextRes(imgText[i]);
            bmb.addBuilder(builder);
        }
        bmb.setOnBoomListener(boomListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.movie_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false);
        //searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.setting:
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                    break;
                case R.id.personsl_center:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
            }
        }
    };


    private OnBoomListener boomListener = new OnBoomListener() {
        @Override
        public void onClicked(int index, BoomButton boomButton) {
            LogUtils.log(TAG, "Click:" + index, LogUtils.DEBUG);
            switch (index){
                case 0:
                    mMovieFragment.backToTop();
                    break;
                case 1:
                    ToastUtils.getInstance().showToast(MainActivity.this,getString(R.string.func_not_open));
                    break;
            }
        }

        @Override
        public void onBackgroundClick() {

        }

        @Override
        public void onBoomWillHide() {

        }

        @Override
        public void onBoomDidHide() {

        }

        @Override
        public void onBoomWillShow() {

        }

        @Override
        public void onBoomDidShow() {

        }
    };

    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav
     */
    private void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
/*                    case R.id.nav_menu_home:
                        break;*/
                    case R.id.nav_menu_recommend:
                        break;
                    case R.id.nav_menu_collection:
                        startActivity(new Intent(MainActivity.this, CollectionActivity.class));
                        break;
                    case R.id.nav_menu_theme:
                        break;
                    case R.id.nav_menu_feedback:
                        break;
                    case R.id.nav_menu_about:
                        break;
                }

                // Menu item点击后选中，并关闭Drawerlayout
                menuItem.setChecked(true);
                drawerLayout_main.closeDrawers();
                // Toast.makeText(MainActivity.this,msgString,Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(isTaskRoot());
    }
}
