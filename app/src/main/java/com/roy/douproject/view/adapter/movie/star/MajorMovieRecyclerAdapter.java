package com.roy.douproject.view.adapter.movie.star;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.douproject.R;
import com.roy.douproject.bean.movie.star.Works;
import com.roy.douproject.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */

public class MajorMovieRecyclerAdapter extends RecyclerView.Adapter<MajorMovieRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<Works> mWorksList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public MajorMovieRecyclerAdapter(Context context, List<Works> worksList) {
        mContext = context;
        mWorksList = worksList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        RelativeLayout major_root;
        ImageView movie_image;
        TextView movie_name;
        TextView movie_avg;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_major_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.major_root = (RelativeLayout) view.findViewById(R.id.major_root);
        viewHolder.movie_image = (ImageView) view.findViewById(R.id.movie_image);
        viewHolder.movie_name = (TextView) view.findViewById(R.id.movie_name);
        viewHolder.movie_avg = (TextView) view.findViewById(R.id.movie_avg);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUtils.newInstance().displayImage(mContext, mWorksList.get(position).getSubject().getImages().getLarge(), holder.movie_image);
        holder.movie_name.setText(mWorksList.get(position).getSubject().getTitle() + "(" + mWorksList.get(position).getSubject().getYear() + ")");
        holder.movie_avg.setText("评分：" + mWorksList.get(position).getSubject().getRating().getAverage());
        holder.major_root.setOnClickListener(new ClickListener(holder.major_root,position));
    }

    @Override
    public int getItemCount() {
        return mWorksList.size();
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

}
