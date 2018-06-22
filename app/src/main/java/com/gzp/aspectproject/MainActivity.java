package com.gzp.aspectproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzp.statisticssdk.utils.net.HttpUtils;
import com.gzp.statisticssdk.utils.net.ICommCallBack;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPassword;
    private EditText etIp;
    private TextView tvMsg;
    private Context mContext;
    final String TAG = "Statistics";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etIp = findViewById(R.id.et_ip);
        tvMsg = findViewById(R.id.tv_msg);
    }

    public void onLogin(View view) {
        Map<String, Object> params = new HashMap<>();
        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        params.put("username", account);
        params.put("password", password);
        HttpUtils.getInstance()
                .url("http://www.wanandroid.com/user/login")
                .addParams(params)
                .doPostExecute(new ICommCallBack() {
                    @Override
                    public void onSuccess(Object tData) {
                        tvMsg.setText("请求的信息==》" + tData.toString());
                        Toast.makeText(mContext, tData.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void getIpContent(View view) {
        //113.116.61.15
//        http://tj.nineton.cn/Heart/index/all?city=CHSH000000&language=zh-chs&unit=c&aqi=city&alarm=1&key=78928e706123c1a8f1766f062bc8676b
        Map<String, Object> params = new TreeMap<>();
        params.put("city", "CHSH000000");
//        params.put("language", "zh-chs");
//        params.put("unit", "c");
//        params.put("aqi", "city");
//        params.put("alarm", 1);
//        params.put("key", "78928e706123c1a8f1766f062bc8676b");
        String ip = etIp.getText().toString().trim();
        HttpUtils.getInstance()
//                .url("https://interface.meiriyiwen.com/article/today")
                .url("http://tj.nineton.cn/Heart/index/all")
//                .url("http://www.wanandroid.com/article/list/0/json")
                .addParams(params)
                .doGetExecute(new ICommCallBack() {
                    @Override
                    public void onSuccess(Object tData) {
                        Log.e(TAG, "====>" + tData.toString());
                        tvMsg.setText(tData.toString());
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
