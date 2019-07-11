package com.netease.neteaseplayer;

import android.app.Application;

import com.netease.neteaseplayer.ui.UIUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UIUtils.getInstance(this);
    }
}
