package com.gzp.aspectproject;

import android.app.Application;

import com.gzp.aspectproject.utils.LogUtil;
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
        LogUtil.init(true);
        StatisticsSDK.init(this);
    }
}
