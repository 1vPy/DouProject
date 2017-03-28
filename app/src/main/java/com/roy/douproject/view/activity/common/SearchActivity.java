package com.roy.douproject.view.activity.common;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.douproject.R;
import com.roy.douproject.bean.movie.details.JsonDetailBean;
import com.roy.douproject.bean.movie.star.JsonStarBean;
import com.roy.douproject.datainterface.movie.MovieInterface;
import com.roy.douproject.presenter.movie.MoviePresenter;
import com.roy.douproject.view.activity.movie.detail.MovieDetailsActivity;
import com.roy.douproject.view.adapter.SearchResultAdapter;
import com.roy.douproject.api.ApiFactory;
import com.roy.douproject.bean.movie.JsonMovieBean;
import com.roy.douproject.bean.movie.Subjects;
import com.roy.douproject.utils.common.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/17.
 */

public class SearchActivity extends AppCompatActivity implements MovieInterface{
    private static final String TAG = SearchActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ListView search_result;

    private SearchResultAdapter mSearchResultAdapter;
    private List<Subjects> mSubjectsList = new ArrayList<>();
    private TextView search_tip;

    private String query;
    private MoviePresenter moviePresenter = new MoviePresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        dealSearch();
    }

    private void dealSearch() {
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            query = getIntent().getStringExtra(SearchManager.QUERY);
            LogUtils.log(TAG, "searchKey:" + query, LogUtils.DEBUG);
        }
        if (!query.isEmpty()) {
            moviePresenter.searchMovie(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    private void init() {
        findView();
        initToolBar();
        initView();
        initEvent();
    }

    private void initToolBar() {
        //toolbar.setBackgroundColor(preferencesUtil.readInt("app_color"));
        //toolbar.setSubtitleTextColor(preferencesUtil.readInt("app_color"));
        toolbar.setTitle("搜索结果");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        search_result = (ListView) findViewById(R.id.search_result);
        search_tip = (TextView) findViewById(R.id.search_tip);
    }

    private void initView() {
        mSearchResultAdapter = new SearchResultAdapter(SearchActivity.this, mSubjectsList);
        search_result.setAdapter(mSearchResultAdapter);
    }

    private void initEvent() {
        search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.log(TAG, mSubjectsList.get(position).getId(), LogUtils.DEBUG);
                Intent intent = new Intent();
                intent.putExtra("movieId", mSubjectsList.get(position).getId());
                intent.setClass(SearchActivity.this, MovieDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void getMovieSucceed(JsonMovieBean jsonMovieBean) {
        mSubjectsList.addAll(jsonMovieBean.getSubjects());
        LogUtils.log(TAG, "mSubjectsList:" + mSubjectsList.size(), LogUtils.DEBUG);
        if (mSubjectsList.size() <= 0) {
            search_tip.setText("T.T，没有搜索结果");
        }
        mSearchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDetailSucceed(JsonDetailBean jsonDetailBean) {

    }

    @Override
    public void getStarSucceed(JsonStarBean jsonStarBean) {

    }

    @Override
    public void getFailed() {

    }

    @Override
    public void getError(Throwable throwable) {
        Toast.makeText(SearchActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogUtils.log(TAG,"onCreateOptionsMenu:"+ Looper.getMainLooper(),LogUtils.DEBUG);
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.movie_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        //searchView.setOnQueryTextListener(queryTextListener);
        RxSearchView.queryTextChanges(searchView)
                .debounce(2, TimeUnit.SECONDS)
                .flatMap(new Function<CharSequence, Observable<JsonMovieBean>>() {
                    @Override
                    public Observable<JsonMovieBean> apply(CharSequence charSequence) throws Exception {
                        return ApiFactory.getDouMovieApi().searchMovie(charSequence.toString());
                    }
                })
                //.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonMovieBean>() {
                    @Override
                    public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                        LogUtils.log(TAG,jsonMovieBean.getTitle()+jsonMovieBean.getTotal(),LogUtils.DEBUG);
                        if(jsonMovieBean.getTotal() > 0){
                            search_result.setVisibility(View.VISIBLE);
                            mSubjectsList.addAll(jsonMovieBean.getSubjects());
                            mSearchResultAdapter.notifyDataSetChanged();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }else{
                            search_result.setVisibility(View.GONE);
                        }

                    }
                });
        return true;
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            ApiFactory.getDouMovieApi().searchMovie(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<JsonMovieBean>() {
                        @Override
                        public void accept(JsonMovieBean jsonMovieBean) throws Exception {
                            LogUtils.log(TAG,jsonMovieBean.getTitle()+jsonMovieBean.getTotal(),LogUtils.DEBUG);
                            if(jsonMovieBean.getTotal() > 0){
                                search_result.setVisibility(View.VISIBLE);
                                mSubjectsList.addAll(jsonMovieBean.getSubjects());
                                mSearchResultAdapter.notifyDataSetChanged();
                            }else{
                                search_result.setVisibility(View.GONE);
                            }

                        }
                    });
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };*/
}
