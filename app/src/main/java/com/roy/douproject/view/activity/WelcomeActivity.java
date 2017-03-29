package com.roy.douproject.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.roy.douproject.R;
import com.roy.douproject.utils.image.ImageUtils;


/**
 * Created by Administrator on 2017/3/1.
 */

public class WelcomeActivity extends Activity {
    private int[] pics = {R.drawable.pic_1,R.drawable.pic_2,R.drawable.pic_3};
    private ImageView welcome_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DouMovieFactory.newInstance().createApi().getHotMovie();
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WelcomeActivity.this.finish();
            }
        },3000);
        welcome_pic = (ImageView) findViewById(R.id.welcome_pic);
        ImageUtils.getInstance().displayImage(WelcomeActivity.this,pics[(int)(Math.random()*pics.length)],welcome_pic);
        //welcome_pic.setImageResource(pics[(int)(Math.random()*pics.length)]);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
