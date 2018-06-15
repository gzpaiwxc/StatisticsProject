package com.gzp.aspectproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzp.statisticssdk.utils.net.HttpUtils;
import com.gzp.statisticssdk.utils.net.ICommCallBack;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPassword;
    private TextView tvMsg;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
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
}
