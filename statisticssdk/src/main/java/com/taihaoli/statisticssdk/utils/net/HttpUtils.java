package com.taihaoli.statisticssdk.utils.net;

import java.util.Map;

/**
 * author: Gzp
 * Create on 2018/6/11
 * Description:
 */
public class HttpUtils {

    private static HttpUtils httpUtils;

    private HttpBody httpBody;

    private HttpUtils() {
        httpBody = new HttpBody();
    }

    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    /**
     * 设置url
     * @param url
     * @return
     */
    public HttpUtils url(String url) {
        httpBody.setUrl(url);
        return this;
    }

    /**
     * 设置读取超时时间
     * @param readTimeOut
     * @return
     */
    public HttpUtils setReadTimeOut(int readTimeOut) {
        httpBody.setReadTimeOut(readTimeOut);
        return this;
    }

    /**
     * 设置连接超时时间
     * @param connTimeOut
     * @return
     */
    public HttpUtils setConnTimeOut(int connTimeOut) {
        httpBody.setConnTimeOut(connTimeOut);
        return this;
    }

    /**
     * 添加参数
     * @param params
     * @return
     */
    public HttpUtils addParams(Map<String, Object> params) {
        httpBody.setParams(params);
        return this;
    }

    /**
     * 添加参数-键值对
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParams(String key, Object value) {
        httpBody.addParams(key, value);
        return this;
    }

    public HttpUtils setPostData(String postData) {
        httpBody.setPostData(postData);
        return this;
    }


    /**
     * 执行POST请求
     * @param callBack
     * @return
     */
    public HttpUtils doPostExecute(ICommCallBack callBack) {
        ProvideHttpRequest httpRequest = new ProvideHttpRequest(new PostRequest(httpBody, callBack));
        httpRequest.startRequest();
        return this;
    }

    /**
     * 执行GET请求
     * @param callBack
     * @return
     */
    public HttpUtils doGetExecute(ICommCallBack callBack) {
        ProvideHttpRequest httpRequest = new ProvideHttpRequest(new GetRequest(httpBody, callBack));
        httpRequest.startRequest();
        return this;
    }



}
