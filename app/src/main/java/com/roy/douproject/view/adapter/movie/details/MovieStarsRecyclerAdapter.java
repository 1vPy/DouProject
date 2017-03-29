package com.roy.douproject.view.adapter.movie.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.douproject.R;
import com.roy.douproject.bean.movie.Casts;
import com.roy.douproject.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class MovieStarsRecyclerAdapter extends RecyclerView.Adapter<MovieStarsRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<Casts> mCastsList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public MovieStarsRecyclerAdapter(Context context, List<Casts> castsList){
        mContext = context;
        mCastsList = castsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        RelativeLayout star_root;
        ImageView star_image;
        TextView star_name;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_details_star,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.star_root = (RelativeLayout) view.findViewById(R.id.star_root);
        viewHolder.star_image = (ImageView) view.findViewById(R.id.star_image);
        viewHolder.star_name = (TextView) view.findViewById(R.id.star_name);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mCastsList.get(position).getAvatars() != null){
            ImageUtils.getInstance().displayImage(mContext,mCastsList.get(position).getAvatars().getLarge(),holder.star_image);
        }else{
            ImageUtils.getInstance().displayImage(mContext,null,holder.star_image);
        }
        holder.star_name.setText(mCastsList.get(position).getName());
        holder.star_root.setOnClickListener(new ClickListener(holder.star_root,position));
    }


    @Override
    public int getItemCount() {
        return mCastsList.size();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View v,int position);
    }

    private class ClickListener implements View.OnClickListener{
        private int mPosition;
        private View mView;

        public ClickListener(View view,int position){
            mPosition = position;
            mView = view;
        }

        @Override
        public void onClick(View v) {
            mOnRecyclerViewItemClickListener.onItemClick(mView,mPosition);
        }
    }
}
