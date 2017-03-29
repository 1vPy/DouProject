package com.roy.douproject.view.adapter.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roy.douproject.R;
import com.roy.douproject.bean.collection.MovieCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */

public class CollectionRecyclerAdapter extends RecyclerView.Adapter<CollectionRecyclerAdapter.ItemViewHolder> {
    private Context mContext;
    private List<MovieCollection> mMovieCollectionList = new ArrayList<>();

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public CollectionRecyclerAdapter(Context context,List<MovieCollection> movieCollectionList){
        mContext = context;
        mMovieCollectionList = movieCollectionList;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
        LinearLayout item_collection_root;
        TextView collection_name;
        TextView collection_type;
        TextView collection_star;

    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_collection,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        viewHolder.item_collection_root = (LinearLayout) view.findViewById(R.id.item_collection_root);
        viewHolder.collection_name = (TextView) view.findViewById(R.id.collection_name);
        viewHolder.collection_type = (TextView) view.findViewById(R.id.collection_type);
        viewHolder.collection_star = (TextView) view.findViewById(R.id.collection_star);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.collection_name.setText(mMovieCollectionList.get(position).getMovieName());
        holder.collection_type.setText(mMovieCollectionList.get(position).getMovieType());
        holder.collection_star.setText(mMovieCollectionList.get(position).getMovieStar());
        holder.item_collection_root.setOnClickListener(new ClickListener(holder.item_collection_root,position));
    }

    @Override
    public int getItemCount() {
        return mMovieCollectionList.size();
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
