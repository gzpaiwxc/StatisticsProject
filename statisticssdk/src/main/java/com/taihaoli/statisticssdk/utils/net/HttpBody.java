package com.taihaoli.statisticssdk.utils.net;

import java.util.HashMap;
import java.util.Map;

/**
 * author: Gzp
 * Create on 2018/6/13
 * Description:
 */
public class HttpBody {
    /**
     * 请求的url
     */
    private String url;

    /**
     * 请求的参数
     */
    private Map<String, Object> params = new HashMap<>();

    /**
     * 请求的参数，一段文本内容
     */
    private String postData;

    /**
     * 读取超时时间，默认10秒
     */
    private int readTimeOut = 10 * 1000;
    /**
     * 连接超时时间，默认10秒
     */
    private int connTimeOut = 10 * 1000;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public HttpBody addParams(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnTimeOut() {
        return connTimeOut;
    }

    public void setConnTimeOut(int connTimeOut) {
        this.connTimeOut = connTimeOut;
    }
}
