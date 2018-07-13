package com.taihaoli.statisticssdk;

/**
 * author: Gzp
 * Create on 2018/6/27
 * Description:
 */
public interface JsonValue {
    /**
     * 会话时间戳
     */
    String SESSION_TIME_STAMP = "sessionTimestamp";
    /**
     * 应用版本
     */
    String APP_VERSION="appVersion";
    /**
     * 会话持续时间(毫秒)
     */
    String SESSION_DURATION="sessionDuration";
    /**
     * 网络传送方
     */
    String CARRIER="carrier";
    /**
     * 设备标识androidId
     */
    String DEVICE_IDENTIFIERS="deviceIdentifiers";
    /**
     * androidId
     */
    String ANDROID_ID = "androidId";
    /**
     * 设备名称
     */
    String DEVICE_MODEL="deviceModel";
    /**
     * 设备简称
     */
    String DEVICE_SUB_MODEL="deviceSubModel";
    /**
     * 国家码
     */
    String COUNTRY_ISO="countryISO";
    /**
     * 事件名
     */
    String EVENT_NAME="eventName";
    /**
     * 事件距会话开始时间差
     */
    String EVENT_OFFSET="eventOffset";
    /**
     * 事件参数
     */
    String EVENT_PARAMETERS="eventParameters";
    /**
     * 用户Id
     */
    String USER_ID="userId";
    /**
     * 性别
     */
    String GENDER="gender";
    /**
     * 出生年月
     */
    String BIRTH_YEAR="birthYear";
    /**
     * 纬度
     */
    String LATITUDE="latitude";
    /**
     * 经度
     */
    String LONGITUDE="longitude";
    /**
     * 会话属性
     */
    String SESSION_PROPERTIES="sessionProperties";


    /**
     * 签名
     */
    String MSG_SIGNATURE = "msgSignature";
    /**
     * 时间戳
     */
    String TIME_STAMP = "timeStamp";
    /**
     * 随机数
     */
    String NONCE = "nonce";
    /**
     * 服务端返回的密文
     */
    String ENCRYPT = "encrypt";

}
