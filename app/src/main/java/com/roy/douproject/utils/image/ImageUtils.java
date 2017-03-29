package com.roy.douproject.utils.image;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roy.douproject.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ImageUtils {
    private static ImageUtils instance;

    private ImageUtils() {
    }

    public static ImageUtils getInstance() {
        if (instance == null) {
            synchronized (ImageUtils.class) {
                if (instance == null) {
                    instance = new ImageUtils();
                }
            }
        }
        return instance;
    }

    public void displayImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public void displayCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public void displayCircleImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .dontAnimate()
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

}
