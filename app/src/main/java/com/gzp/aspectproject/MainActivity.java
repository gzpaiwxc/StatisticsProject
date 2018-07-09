package com.gzp.aspectproject;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzp.aspectproject.utils.LogUtil;
import com.gzp.statisticssdk.sdk.StatisticsSDK;
import com.gzp.statisticssdk.utils.file.FileIOUtils;
import com.gzp.statisticssdk.utils.file.FileUtils;
import com.gzp.statisticssdk.utils.net.HttpUtils;
import com.gzp.statisticssdk.utils.net.ICommCallBack;
import com.gzp.statisticssdk.utils.permission.PermissionHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

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
        initView();
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etIp = findViewById(R.id.et_ip);
        tvMsg = findViewById(R.id.tv_msg);
        Button btnToRSA = findViewById(R.id.btn_to_rsa);
        Button btnTest = findViewById(R.id.btn_test);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnGetContent = findViewById(R.id.btn_get_content);
        Button btnTestPermission = findViewById(R.id.btn_test_permission);
        Button btnTestFile = findViewById(R.id.btn_test_file);
        Button btnTestRead = findViewById(R.id.btn_test_read);
        Button btnTestDelete = findViewById(R.id.btn_test_deleted);
        Button btnTestPostData = findViewById(R.id.btn_test_post_file_data);
        btnTestPostData.setOnClickListener(this);
        btnTestDelete.setOnClickListener(this);
        btnToRSA.setOnClickListener(this);
        btnTest.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnGetContent.setOnClickListener(this);
        btnTestPermission.setOnClickListener(this);
        btnTestFile.setOnClickListener(this);
        btnTestRead.setOnClickListener(this);
    }


    private void onLogin() {
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

    private void getContent() {
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


    private void onIntentToActivity() {
        Intent intent = new Intent(MainActivity.this, RSATestActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        StatisticsSDK.startSession(this);
//        StatisticsSDK.logEvent("app启动");
    }

    @Override
    protected void onStop() {
        super.onStop();
        StatisticsSDK.endSession(this);
    }

    private void test() {
        Map<String, Object> params = new HashMap<>();
        long timel = SystemClock.elapsedRealtime();
        params.put("name", "hello");
        params.put("time", String.valueOf(timel));
        params.put("hello", "hello world");
        StatisticsSDK.logEvent("点击测试按钮", params);
    }

    private void testPermission() {
        PermissionHelper.requestPhone(new PermissionHelper.OnPermissionGrantedListener() {
            @Override
            public void onPermissionGranted() {
                LogUtil.e("权限申请成功");
            }
        });
    }

    private void testFile() {
        //        String sdCardResourceDir = FileUtils.createSDCardResourceDir(this);
//        File fileDir = FileUtils.getFileDir(this);
//        String filePath = FileUtils.FILE_PATH + "/test.txt";
//        String filePath2 = FileUtils.FILE_PATH2;
//        String filePath3 = FileUtils.FILE_PATH3;
//        Log.e(TAG, "filePath==>" + filePath);
//        Log.e(TAG, "filePath2==>" + filePath2);
//        Log.e(TAG, "filePath3==>" + filePath3);
//
//        String content = "hello world!";
//        FileIOUtils.writeFileFromString(filePath, content);
//        FileIOUtils.writeFileFromBytesByStream(sdCardResourceDir + "/test.txt", content.getBytes(),true);
//        byte[] bytes = FileIOUtils.readFile2BytesByStream(sdCardResourceDir + "/test.txt");
//        LogUtil.e("===file===>"+new String(bytes)+"    ===byte===>"+bytes.length);


//        boolean b = FileIOUtils.writeFileFromBytesByStream(filePath, content.getBytes(), true);
//        LogUtil.e("是否创建成功==》" + b);
//        byte[] bytes = FileIOUtils.readFile2BytesByStream(filePath);
//        LogUtil.e("===file===>"+new String(bytes)+"    ===byte===>"+bytes.length);


//        boolean b = FileIOUtils.writeFileFromBytesByStream(filePath, content.getBytes());
//        LogUtil.e("是否写入成功===》" + b);
//        byte[] bytes1 = FileIOUtils.readFile2BytesByStream(filePath);
//        LogUtil.e("===根目录下的test===>"+new String(bytes1));

        String fileDir = FileIOUtils.getFileDir(this);
        LogUtil.e(fileDir);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_to_rsa:
                onIntentToActivity();
                break;
            case R.id.btn_test:
                test();
                break;
            case R.id.btn_login:
                onLogin();
                break;
            case R.id.btn_get_content:
                getContent();
                break;
            case R.id.btn_test_permission:
                testPermission();
                break;
            case R.id.btn_test_file:
                testFile();
                break;
            case R.id.btn_test_read:
                StatisticsSDK.readFile();
                break;
            case R.id.btn_test_deleted:
                StatisticsSDK.deleteFile();
                break;
            case R.id.btn_test_post_file_data:
                StatisticsSDK.postFileDataToService();
                break;
        }
    }
}
