package com.gzp.statisticssdk.utils.net;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * author: Gzp
 * Create on 2018/6/12
 * Description:网络请求类
 */
public abstract class HttpRequest {

    HttpBody httpBody;
    ICommCallBack mCallback;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.WHAT_REQ_SUCCESS:
                    mCallback.onSuccess(msg.obj);
                    break;
                case Constant.WHAT_REQ_FAILD:
                    mCallback.onFailed(new Exception("网络异常"));
                    break;
                case Constant.WHAT_MALFORMED_URL_EXCEPTION:
                    mCallback.onFailed(new MalformedURLException("MalformedURLException"));
                    break;
                case Constant.WHAT_IO_EXCEPTION:
                    mCallback.onFailed(new IOException("IOException"));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 请求方法
     */
    public abstract void request();

}
