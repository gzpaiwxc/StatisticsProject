package com.gzp.statisticssdk.utils.net;

import android.os.Message;

import com.gzp.statisticssdk.utils.CustomThreadPool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * author: Gzp
 * Create on 2018/6/13
 * Description:
 */
public class GetRequest extends HttpRequest {

    HttpURLConnection conn;

    public GetRequest(HttpBody httpBody, ICommCallBack callBack) {
        this.httpBody = httpBody;
        this.mCallback = callBack;
    }

    @Override
    public void request() {
        CustomThreadPool.getInstance().startTask(new Runnable() {
            @Override
            public void run() {
                String urlPath = httpBody.getUrl();
                try {
                    URL url = new URL(urlPath);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(httpBody.getConnTimeOut());
                    conn.setReadTimeout(httpBody.getReadTimeOut());
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoInput(true);
                    conn.setUseCaches(true);
                    if (conn.getResponseCode() == 200) {//请求成功
                        InputStream is = conn.getInputStream();
                        int len = 0;
                        byte[] buf = new byte[1024 * 1024];
                        StringBuilder sb = new StringBuilder();
                        if ((len = is.read(buf)) != -1) {
                            sb.append(new String(buf, 0, len));
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = Constant.WHAT_REQ_SUCCESS;
                        msg.obj = sb.toString();
                        mHandler.sendMessage(msg);
                    } else {
                        mHandler.sendEmptyMessage(Constant.WHAT_REQ_FAILD);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        });
    }
}
