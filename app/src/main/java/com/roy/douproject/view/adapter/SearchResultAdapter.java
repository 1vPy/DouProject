package com.roy.douproject.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roy.douproject.R;
import com.roy.douproject.bean.movie.Casts;
import com.roy.douproject.bean.movie.Subjects;
import com.roy.douproject.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public class SearchResultAdapter extends BaseAdapter {
    private Context mContext;
    private List<Subjects> mSubjectsList = new ArrayList<>();

    public SearchResultAdapter(Context context, List<Subjects> subjectsList) {
        mContext = context;
        mSubjectsList = subjectsList;
    }

    @Override
    public int getCount() {
        return mSubjectsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubjectsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_result, null);
            holder.search_image = (ImageView) convertView.findViewById(R.id.search_image);
            holder.search_name = (TextView) convertView.findViewById(R.id.search_name);
            holder.search_type = (TextView) convertView.findViewById(R.id.search_type);
            holder.search_star = (TextView) convertView.findViewById(R.id.search_star);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageUtils.getInstance().displayImage(mContext, mSubjectsList.get(position).getImages().getLarge(), holder.search_image);
        holder.search_name.setText(mSubjectsList.get(position).getTitle());
        String type = "";
        for (String s : mSubjectsList.get(position).getGenres()) {
            type = type + s + " ";
        }
        holder.search_type.setText("类型：" + type);

        String stars = "";
        for (Casts s : mSubjectsList.get(position).getCasts()) {
            stars = stars + s.getName() + " ";
        }
        holder.search_star.setText("主演："+stars);
        return convertView;
    }

    class ViewHolder {
        ImageView search_image;
        TextView search_name;
        TextView search_type;
        TextView search_star;
    }
}
