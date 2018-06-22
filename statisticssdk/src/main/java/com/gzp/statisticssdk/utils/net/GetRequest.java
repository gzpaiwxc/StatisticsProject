package com.gzp.statisticssdk.utils.net;

import android.os.Message;
import android.util.Log;

import com.gzp.statisticssdk.utils.CustomThreadPool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

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
                    if (httpBody.getParams() != null || httpBody.getParams().size() != 0) {
                        urlPath += "?" + getParams();
                    }
                    System.out.print("请求链接==>" + urlPath);
                    URL url = new URL(urlPath);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(httpBody.getConnTimeOut());
                    conn.setReadTimeout(httpBody.getReadTimeOut());
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setUseCaches(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    if (conn.getResponseCode() == 200) {//请求成功
                        InputStream is = conn.getInputStream();
                        int len = 0;
                        byte[] buf = new byte[1024 * 1024];
                        StringBuilder sb = new StringBuilder();
                        if ((len = is.read(buf)) != -1) {
                            sb.append(new String(buf, 0, len));
                        }
                        String strUTF8 = URLDecoder.decode(sb.toString(), "gbk");
                        Log.e("Statistics", strUTF8);
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
