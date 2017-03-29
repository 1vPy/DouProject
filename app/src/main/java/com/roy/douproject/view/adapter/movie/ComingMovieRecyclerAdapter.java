package com.roy.douproject.view.adapter.movie;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.douproject.R;
import com.roy.douproject.bean.movie.Casts;
import com.roy.douproject.bean.movie.Directors;
import com.roy.douproject.bean.movie.Subjects;
import com.roy.douproject.utils.common.LogUtils;
import com.roy.douproject.utils.common.ScreenUtils;
import com.roy.douproject.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class ComingMovieRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = ComingMovieRecyclerAdapter.class.getSimpleName();
    private static final int VIEW_ITEM = 0;
    private static final int VIEW_PROG = 1;
    private Context mContext;
    private List<Subjects> mSubjectsList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private OnRecyclerViewLoadMoreListener mOnRecyclerViewLoadMoreListener;
    private RecyclerView mRecyclerView;

    private boolean isLoading;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值
    private int visibleThreshold = 1;

    public ComingMovieRecyclerAdapter(Context context, List<Subjects> subjectsList) {
        mContext = context;
        mSubjectsList = subjectsList;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        RelativeLayout comingmovie_root;
        ImageView comingmovie_image;
        TextView comingmovie_name;
        TextView comingmovie_type;
        TextView comingmovie_directors;
        TextView comingmovie_stars;
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View itemView) {
            super(itemView);
        }

        ProgressBar progressBar;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comingmovie, parent, false);
            viewHolder = new ComingMovieRecyclerAdapter.ItemViewHolder(view);
            ((ItemViewHolder) viewHolder).comingmovie_root = (RelativeLayout) view.findViewById(R.id.comingmovie_root);
            ((ItemViewHolder) viewHolder).comingmovie_image = (ImageView) view.findViewById(R.id.comingmovie_image);
            ((ItemViewHolder) viewHolder).comingmovie_name = (TextView) view.findViewById(R.id.comingmovie_name);
            ((ItemViewHolder) viewHolder).comingmovie_type = (TextView) view.findViewById(R.id.comingmovie_type);
            ((ItemViewHolder) viewHolder).comingmovie_directors = (TextView) view.findViewById(R.id.comingmovie_directors);
            ((ItemViewHolder) viewHolder).comingmovie_stars = (TextView) view.findViewById(R.id.comingmovie_stars);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_progress, parent, false);
            viewHolder = new ComingMovieRecyclerAdapter.ProgressViewHolder(view);
            ((ProgressViewHolder) viewHolder).progressBar = (ProgressBar) view.findViewById(R.id.pb);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ViewGroup.LayoutParams params = ((ItemViewHolder) holder).comingmovie_image.getLayoutParams();
            int width = ScreenUtils.getScreenWidthDp(mContext);
            int ivWidth = (width - ScreenUtils.dipToPx(mContext, 80)) / 3;
            params.width = ivWidth;
            double height = (420.0 / 300.0) * ivWidth;
            params.height = (int) height;
            ((ItemViewHolder) holder).comingmovie_image.setLayoutParams(params);
            //Glide.with(mContext).load(mSubjectsList.get(position).getImages().getLarge()).into(holder.comingmovie_image);
            ImageUtils.getInstance().displayImage(mContext, mSubjectsList.get(position).getImages().getLarge(), ((ItemViewHolder) holder).comingmovie_image);
            ((ItemViewHolder) holder).comingmovie_name.setText(mSubjectsList.get(position).getTitle());

            String type = "";
            for (String s : mSubjectsList.get(position).getGenres()) {
                type = type + s + " ";
            }
            ((ItemViewHolder) holder).comingmovie_type.setText("类型：" + type);

            String directors = "";
            for (Directors s : mSubjectsList.get(position).getDirectors()) {
                directors = directors + s.getName() + " ";
            }
            ((ItemViewHolder) holder).comingmovie_directors.setText("导演：" + directors);

            String stars = "";
            for (Casts s : mSubjectsList.get(position).getCasts()) {
                stars = stars + s.getName() + " ";
            }
            ((ItemViewHolder) holder).comingmovie_stars.setText("主演：" + stars);

            ((ItemViewHolder) holder).comingmovie_root.setOnClickListener(new ClickListener(((ItemViewHolder) holder).comingmovie_root, position));
        } else if (holder instanceof ProgressViewHolder) {
            if (((ProgressViewHolder) holder).progressBar != null)
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return mSubjectsList == null ? 0 : mSubjectsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mSubjectsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View v, int position);
    }

    private class ClickListener implements View.OnClickListener {
        private int mPosition;
        private View mView;

        public ClickListener(View view, int position) {
            mPosition = position;
            mView = view;
        }

        @Override
        public void onClick(View v) {
            mOnRecyclerViewItemClickListener.onItemClick(mView, mPosition);
        }
    }

    public void setOnRecyclerViewLoadMoreListener(RecyclerView recyclerView, OnRecyclerViewLoadMoreListener onRecyclerViewLoadMoreListener) {
        mRecyclerView = recyclerView;
        mOnRecyclerViewLoadMoreListener = onRecyclerViewLoadMoreListener;
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //mRecyclerView添加滑动事件监听
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    LogUtils.log(TAG, "totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition + "-----" + "isLoading = " + isLoading, LogUtils.DEBUG);
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                        //此时是刷新状态
                        if (mOnRecyclerViewLoadMoreListener != null)
                            mOnRecyclerViewLoadMoreListener.loadMore();
                        isLoading = true;
                    }
                }
            });
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    public interface OnRecyclerViewLoadMoreListener {
        void loadMore();
    }
}
