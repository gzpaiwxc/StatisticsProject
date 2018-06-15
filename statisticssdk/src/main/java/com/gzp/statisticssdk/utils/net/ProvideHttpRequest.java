package com.gzp.statisticssdk.utils.net;

/**
 * author: Gzp
 * Create on 2018/6/15
 * Description:
 */
public class ProvideHttpRequest {
    HttpRequest mHttpRequest;

    public ProvideHttpRequest(HttpRequest httpRequest) {
        this.mHttpRequest = httpRequest;
    }

    public void startRequest() {
        mHttpRequest.request();
    }
}
