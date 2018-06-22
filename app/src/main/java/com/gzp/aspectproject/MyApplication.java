package com.gzp.aspectproject;

import android.app.Application;

import com.gzp.statisticssdk.sdk.StatisticsSDK;

/**
 * author: Gzp
 * Create on 2018/6/20
 * Description:
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StatisticsSDK.init(this);
    }
}
