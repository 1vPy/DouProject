package com.roy.douproject.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roy.douproject.R;
import com.roy.douproject.bean.movie.Subjects;
import com.roy.douproject.utils.common.ScreenUtils;
import com.roy.douproject.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public class HotMovieRecyclerAdapter extends RecyclerView.Adapter<HotMovieRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<Subjects> mSubjectsList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public HotMovieRecyclerAdapter(Context context, List<Subjects> subjectsList){
        mContext = context;
        mSubjectsList = subjectsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
        RelativeLayout hotmovie_root;
        ImageView hotmovie_image;
        TextView hotmovie_name;
        TextView hotmovie_average;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hotmovie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.hotmovie_root = (RelativeLayout) view.findViewById(R.id.hotmovie_root);
        viewHolder.hotmovie_image = (ImageView) view.findViewById(R.id.hotmovie_image);
        viewHolder.hotmovie_name = (TextView) view.findViewById(R.id.hotmovie_name);
        viewHolder.hotmovie_average = (TextView) view.findViewById(R.id.hotmovie_average);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewGroup.LayoutParams params=holder.hotmovie_image.getLayoutParams();
        int width= ScreenUtils.getScreenWidthDp(mContext);
        int ivWidth=(width-ScreenUtils.dipToPx(mContext,80))/3;
        params.width=ivWidth;
        double height=(420.0/300.0)*ivWidth;
        params.height=(int)height;
        holder.hotmovie_image.setLayoutParams(params);
        //Glide.with(mContext).load(mSubjectsList.get(position).getImages().getLarge()).into(holder.hotmovie_image);
        ImageUtils.getInstance().displayImage(mContext,mSubjectsList.get(position).getImages().getLarge(),holder.hotmovie_image);
        holder.hotmovie_name.setText(mSubjectsList.get(position).getTitle());
        if(TextUtils.isEmpty(mSubjectsList.get(position).getRating().getAverage()+"")){
            holder.hotmovie_average.setText("暂无评分");
        }else{
            holder.hotmovie_average.setText("评分："+String.valueOf(mSubjectsList.get(position).getRating().getAverage()));
        }
        holder.hotmovie_root.setOnClickListener(new ClickListener(holder.hotmovie_root,position));
    }


    @Override
    public int getItemCount() {
        return mSubjectsList.size();
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
