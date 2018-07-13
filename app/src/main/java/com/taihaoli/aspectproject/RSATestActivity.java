package com.taihaoli.aspectproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gzp.aspectproject.R;
import com.taihaoli.aspectproject.utils.LogUtil;
import com.taihaoli.statisticssdk.utils.security.Base64Utils;
import com.taihaoli.statisticssdk.utils.security.RSACipherStrategy;
import com.taihaoli.statisticssdk.utils.security.RSAConstant;
import com.taihaoli.statisticssdk.utils.security.RSAUtils;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSATestActivity extends AppCompatActivity implements View.OnClickListener{

    final String TAG = "Statistics";

    private EditText etEncryptData;


    private String encryptData;

    RSAPublicKey rsaPublicKey;
    RSAPrivateKey rsaPrivateKey;

    String encryptStr;

    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsatest);
        initView();

        KeyPair keyPair = RSAUtils.generateRSAKeyPair();
        //公钥
        rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        data = getData();

    }

    private void initView() {
        etEncryptData = findViewById(R.id.et_encrypt);
        Button btnDecrypt1 = findViewById(R.id.btn_decrypt);
        Button btnDecrypt2 = findViewById(R.id.btn_decrypt2);
        Button btnEncrypt1 = findViewById(R.id.btn_encrypt);
        Button btnEncrypt2 = findViewById(R.id.btn_encrypt2);
        btnEncrypt1.setOnClickListener(this);
        btnEncrypt2.setOnClickListener(this);
        btnDecrypt1.setOnClickListener(this);
        btnDecrypt2.setOnClickListener(this);
    }

    private String getData() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            stringBuilder.append("数据").append(i);
        }
        return stringBuilder.toString();
    }



    /**
     * 加密
     */
    private void encrypt() {
        encryptData = etEncryptData.getText().toString().trim();
        RSACipherStrategy.getInstance().initPublicKey(RSAConstant.RSA_PUBLISH_KEY);
        try {

            if (encryptData.length() != 0) {
                LogUtil.e("长度==》" + encryptData.length() + "  加密前的数据==》" + encryptData);
                encryptStr = RSACipherStrategy.getInstance().encrypt(encryptData);
            } else {
                LogUtil.e("长度==》" + data.length() + "  加密前的数据==》" + data);
                encryptStr = RSACipherStrategy.getInstance().encrypt(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.e("加密后的数据==》" + encryptStr);
    }

    /**
     * 解密
     */
    private void decrypt() {
        RSACipherStrategy.getInstance().initPrivateKey(RSAConstant.RSA_PRIVATE_KEY);
        String decryptData = null;
        try {
            decryptData = RSACipherStrategy.getInstance().decrypt(encryptStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.e("解密后的数据==>" + decryptData);
    }

    private void encryptData() {
        try {
            byte[] bytes = RSAUtils.encryptByPublicKeyForSpilt(data.getBytes(), rsaPublicKey);
            encryptStr = Base64Utils.encode(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.e("加密后的数据==》" + encryptStr);
    }

    private void decryptData() {
        String decryptData = null;
        try {
            byte[] bytes = RSAUtils.decryptByPrivateKeyForSpilt(Base64Utils.decode(encryptStr), rsaPrivateKey);
            decryptData = new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.e("解密后的数据==>" + decryptData);
    }



    public static void main(String[] args) {
        String encrypt = "Ln9KaVn1DpeC/jHDDu9zb0O3tl5HQiQT5zBkl4dBr55yaKCW3jBfjxdVM0z5ZSM0HkGQrnxghYp/yunOWxUCZ5soPw/EaQd8aV2VUlqmBijo4cVhWYuUULEkbhfBTNiJGnFx9GgEB10T7QmV4Q9R0I0Vum4PyUdXwu5RjbTICwwXdyPmYeQdD109jJY4/gvzfAoio78H+keC09MLUwDXMBmmTOb607njSKgSslYcX/7CUI+TKZcxpd/5flfDebZm+DneSmGxp3j//J6m1uaAvrO6PKmqoYd9f2BU3XQHsAR7kM/a+9zRzZZsWDnI99vT+69yzYSzGuNpVP6vte7R4qL/xolf56Ms6Mgq9hXcyKMeQhNLlqX2+iMcqWTc3WM6oh+qhNAJJJbtUeVbFkkdgCJf1ZQFWHimuXf6xKRx1K98HV+T4TVb/k8d49sINmuumzpOgyJ7PTgP9dqyDXiB5+Bql9hh4GHYVT321U/vho5CW0PXC4jSmCY5lsnD23fjjG5lhQfdeLHDq23Ex/3mjVOhIb2GjrpW+mNrKwE1+mqFNIAqKcYSCyyE/hoXAa/snE+9a6QS5PZWWIsG8P0sgW/xEpflhVQaR/8foj4/m8LjI5zWcXVvk7vIKZe/hjfToGc7EuNM1fw49ADAI/odhEgXKcgkrrWWdJA4rC3ZqaYYLwulhcsXyxx+CnmtrorbsmWZOCsuQCFiXxUN8VAMTc/O1nDKzzGJ2sMYDKcEwQyti2CyhsipiENJdvCpWF6cb5XS1Joj5EGybH/EY3rFhyA1xpaffJR5cjUpLKqXIcM7e0+zmN3ZXyw9vvqf3APsPufyAv5hDbv/Zg8UsmT9XImoxJsTEuYRUTIglMgBoUwxAUcT2Tgr875wmYU1xccskRHf5Q8c/lT916Y7+E9S9VZfK3legqWz+XfDgTqDkMxyUZeKNW7ayzK9Bxm/F2WVeJs3iufP6I+Q4WhU+qaOuByXucnLVWN7S5PBnDPN1yrih8Xpf6IPini4wIApYcCUFDtseOXVVLWAPthQyGBM64sROIL5DnVUtghB6hdrV69YJKbppJL5Si8x+2SasNN69YjqdACyRSwBp1yBA3owZupHwgXrajSDsO2YrF/Nc10FqztxPvnR1rTjbPYaw3ww62UlQiatPByfbVzCkgYjyN9/O036Lc29LPgnWp5XE6Q=";
        String decryptData = "";
        RSACipherStrategy.getInstance().initPrivateKey(RSAConstant.RSA_PRIVATE_KEY);
        try {
            decryptData = RSACipherStrategy.getInstance().decrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解密后的数据===》" + decryptData);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_encrypt:
                encrypt();
                break;
            case R.id.btn_encrypt2:
                encryptData();
                break;
            case R.id.btn_decrypt:
                decrypt();
                break;
            case R.id.btn_decrypt2:
                decryptData();
                break;
        }
    }
}
