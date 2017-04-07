package com.roy.douproject.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.roy.douproject.R;
import com.roy.douproject.utils.image.ImageUtils;


/**
 * Created by 1vPy(Roy) on 2017/3/1.
 * 应用第一个过渡Activity,在MainActivity加载后加载;
 * 随机加载一个过渡图片;
 */

public class WelcomeActivity extends Activity {
    private int[] pics = {R.drawable.pic_1,R.drawable.pic_2,R.drawable.pic_3};
    private ImageView ivWelcomePic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WelcomeActivity.this.finish();
            }
        },3000);
        ivWelcomePic = (ImageView) findViewById(R.id.ivWelcomePic);
        //ImageUtils.getInstance().displayImage(WelcomeActivity.this,pics[(int)(Math.random()*pics.length)],welcome_pic);
        ivWelcomePic.setImageResource(pics[(int)(Math.random()*pics.length)]);
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
