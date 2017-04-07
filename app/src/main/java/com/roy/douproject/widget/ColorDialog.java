package com.roy.douproject.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.roy.douproject.DouKit;
import com.roy.douproject.R;
import com.roy.douproject.utils.common.ThemePreference;

/**
 * Created by 1vPy(Roy) on 2016/11/30.
 */

public class ColorDialog extends Dialog {
    private OnColorChangedListener colorChangedListener;
    private Context context;
    private ColorPicker picker;
    private SaturationBar saturationBar;
    private ValueBar valueBar;
    private Button confirm_color;
    private Button cancel_color;
    private int oldColor;

    public ColorDialog(Context context, OnColorChangedListener colorChangedListener, int oldColor) {
        super(context);
        this.context = context;
        this.colorChangedListener = colorChangedListener;
        this.oldColor = oldColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(false);
        setTitle(R.string.Theme_picker);

        picker = (ColorPicker) findViewById(R.id.picker);
        saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        valueBar = (ValueBar) findViewById(R.id.valuebar);
        confirm_color = (Button) findViewById(R.id.confirm_color);
        cancel_color = (Button) findViewById(R.id.cancel_color);

        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        picker.getColor();
        picker.setColor(ThemePreference.getThemePreference(DouKit.getContext()).readTheme());

        picker.setOldCenterColor(oldColor);

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                colorChangedListener.colorChanged(color);
            }
        });
        picker.setShowOldCenterColor(true);
        confirm_color.setOnClickListener(clickListener);
        cancel_color.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.confirm_color:
                    dismiss();
                    break;
                case R.id.cancel_color:
                    colorChangedListener.colorChanged(oldColor);
                    dismiss();
                    break;
            }
        }
    };

    public interface OnColorChangedListener {
        /**
         * 回调函数
         *
         * @param color 选中的颜色
         */
        void colorChanged(int color);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        colorChangedListener.colorChanged(oldColor);
    }
}
