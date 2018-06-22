package com.gzp.statisticssdk.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.gzp.statisticssdk.utils.DeviceUtils;
import com.gzp.statisticssdk.utils.PhoneUtils;
import com.gzp.statisticssdk.utils.Utils;
import com.gzp.statisticssdk.utils.net.HttpUtils;

import org.json.JSONObject;

import java.util.Locale;

/**
 * author: Gzp
 * Create on 2018/6/20
 * Description:
 */
public class StatisticsSDK {

    final static String TAG = "Statistics";

    /**
     * 设备id
     */
    private static String deviceId;
    /**
     * IMEI
     */
    private static String IMEI;

    /**
     * 应用版本
     */
    private static String appVersion;

    private static String androidId;
    /**
     * 设备厂商
     */
    private static String deviceManufacturer;

    /**
     * 设备型号
     */
    private static String deviceModel;

    private static String brand;

    private static long currentTime;

    private static String sdkVersionName;

    private static String sdkVersionCode;


    /**
     * 初始化sdk
     * 在这里获取到设备的id、型号等
     * 初始化sdk后开始计时
     */
    @SuppressLint("MissingPermission")
    public static void init(Context context) {
        Utils.init(context);
        String country = getCountry(context);
        androidId = DeviceUtils.getAndroidID();
        deviceManufacturer = DeviceUtils.getManufacturer();
        deviceModel = DeviceUtils.getModel();
        brand = DeviceUtils.getBrand();
        Log.e(TAG, "androidId==>" + androidId + "  deviceManufacturer==>" + deviceManufacturer + "  deviceModel==>" + deviceModel + "  brand==>" + brand + "  country==>" + country);
        Log.e(TAG, "USER==》" + Build.USER + "  BOOTLOADER==》" + Build.BOOTLOADER);
    }


    /**
     * 开始会话
     * @param context
     */
    public static void startSession(Context context) {

    }

    /**
     * 结束会话
     * @param context
     */
    public static void endSession(Context context) {

    }

    /**
     * 事件记录
     */
    public static void logEvent() {
        JSONObject json = new JSONObject();

        //        HttpUtils.getInstance().url("").
    }


    /**
     * 当前时间戳
     * @return
     */
    private long currentTime() {
        return System.currentTimeMillis() / 1000;
    }


    private static String getCountry(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.getCountry();
    }

}
