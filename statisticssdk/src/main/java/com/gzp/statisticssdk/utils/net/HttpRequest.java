package com.gzp.statisticssdk.utils.net;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
     * 获取请求的参数
     * @return
     */
    public String getParams() {
        Map<String, Object> params = httpBody.getParams();
        if (params == null || params.size() == 0) {
            return null;
        }
        String data = "";
        Set<Map.Entry<String, Object>> entrySet = params.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> me = iterator.next();
            String value = "" + me.getValue();
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            data += me.getKey() + "=" + value + "&";
        }
        data = data.substring(0, data.lastIndexOf("&"));//去掉最后一个&
        return data;
    }

    /**
     * 请求方法
     */
    public abstract void request();

}
