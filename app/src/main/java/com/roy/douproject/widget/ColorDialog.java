package com.roy.douproject.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.roy.douproject.R;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ColorDialog extends Dialog {
    private OnColorChangedListener colorChangedListener;
    private Context context;
    private ColorPicker picker;
    private SaturationBar saturationBar;
    private ValueBar valueBar;
    private Button confirm_color;
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
        picker = (ColorPicker) findViewById(R.id.picker);
        saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        valueBar = (ValueBar) findViewById(R.id.valuebar);
        confirm_color = (Button) findViewById(R.id.confirm_color);

        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        picker.getColor();

        picker.setOldCenterColor(oldColor);

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                colorChangedListener.colorChanged(color);
            }
        });
        picker.setShowOldCenterColor(true);
        confirm_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnColorChangedListener {
        /**
         * 回调函数
         *
         * @param color 选中的颜色
         */
        void colorChanged(int color);
    }
}
