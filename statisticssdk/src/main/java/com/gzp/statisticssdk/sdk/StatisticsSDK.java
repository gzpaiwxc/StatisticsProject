package com.gzp.statisticssdk.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.gzp.statisticssdk.JsonValue;
import com.gzp.statisticssdk.bean.AppRawData;
import com.gzp.statisticssdk.utils.phone.DeviceUtils;
import com.gzp.statisticssdk.utils.Utils;
import com.gzp.statisticssdk.utils.net.HttpUtils;
import com.gzp.statisticssdk.utils.net.ICommCallBack;
import com.gzp.statisticssdk.utils.phone.PhoneUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    /**
     * 国家码
     */
    private static String countryISO;

    private static String operator;

    private static long currentTime;

    private static long launchTime;

    private static long endTime;

    private static long sessionTimeStamp;

    private static String sdkVersionName;

    private static String sdkVersionCode;

    private static AppRawData appRawData;


    /**
     * 初始化sdk
     * 在这里获取到设备的id、型号等
     * 初始化sdk后开始计时
     */
    @SuppressLint("MissingPermission")
    public static void init(Context context) {
        Utils.init(context);
        countryISO = getCountry(context);
        androidId = DeviceUtils.getAndroidID();
        deviceManufacturer = DeviceUtils.getManufacturer();
        deviceModel = DeviceUtils.getModel();
        brand = DeviceUtils.getBrand();
        operator = PhoneUtils.getSimOperatorName();
        //应用启动时的时间戳
        launchTime = currentTime();
        appRawData = new AppRawData();
        Log.e(TAG, "androidId==>" + androidId + "  deviceManufacturer==>" + deviceManufacturer +
                "  deviceModel==>" + deviceModel + "  brand==>" + brand + "  country==>" + countryISO + "   operator==>" + operator);
        Log.e(TAG, "USER==》" + Build.USER + "  BOOTLOADER==》" + Build.BOOTLOADER);
    }


    /**
     * 开始会话
     * @param context
     */
    public static void startSession(Context context) {
        sessionTimeStamp = currentTime();
    }

    /**
     * 结束会话
     * @param context
     */
    public static void endSession(Context context) {
        endTime = currentTime();
        //
        long sessionDuration = endTime - launchTime;
        appRawData.setSessionDuration(String.valueOf(sessionDuration));

    }

    public static void logEvent(String eventId) {
        logEvent(eventId, null);
    }


    /**
     * 事件记录
     */
    public static void logEvent(String eventId, Map<String, Object> map) {
        appRawData.setCountryISO(countryISO);//国家码
        appRawData.setDeviceModel(deviceManufacturer);//设备厂商
        appRawData.setDeviceSubModel(deviceModel);//设备型号
        appRawData.setEventName(eventId);//事件名
//        appRawData.getEventParameters().putAll(map == null ? new HashMap<String, Object>() : map);
        appRawData.setEventParameters(map == null ? new HashMap<String, Object>() : map);//事件集
        appRawData.setSessionTimestamp(sessionTimeStamp == 0 ? String.valueOf(launchTime) : String.valueOf(launchTime));//session时间戳
        AppRawData.DeviceIdentifiersEntity deviceIdentifiersEntity = new AppRawData.DeviceIdentifiersEntity();
        deviceIdentifiersEntity.setAndroidId(androidId);
        appRawData.setDeviceIdentifiers(deviceIdentifiersEntity);//androidId
        appRawData.setEventOffset(sessionTimeStamp == 0 ? String.valueOf(currentTime() - launchTime) : String.valueOf(currentTime() - sessionTimeStamp));//事件偏移时间
        JSONObject json = new JSONObject();
        try {
            json.put(JsonValue.SESSION_TIME_STAMP, appRawData.getSessionTimestamp());
            json.put(JsonValue.EVENT_NAME, appRawData.getEventName());
            json.put(JsonValue.APP_VERSION, "");
            json.put(JsonValue.BIRTH_YEAR, "");
            json.put(JsonValue.CARRIER, "");
            json.put(JsonValue.COUNTRY_ISO, countryISO);
            Map<String, Object> mapDeviceIdentifiers = new HashMap<>();
            mapDeviceIdentifiers.put(JsonValue.ANDROID_ID, appRawData.getDeviceIdentifiers().getAndroidId());
            JSONObject jsonDeviceIdentifiers = new JSONObject(mapDeviceIdentifiers);
            json.put(JsonValue.DEVICE_IDENTIFIERS, jsonDeviceIdentifiers);
            json.put(JsonValue.DEVICE_MODEL, deviceManufacturer);
            json.put(JsonValue.DEVICE_SUB_MODEL, deviceModel);
            json.put(JsonValue.EVENT_OFFSET, appRawData.getEventOffset());
            JSONObject jsonEventParams = new JSONObject(appRawData.getEventParameters());
            json.put(JsonValue.EVENT_PARAMETERS, jsonEventParams);
            json.put(JsonValue.GENDER, "");
            json.put(JsonValue.LATITUDE, "");
            json.put(JsonValue.LONGITUDE, "");
            json.put(JsonValue.SESSION_DURATION, "");
            json.put(JsonValue.SESSION_PROPERTIES, "");
            json.put(JsonValue.USER_ID, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "appRawData===>" + appRawData.toString());
        Log.e(TAG, "json===>"+json.toString());

        Map<String, Object> mapDeviceIdentifiers = new HashMap<>();
        mapDeviceIdentifiers.put(JsonValue.ANDROID_ID, appRawData.getDeviceIdentifiers().getAndroidId());
        Map<String, Object> params = new HashMap<>();
        params.put(JsonValue.SESSION_TIME_STAMP, appRawData.getSessionTimestamp());
        params.put(JsonValue.DEVICE_IDENTIFIERS, mapDeviceIdentifiers);
        params.put(JsonValue.APP_VERSION, appRawData.getAppVersion());
        params.put(JsonValue.BIRTH_YEAR, appRawData.getBirthYear());
        params.put(JsonValue.CARRIER, appRawData.getCarrier());
        params.put(JsonValue.COUNTRY_ISO, appRawData.getCountryISO());
        params.put(JsonValue.DEVICE_MODEL, appRawData.getDeviceModel());
        params.put(JsonValue.DEVICE_SUB_MODEL, appRawData.getDeviceSubModel());
        params.put(JsonValue.EVENT_NAME, eventId);
        params.put(JsonValue.EVENT_OFFSET, appRawData.getEventOffset());
        params.put(JsonValue.EVENT_PARAMETERS, appRawData.getEventParameters());
        params.put(JsonValue.GENDER, appRawData.getGender());
        params.put(JsonValue.LATITUDE, appRawData.getLatitude());
        params.put(JsonValue.LONGITUDE, appRawData.getLongitude());
        params.put(JsonValue.SESSION_DURATION, appRawData.getSessionTimestamp());
        params.put(JsonValue.SESSION_PROPERTIES, appRawData.getSessionProperties());
        params.put(JsonValue.USER_ID, appRawData.getUserId());

        Log.e(TAG, "params==>" + params.toString());
        HttpUtils.getInstance()
                .url("http://192.168.1.52:8088/sdk/data/info")
        .addParams(params)
        .doPostExecute(new ICommCallBack() {
            @Override
            public void onSuccess(Object tData) {
                Log.e(TAG, "===data===" + tData.toString());
            }

            @Override
            public void onFailed(Throwable e) {
                Log.e(TAG, "===throwable==" + e.toString());
            }
        })

        ;
    }


    /**
     * 当前时间戳
     * @return
     */
    private static long currentTime() {
        return System.currentTimeMillis() / 1000;
    }


    /**
     * 获取国家码
     * @param context
     * @return
     */
    private static String getCountry(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.getCountry();
    }

}
