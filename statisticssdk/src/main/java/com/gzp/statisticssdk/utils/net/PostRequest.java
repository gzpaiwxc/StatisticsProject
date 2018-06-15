package com.gzp.statisticssdk.utils.net;

import android.os.Message;

import com.gzp.statisticssdk.utils.CustomThreadPool;

import java.io.DataOutputStream;
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
public class PostRequest extends HttpRequest{
    private HttpURLConnection conn;

    public PostRequest(HttpBody httpBody, ICommCallBack callBack) {
        this.httpBody = httpBody;
        this.mCallback = callBack;
    }


    @Override
    public void request() {
        CustomThreadPool.getInstance().startTask(
                new Runnable() {
                    @Override
                    public void run() {
                        String urlPath = httpBody.getUrl();
                        try {
                            URL url = new URL(urlPath);
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(httpBody.getReadTimeOut());
                            conn.setConnectTimeout(httpBody.getConnTimeOut());
                            conn.setUseCaches(true);
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.setRequestMethod("POST");
                            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                            outputStream.writeBytes(httpBody.getParams().toString());
                            outputStream.flush();
                            outputStream.close();
                            if (conn.getResponseCode() == 200) {//请求成功
                                InputStream inputStream = conn.getInputStream();
                                int len = 0;
                                byte[] buf = new byte[1024 * 1024];
                                StringBuilder sb = new StringBuilder();
                                if ((len = inputStream.read(buf)) != -1) {
                                    sb.append(new String(buf, 0, len));
                                }
                                inputStream.close();
                                Message msg = mHandler.obtainMessage();
                                msg.obj = sb.toString();
                                msg.what = Constant.WHAT_REQ_SUCCESS;
                                mHandler.sendMessage(msg);
                            } else {
                                mHandler.sendEmptyMessage(Constant.WHAT_REQ_FAILD);
                            }
                        } catch (MalformedURLException e) {
                            mHandler.sendEmptyMessage(Constant.WHAT_MALFORMED_URL_EXCEPTION);
                            e.printStackTrace();
                        } catch (IOException e) {
                            mHandler.sendEmptyMessage(Constant.WHAT_IO_EXCEPTION);
                            e.printStackTrace();
                        }finally {
                            if (conn != null) {
                                conn.disconnect();
                            }
                        }
                    }
                }
        );

    }
}
