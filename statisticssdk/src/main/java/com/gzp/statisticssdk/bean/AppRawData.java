package com.gzp.statisticssdk.bean;

import java.util.Map;

/**
 * author: Gzp
 * Create on 2018/6/22
 * Description:
 */
public class AppRawData {

    /**
     * sessionTimestamp : 1527835750385
     * appVersion : 1.0
     * sessionDuration : 31634
     * carrier : chinanet guangdong province network
     * deviceIdentifiers : {"AndroidId":"e9036692ddc3f909"}
     * deviceModel : Hongmi 2 (Redmi 2)
     * deviceSubModel : HM 2A
     * countryISO : CN
     * eventName : 按钮1被点击了
     * eventOffset : 7819
     * eventParameters : {"name":"我叫王治明","time":"2018年06月01日 14:49:18"}
     * userId :
     * gender : Unknown
     * birthYear :
     * latitude :
     * longitude :
     * sessionProperties : {}
     */

    private String sessionTimestamp;
    private String appVersion;
    private String sessionDuration;
    private String carrier;
    private DeviceIdentifiersEntity deviceIdentifiers;
    private String deviceModel;
    private String deviceSubModel;
    private String countryISO;
    private String eventName;
    private String eventOffset;
    private Map<String, Object> eventParameters;
    private String userId;
    private String gender;
    private String birthYear;
    private String latitude;
    private String longitude;
    private SessionPropertiesEntity sessionProperties;

    public String getSessionTimestamp() {
        return sessionTimestamp;
    }

    public void setSessionTimestamp(String sessionTimestamp) {
        this.sessionTimestamp = sessionTimestamp;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(String sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public DeviceIdentifiersEntity getDeviceIdentifiers() {
        return deviceIdentifiers;
    }

    public void setDeviceIdentifiers(DeviceIdentifiersEntity deviceIdentifiers) {
        this.deviceIdentifiers = deviceIdentifiers;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceSubModel() {
        return deviceSubModel;
    }

    public void setDeviceSubModel(String deviceSubModel) {
        this.deviceSubModel = deviceSubModel;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventOffset() {
        return eventOffset;
    }

    public void setEventOffset(String eventOffset) {
        this.eventOffset = eventOffset;
    }

    public Map<String, Object> getEventParameters() {
        return eventParameters;
    }

    public void setEventParameters(Map<String,Object> eventParameters) {
        this.eventParameters = eventParameters;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public SessionPropertiesEntity getSessionProperties() {
        return sessionProperties;
    }

    public void setSessionProperties(SessionPropertiesEntity sessionProperties) {
        this.sessionProperties = sessionProperties;
    }

    public static class DeviceIdentifiersEntity {
        /**
         * AndroidId : e9036692ddc3f909
         */

        private String AndroidId;

        public String getAndroidId() {
            return AndroidId;
        }

        public void setAndroidId(String AndroidId) {
            this.AndroidId = AndroidId;
        }

        @Override
        public String toString() {
            return "DeviceIdentifiersEntity{" +
                    "AndroidId='" + AndroidId + '\'' +
                    '}';
        }
    }

    public static class EventParametersEntity {
        /**
         * name : 我叫王治明
         * time : 2018年06月01日 14:49:18
         */

        private String name;
        private String time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static class SessionPropertiesEntity {

    }

    @Override
    public String toString() {
        return "AppRawData{" +
                "sessionTimestamp='" + sessionTimestamp + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", sessionDuration='" + sessionDuration + '\'' +
                ", carrier='" + carrier + '\'' +
                ", deviceIdentifiers=" + deviceIdentifiers +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceSubModel='" + deviceSubModel + '\'' +
                ", countryISO='" + countryISO + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventOffset='" + eventOffset + '\'' +
                ", eventParameters=" + eventParameters +
                ", userId='" + userId + '\'' +
                ", gender='" + gender + '\'' +
                ", birthYear='" + birthYear + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", sessionProperties=" + sessionProperties +
                '}';
    }
}
