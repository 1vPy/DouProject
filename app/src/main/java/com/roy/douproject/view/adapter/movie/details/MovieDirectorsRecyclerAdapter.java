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
import com.roy.douproject.bean.movie.Directors;
import com.roy.douproject.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class MovieDirectorsRecyclerAdapter extends RecyclerView.Adapter<MovieDirectorsRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<Directors> mDirectorsList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public MovieDirectorsRecyclerAdapter(Context context, List<Directors> directorsList){
        mContext = context;
        mDirectorsList = directorsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        RelativeLayout director_root;
        ImageView director_image;
        TextView director_name;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_details_director,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.director_root = (RelativeLayout) view.findViewById(R.id.director_root);
        viewHolder.director_image = (ImageView) view.findViewById(R.id.director_image);
        viewHolder.director_name = (TextView) view.findViewById(R.id.director_name);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mDirectorsList.get(position).getAvatars() != null){
            ImageUtils.getInstance().displayImage(mContext,mDirectorsList.get(position).getAvatars().getLarge(),holder.director_image);
        }else{
            ImageUtils.getInstance().displayImage(mContext,null,holder.director_image);
        }

        holder.director_name.setText(mDirectorsList.get(position).getName());
        holder.director_root.setOnClickListener(new ClickListener(holder.director_root,position));
    }



    @Override
    public int getItemCount() {
        return mDirectorsList.size();
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
