package com.roy.douproject.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.michaldrabik.tapbarmenulib.TapBarMenu;
import com.roy.douproject.R;
import com.roy.douproject.view.activity.common.CollectionActivity;
import com.roy.douproject.view.activity.common.LoginActivity;
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

    private TapBarMenu tapBarMenu;
    private ImageView back_to_top;

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
    }

    private void initToolBar() {
        //toolbar.setBackgroundColor(preferencesUtil.readInt("app_color"));
        //toolbar.setSubtitleTextColor(preferencesUtil.readInt("app_color"));
        toolbar.setTitle(getString(R.string.app_name));
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

        tapBarMenu = (TapBarMenu) findViewById(R.id.tapBarMenu);
        back_to_top = (ImageView) findViewById(R.id.back_to_top);
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
        tapBarMenu.setOnClickListener(clickListener);
        back_to_top.setOnClickListener(clickListener);
/*        RxSearchView.queryTextChanges(searchView)
                .debounce(2, TimeUnit.SECONDS)
                .flatMap(new Function<CharSequence, Observable<JsonMovieBean>>() {
                    @Override
                    public Observable<JsonMovieBean> apply(CharSequence charSequence) throws Exception {
                        return ApiFactory.getDouMovieApi().searchMovie(charSequence.toString());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(@NonNull JsonMovieBean jsonMovieBean) throws Exception {
                        LogUtils.log(TAG,jsonMovieBean.getTitle()+jsonMovieBean.getCount(),LogUtils.DEBUG);
                    }
                });*/
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
                    break;
                case R.id.personsl_center:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
                case R.id.tapBarMenu:
                    tapBarMenu.toggle();
                    break;
                case R.id.back_to_top:
                    mMovieFragment.backToTop();
                    tapBarMenu.close();
                    break;
            }
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
