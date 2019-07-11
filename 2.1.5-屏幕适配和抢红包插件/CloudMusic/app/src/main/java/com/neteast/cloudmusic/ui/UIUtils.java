package com.neteast.cloudmusic.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class UIUtils {
    //    工具类
    private static UIUtils instance;
    //标准值
    public static final float STANDARD_WIDTH = 1080f;
    public static final float STANDARD_HEIGHT = 1920f;
    //获取屏幕实际宽高
    public static float displayMetricsWidth;
    public static float displayMetricsHeight;
    public static float statusBarHeight;

    // applicaiton
    public static UIUtils getInstance(Context context) {
        if (instance == null) {
            instance = new UIUtils(context);
        }
        return instance;
    }

    public static UIUtils notifyInstance(Context context) {
        instance = new UIUtils(context);
        return instance;
    }

    //    activity
    public static UIUtils getInstance() {
        if (instance == null) {
            throw new RuntimeException("UiUtil应该先调用含有构造方法进行初始化");
        }
        return instance;
    }

    public UIUtils(Context context) {
        //计算缩放系数
        if (displayMetricsWidth == 0f || displayMetricsHeight == 0f) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            //注意
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            statusBarHeight = getSystemBarHeight(context);
            if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                //横屏
                this.displayMetricsWidth = displayMetrics.heightPixels;
                this.displayMetricsHeight = displayMetrics.widthPixels;
            } else {
                //竖屏
                this.displayMetricsWidth = displayMetrics.widthPixels;
                this.displayMetricsHeight = displayMetrics.heightPixels;
            }
        }
    }

    public float getHorizontalScaleValue() {
        return displayMetricsWidth / STANDARD_WIDTH;
    }

    public float getVerticalScaleValue() {

        return displayMetricsHeight / STANDARD_HEIGHT;
    }

    public int getWidth(int width) {
        return Math.round((float) width * this.displayMetricsWidth / STANDARD_WIDTH);
    }

    public int getHeight(int height) {
        return Math.round((float) height * this.displayMetricsHeight / STANDARD_HEIGHT);
    }

    public static int getSystemBarHeight(Context context) {
        return getValue(context, "com.android.internal.R$dimen", "status_bar_height", 48);
    }

    public static int getValue(Context context, String dimenClass, String system_bar_height, int defaultValue) {
        // com.android.internal.R$dimen    status_bar_height   状态栏的高度
        try {
            Class<?> clz = Class.forName(dimenClass);
            Object object = clz.newInstance();
            Field field = clz.getField(system_bar_height);
            int id = Integer.parseInt(field.get(object).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
