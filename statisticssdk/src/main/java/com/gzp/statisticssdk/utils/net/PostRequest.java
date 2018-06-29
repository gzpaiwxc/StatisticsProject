package com.gzp.statisticssdk.utils.net;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.gzp.statisticssdk.JsonValue;
import com.gzp.statisticssdk.utils.CustomThreadPool;
import com.gzp.statisticssdk.utils.MD5Util;
import com.gzp.statisticssdk.utils.rsa.RSACipherStrategy;
import com.gzp.statisticssdk.utils.rsa.RSAConstant;

import org.json.JSONException;
import org.json.JSONObject;

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
//                            conn.setRequestProperty("Content-Type", "text/plain");
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.setRequestProperty("Charset", "UTF-8");
                            conn.setRequestMethod("POST");
                            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                            outputStream.writeBytes(getPostParams());
                            outputStream.flush();
                            outputStream.close();
                            Log.e("Statistics", "====responseCode=====>" + conn.getResponseCode());
                            if (conn.getResponseCode() == 200) {//请求成功
                                InputStream inputStream = conn.getInputStream();
                                int len = 0;
                                byte[] buf = new byte[1024 * 1024];
                                StringBuilder sb = new StringBuilder();
                                if ((len = inputStream.read(buf)) != -1) {
                                    sb.append(new String(buf, 0, len));
                                }
                                inputStream.close();
                                JSONObject json = new JSONObject(sb.toString());
                                String msgSignature = json.getString(JsonValue.MSG_SIGNATURE);
                                String timeStamp = json.getString(JsonValue.TIME_STAMP);
                                String nonce = json.getString(JsonValue.NONCE);
                                String encrypt = json.getString(JsonValue.ENCRYPT);
                                Log.e("Statistics", "msgSignature==>" + msgSignature + "  timeStamp==>" + timeStamp + "  nonce==>" + nonce);
                                Log.e("Statistics", "encrypt==>" + encrypt);
                                String md5String = MD5Util.getMD5String(timeStamp + nonce + RSAConstant.MD5_KEY).toUpperCase();
                                Log.e("Statistics", "md5String==>" + md5String);
                                String decryptData = null;
                                if (TextUtils.equals(md5String, msgSignature)) {
                                    RSACipherStrategy.getInstance().initPublicKey(RSAConstant.RSA_PUBLISH_KEY);
                                    decryptData = RSACipherStrategy.getInstance().decryptByPublic(encrypt);
                                    Log.e("Statistics", "解密后的内容===》"+decryptData);
                                }

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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (conn != null) {
                                conn.disconnect();
                            }
                        }
                    }
                }
        );

    }
}
