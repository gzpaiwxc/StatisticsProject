package com.taihaoli.statisticssdk.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.taihaoli.statisticssdk.JsonValue;
import com.taihaoli.statisticssdk.bean.AppRawData;
import com.taihaoli.statisticssdk.utils.Utils;
import com.taihaoli.statisticssdk.utils.app_info.AppUtils;
import com.taihaoli.statisticssdk.utils.app_info.DeviceUtils;
import com.taihaoli.statisticssdk.utils.app_info.PhoneUtils;
import com.taihaoli.statisticssdk.utils.file.FileIOUtils;
import com.taihaoli.statisticssdk.utils.net.HttpUtils;
import com.taihaoli.statisticssdk.utils.net.ICommCallBack;
import com.taihaoli.statisticssdk.utils.permission.PermissionHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * author: Gzp
 * Create on 2018/6/20
 * Description:
 */
public class StatisticsSDK {

    final static String TAG = "Statistics";

    private static final String SESSION_URL = "http://192.168.1.48:8088/sdk/data/session";
    private static final String EVENT_URL = " http://192.168.1.48:8088/sdk/data/event";

    private static String filePath = "";

    /**
     * appKey
     */
    private static String mAppKey = "taihaoli";

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

    /**
     * androidId
     */
    private static String androidId;
    /**
     * 设备厂商
     */
    private static String deviceManufacturer;

    /**
     * 设备型号
     */
    private static String deviceModel;

    /**
     * 国家码
     */
    private static String countryISO;

    /**
     * 运营商
     */
    private static String operator;

    private static long currentTime;

    /**
     * APP启动时间
     */
    private static long launchTime;

    /**
     * 会话结束时间
     */
    private static long endTime;

    /**
     * 会话时间戳
     */
    private static long sessionTimeStamp;

    /**
     * 服务端返回的时间戳
     */
    private static long serviceSessionTimeStamp;

    private static String sdkVersionName;

    private static String sdkVersionCode;

    private static AppRawData appRawData;


    /**
     * 初始化sdk
     * 在这里获取到设备的id、型号等
     * 初始化sdk后开始计时
     */
    @SuppressLint("MissingPermission")
    public static void init(Context context, String appKey) {
        filePath = FileIOUtils.getFileDir(context) + "/statistics.txt";
        Utils.init(context);
        mAppKey = appKey;
        PermissionHelper.requestPhone(new PermissionHelper.OnPermissionGrantedListener() {
            @Override
            public void onPermissionGranted() {
                IMEI = PhoneUtils.getIMEI();
            }
        });
        countryISO = DeviceUtils.getCountry(context);
        androidId = DeviceUtils.getAndroidID();
        deviceManufacturer = DeviceUtils.getManufacturer();
        deviceModel = DeviceUtils.getModel();
        operator = PhoneUtils.getSimOperatorName();
        String simOperatorByMnc = PhoneUtils.getSimOperatorByMnc();
        appVersion = String.valueOf(AppUtils.getAppVersionCode());
        //应用启动时的时间戳
        launchTime = currentTime();
        appRawData = new AppRawData();
        TimeZone.getTimeZone("GMT");
        Log.e(TAG, "androidId==>" + androidId + "  deviceManufacturer==>" + deviceManufacturer +
                "  deviceModel==>" + deviceModel + "  country==>" + countryISO + "   operator==>" + operator + "  simOperatorByMnc==>" + simOperatorByMnc
                + "  IMEI==>" + IMEI + "  appVersion==>" + appVersion + "  serviceSessionTimeStamp==>" + serviceSessionTimeStamp);
    }


    /**
     * 开始会话
     *
     * @param context
     */
    public static void startSession(Context context) {
        if (context == null) throw new NullPointerException("");
        sessionTimeStamp = currentTime();
        getAppData("", new HashMap<String, Object>());
        Map<String, Object> map = getDataMap("");
        sendDataToService(SESSION_URL, map, "");
    }

    /**
     * 结束会话
     *
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
        getAppData(eventId, map);
        dataToJson();
        Map<String, Object> dataMap = getDataMap(eventId);
        JSONObject jsonObject = new JSONObject(dataMap);
        Log.e(TAG, "==写入文件的数据dataMap==>" + jsonObject.toString());
        writeDataToFile(filePath, jsonObject.toString());

    }

    public static void postFileDataToService() {
        String readFileData = readDataFromFile(filePath);
        sendDataToService(EVENT_URL, null, readFileData);
    }

    public static void readFile() {
        String readFileData = readDataFromFile(filePath);
        Log.e(TAG, "==读出来的文件内容fileData==>" + readFileData);
    }

    public static void deleteFile() {
        deleteFile(filePath);

    }


    /**
     * 发送数据到服务端
     */
    private static void sendDataToService(String url, Map<String, Object> map, String postData) {
        HttpUtils.getInstance()
                .url(url + "?apikey=" + mAppKey)
                .addParams(map == null ? new HashMap<String, Object>() : map)
                .setPostData(postData)
                .doPostExecute(new ICommCallBack() {
                    @Override
                    public void onSuccess(Object tData) {
                        Log.e(TAG, "==返回的数据==》" + tData.toString());
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        Log.e(TAG, "==error==>" + e.toString());
                    }
                });

    }


    @NonNull
    private static Map<String, Object> getDataMap(String eventId) {
        Map<String, Object> params = new HashMap<>();
        params.put(JsonValue.SESSION_TIME_STAMP, appRawData.getSessionTimestamp());
        params.put(JsonValue.DEVICE_IDENTIFIERS, appRawData.getDeviceIdentifiers());
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
        params.put(JsonValue.SESSION_DURATION, appRawData.getSessionDuration());
        params.put(JsonValue.USER_ID, appRawData.getUserId());

        Log.e(TAG, "params==>" + params.toString());
        return params;
    }

    /**
     * 得到 app 数据对象
     *
     * @param eventId
     * @param map
     */
    private static void getAppData(String eventId, Map<String, Object> map) {
        appRawData.setCountryISO(countryISO);//国家码
        appRawData.setDeviceModel(deviceManufacturer);//设备厂商
        appRawData.setDeviceSubModel(deviceModel);//设备型号
        appRawData.setEventName(eventId);//事件名
        appRawData.setEventParameters(map);//事件集
        appRawData.setSessionTimestamp(serviceSessionTimeStamp == 0 ? String.valueOf(launchTime) : String.valueOf(serviceSessionTimeStamp));//session时间戳
        Map<String, Object> deviceIdentifiers = new HashMap<>();
        deviceIdentifiers.put(JsonValue.ANDROID_ID, androidId);
        appRawData.setDeviceIdentifiers(deviceIdentifiers);//androidId
        appRawData.setEventOffset(sessionTimeStamp == 0 ? String.valueOf((currentTime() - launchTime)) : String.valueOf((currentTime() - sessionTimeStamp)));//事件偏移时间
        appRawData.setBirthYear("");
        appRawData.setAppVersion(appVersion);
        appRawData.setCarrier(operator);
        appRawData.setGender("");
        appRawData.setLatitude("");
        appRawData.setUserId("");
        appRawData.setLongitude("");
        appRawData.setSessionDuration(endTime != 0 ? String.valueOf((endTime - launchTime)) : String.valueOf((currentTime() - launchTime)));

    }

    /**
     * 将对象转为json格式
     */
    private static void dataToJson() {
        JSONObject json = new JSONObject();
        try {
            json.put(JsonValue.SESSION_TIME_STAMP, appRawData.getSessionTimestamp());
            json.put(JsonValue.EVENT_NAME, appRawData.getEventName());
            json.put(JsonValue.APP_VERSION, "");
            json.put(JsonValue.BIRTH_YEAR, "");
            json.put(JsonValue.CARRIER, "");
            json.put(JsonValue.COUNTRY_ISO, countryISO);
            json.put(JsonValue.DEVICE_IDENTIFIERS, appRawData.getDeviceIdentifiers());
            json.put(JsonValue.DEVICE_MODEL, deviceManufacturer);
            json.put(JsonValue.DEVICE_SUB_MODEL, deviceModel);
            json.put(JsonValue.EVENT_OFFSET, appRawData.getEventOffset());
//            JSONObject jsonEventParams = new JSONObject((HashMap<String, Object>) appRawData.getEventParameters());
            JSONObject jsonEventParams = null;
            if (appRawData.getEventParameters().toString().equals("")) {
                jsonEventParams = new JSONObject();
            } else {
                jsonEventParams = new JSONObject((HashMap<String, Object>) appRawData.getEventParameters());
            }
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
        Log.e(TAG, "json===>" + json.toString());
    }

    /**
     * 将事件写入文件
     *
     * @param path
     * @param data
     */
    private static void writeDataToFile(String path, Object data) {
        FileIOUtils.writeFileFromString(path, data.toString() + ",", true);
    }


    /**
     * 从文件中把事件读出来
     *
     * @param path
     */
    private static String readDataFromFile(String path) {
        return FileIOUtils.readFile2String(path);
    }

    /**
     * 根据路径删除文件
     * @param filePath
     */
    private static void deleteFile(String filePath) {
        boolean b = FileIOUtils.deleteFile(filePath);
        Log.e(TAG, "文件是否删除成功==>" + b);
    }


    /**
     * 当前时间戳
     *
     * @return
     */
    private static long currentTime() {
        return System.currentTimeMillis();
    }

}
