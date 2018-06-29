/*
 * Copyright 2015 Rocko (http://rocko.xyz) <rocko.zxp@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gzp.statisticssdk.utils.rsa;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


public class RSACipherStrategy extends CipherStrategy {

	private RSAPublicKey mPublicKey;
	private RSAPrivateKey mPrivateKey;

    private static RSACipherStrategy rsaCipherStrategy;

    private RSACipherStrategy() {

    }

    public static RSACipherStrategy getInstance() {
        if (rsaCipherStrategy == null) {
            synchronized (RSACipherStrategy.class) {
                if (rsaCipherStrategy == null) {
                    rsaCipherStrategy = new RSACipherStrategy();
                }
            }
        }
        return rsaCipherStrategy;
    }

	public void initPublicKey(String publicKeyContentStr) {
		try {
			mPublicKey = (RSAPublicKey) RSAUtils.loadPublicKey(publicKeyContentStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initPublicKey(InputStream publicKeyIs) {
		try {
			mPublicKey = (RSAPublicKey) RSAUtils.loadPublicKey(publicKeyIs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initPrivateKey(String privateKeyContentStr) {
		try {
			mPrivateKey = (RSAPrivateKey) RSAUtils.loadPrivateKey(privateKeyContentStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initPrivateKey(InputStream privateIs) {
		try {
			mPrivateKey = (RSAPrivateKey) RSAUtils.loadPrivateKey(privateIs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String encrypt(String content) throws Exception {
		if (mPublicKey == null) {
			throw new NullPointerException("PublicKey is null, please init it first");
		}
        byte[] encryptByte = RSAUtils.encryptByPublicKeyForSpilt(content.getBytes(), mPublicKey);
//        byte[] encryptByte = RSAUtils.encryptData(content.getBytes(), mPublicKey);

		return encodeConvert(encryptByte);
	}

	@Override
	public String decrypt(String encryptContent) throws Exception {
		if (mPrivateKey == null) {
			throw new NullPointerException("PrivateKey is null, please init it first");
		}
		byte[] encryptByte = decodeConvert(encryptContent);
        byte[] decryptByte = RSAUtils.decryptByPrivateKeyForSpilt(encryptByte, mPrivateKey);
//        byte[] decryptByte = RSAUtils.decryptData(encryptByte, mPrivateKey);

		return new String(decryptByte);
	}

    @Override
    public String decryptByPublic(String encryptContent) throws Exception {
        if (mPublicKey == null) {
            throw new NullPointerException("publicKey is null, please init it first");
        }
        byte[] encryptByte = decodeConvert(encryptContent);
        byte[] decryptByte = RSAUtils.decryptByPublicKey(encryptByte, mPublicKey);
        return new String(decryptByte);
    }

}
