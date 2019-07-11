package com.netease.neteaseplayer.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class UIUtils {
    //标准值  正常情况下应该保存在配置文件中
    public static final float STANDARD_WIDTH = 1080f;
    public static final float STANDARD_HEIGHT = 1920f;

    /*手柄起始角度*/
    public static final float ROTATION_INIT_NEEDLE = -20;

    /*唱针宽高、距离等比例*/
    public static final int NEEDLE_WIDTH = 276;
    public static final int NEEDLE_MARGIN_LEFT = 495;
    public static final int NEEDLE_PIVOT_X = 56;
    public static final int NEEDLE_PIVOT_Y = 93;
    public static final int NEEDLE_HEIGHT = 382;
    public static final int NEEDLE_MARGIN_TOP = 32;

    /*唱盘比例*/
    public static final int DISC_BG_WIDTH = 810;
    public static final int DISC_BLACK_WIDTH = 804;
    public static final int DISC_POSTER_WIDTH = 534;
    public static final int DISC_MARGIN_TOP = 210;

    //实际设备信息
    public static float displayMetricsWidth;
    public static float displayMetricsHeight;

    public static float stateBarHeight;
    ;
    private static UIUtils instance;


    public static UIUtils getInstance(Context context) {
        if (instance == null) {
            instance = new UIUtils(context);
        }
        return instance;
    }

    public static UIUtils getInstance() {
        if (instance == null) {
            throw new RuntimeException("UiUtil应该先调用含有构造方法进行初始化");
        }
        return instance;
    }

    private UIUtils(Context context) {
        if (displayMetricsWidth == 0.0f || displayMetricsHeight == 0.0f) {
            //需要得到真机上的宽高值
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            //在这里得到设备的真实值
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int systemBarHeight = getStatusBarHeight(context);
            //判断横屏还竖屏
            if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
                this.displayMetricsWidth = (float) (displayMetrics.heightPixels);
                this.displayMetricsHeight = (float) (displayMetrics.widthPixels);
            } else {
                this.displayMetricsWidth = (float) (displayMetrics.widthPixels);
                this.displayMetricsHeight = (float) (displayMetrics.heightPixels);
            }
            stateBarHeight = getStatusBarHeight(context);
        }
    }

    public float getHorizontalScaleValue() {
        return displayMetricsWidth / STANDARD_WIDTH;
    }

    public float getVerticalScaleValue() {
        return displayMetricsHeight / STANDARD_HEIGHT;
    }

    /**
     * 用于得到状态框的高度
     */
    public int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int height = context.getResources().getDimensionPixelSize(resourceId);
        if (height != -1) {
            return height;
        }
        return getValue(context, "com.android.internal.R$dimen", "status_bar_height", 48);
    }

    private int getValue(Context context, String dimeClass, String system_bar_height, int defaultValue) {

        try {
            Class<?> clz = Class.forName(dimeClass);
            Object object = clz.newInstance();
            Field field = clz.getField(system_bar_height);
            int id = Integer.parseInt(field.get(object).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public int getWidth(int width) {
        return Math.round((float) width * this.displayMetricsWidth / STANDARD_WIDTH);
    }

    public int getHeight(int height) {
        return Math.round((float) height * this.displayMetricsHeight / STANDARD_HEIGHT);
    }

    /*设备屏幕宽度*/
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /*设备屏幕高度*/
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


}
