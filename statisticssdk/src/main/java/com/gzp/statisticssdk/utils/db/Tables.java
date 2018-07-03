package com.gzp.statisticssdk.utils.db;

import android.provider.BaseColumns;

/**
 * author: Gzp
 * Create on 2018/7/3
 * Description:数据库中的表的信息
 */
public final class Tables {
    public final static String TABLE_STATISTICS = "statistics";

    public static class CACB_APP_RAW_DATA implements BaseColumns {
        public final static String CREATE_TABLE = "CREATE TABLE" + TABLE_STATISTICS
                + " (" + CACB_APP_RAW_DATA.ID + " INTEGER NOT NULL PRIMARY KEY, "
                + CACB_APP_RAW_DATA.APP_ID + " INTEGER NOT NULL, "
                + CACB_APP_RAW_DATA.APP_VERSION + " INTEGER NOT NULL"
                + CACB_APP_RAW_DATA.SESSION_TIMESTAMP + " LONG NOT NULL"
                + CACB_APP_RAW_DATA.SESSION_DURATION + " LONG NOT NULL"
                +CACB_APP_RAW_DATA.CARRIER+" CHAR(64)";


        public final static String ID = "id";//记录id
        public final static String APP_ID = "app_id";//appId
        public final static String SESSION_TIMESTAMP = "session_timestamp";//会话时间戳
        public final static String APP_VERSION = "app_version";//应用版本
        public final static String SESSION_DURATION = "session_duration";//会话持续时间(毫秒)
        public final static String CARRIER = "carrier";//网络传送方
        public final static String DEVICE_IDENTIFIERS = "device_identifiers";//设备标识
        public final static String DEVICE_MODEL = "device_model";//设备名称
        public final static String DEVICE_SUB_MODEL = "device_sub_model";//设备简称
        public final static String COUNTRY_ISO = "country_ISO";//国家
        public final static String EVENT_NAME = "event_name";//事件名
        public final static String EVENT_OFFSET = "event_offset";//事件距会话开始时间差
        public final static String EVENT_PARAMETERS = "event_parameters";//事件参数
        public final static String USER_ID = "user_id";//用户id
        public final static String GENDER = "gender";//性别
        public final static String BIRTH_YEAR = "birth_year";//出生年份
        public final static String LATITUDE = "latitude";//纬度
        public final static String LONGITUDE = "longitude";//经度
        public final static String SESSION_PROPERTIES = "session_properties";//会话属性
        public final static String SAVE_TIME = "save_time";//记录时间

    }
}
