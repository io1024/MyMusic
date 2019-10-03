package com.zero.music;

import android.app.Application;

/**
 * 应用入口
 */
public class MyApplication extends Application {

    private static MyApplication context;//全局上下文

    public static MyApplication getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
