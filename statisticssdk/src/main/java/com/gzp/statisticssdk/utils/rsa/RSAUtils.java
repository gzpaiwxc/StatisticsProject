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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @author Mr.Zheng
 * @date 2014年8月22日 下午1:44:23
 */
public final class RSAUtils {
    private final static String KEY_PAIR = "RSA";
    private final static String CIPHER = "RSA/ECB/PKCS1Padding";
    private static final int DEFAULT_KEY_SIZE = 1024;//秘钥默认长度
    public static final byte[] DEFAULT_SPLIT = "#PART#".getBytes();    // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
    private static final int MAX_ENCRYPT_BLOCK = (DEFAULT_KEY_SIZE / 8) - 11;// 当前秘钥支持加密的最大字节数
    private static final int MAX_DECRYPT_BLOCK = 128;// 当前秘钥支持解密的最大字节数

    /**
     * 随机生成RSA密钥对(默认密钥长度为1024)
     *
     * @return KeyPair
     */
    public static KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(DEFAULT_KEY_SIZE);
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048<br>
     *                  一般1024
     * @return KeyPair
     */
    private static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_PAIR);
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param data      需加密数据的byte数据
     * @param publicKey 公钥
     * @return 加密后的byte型数据
     */
    public static byte[] encryptData(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER);
        // 编码前设定编码方式及密钥
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 传入编码数据并返回编码结果
        return cipher.doFinal(data);
    }

    /**
     * 用私钥解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param privateKey    私钥
     * @return 解密后的字节
     */
    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

//    /**
//     * 用公钥对字符串进行分段加密
//     */
//    public static byte[] encryptByPublicKeyForSpilt(byte[] data, PublicKey publicKey) throws Exception {
//        int dataLen = data.length;
//        if (dataLen <= MAX_ENCRYPT_BLOCK) {
//            return encryptData(data, publicKey);
//        }
//        List<Byte> allBytes = new ArrayList<Byte>(2048);
//        int bufIndex = 0;
//        int subDataLoop = 0;
//        byte[] buf = new byte[MAX_ENCRYPT_BLOCK];
//        for (int i = 0; i < dataLen; i++) {
//            buf[bufIndex] = data[i];
//            if (++bufIndex == MAX_ENCRYPT_BLOCK || i == dataLen - 1) {
//                subDataLoop++;
//                if (subDataLoop != 1) {
//                    for (byte b : DEFAULT_SPLIT) {
//                        allBytes.add(b);
//                    }
//                }
//                byte[] encryptBytes = encryptData(buf, publicKey);
//                for (byte b : encryptBytes) {
//                    allBytes.add(b);
//                }
//                bufIndex = 0;
//                if (i == dataLen - 1) {
//                    buf = null;
//                } else {
//                    buf = new byte[Math.min(MAX_ENCRYPT_BLOCK, dataLen - i - 1)];
//                }
//            }
//        }
//        byte[] bytes = new byte[allBytes.size()];
//        {
//            int i = 0;
//            for (Byte b : allBytes) {
//                bytes[i++] = b.byteValue();
//            }
//        }
//        return bytes;
//    }

    /**
     * 用公钥对字符串进行分段加密
     */
    public static byte[] encryptByPublicKeyForSpilt(byte[] data, PublicKey publicKey) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 使用私钥分段解密
     */
    public static byte[] decryptByPrivateKeyForSpilt(byte[] encrypted, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = encrypted.length;
        byte[] result = new byte[(encrypted.length/128 + 1 )*117];
        int offSet = 0;
        byte[] cache;
        int i = 0;
        int len=0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encrypted, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encrypted, offSet, inputLen - offSet);
            }
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
            for(byte b:cache){
                result[len++]=b;
            }
        }
        return result;
    }

    /**
     * 使用公钥进行分段解密
     * @param encryptByte 密文
     * @param publicKey 公钥
     * @return 解密后的字节
     */
    public static byte[] decryptByPublicKey(byte[] encryptByte, RSAPublicKey publicKey)throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        int inputLen = encryptByte.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptByte, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptByte, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }


//    /**
//     * 使用私钥分段解密
//     */
//    public static byte[] decryptByPrivateKeyForSpilt(byte[] encrypted, PrivateKey privateKey) throws Exception {
//        int splitLen = DEFAULT_SPLIT.length;
//        if (splitLen <= 0) {
//            return decryptData(encrypted, privateKey);
//        }
//        int dataLen = encrypted.length;
//        List<Byte> allBytes = new ArrayList<Byte>(1024);
//        int latestStartIndex = 0;
//        for (int i = 0; i < dataLen; i++) {
//            byte bt = encrypted[i];
//            boolean isMatchSplit = false;
//            if (i == dataLen - 1) {
//                // 到data的最后了
//                byte[] part = new byte[dataLen - latestStartIndex];
//                System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
//                byte[] decryptPart = decryptData(part, privateKey);
//                for (byte b : decryptPart) {
//                    allBytes.add(b);
//                }
//                latestStartIndex = i + splitLen;
//                i = latestStartIndex - 1;
//            } else if (bt == DEFAULT_SPLIT[0]) {
//                // 这个是以split[0]开头
//                if (splitLen > 1) {
//                    if (i + splitLen < dataLen) {
//                        // 没有超出data的范围
//                        for (int j = 1; j < splitLen; j++) {
//                            if (DEFAULT_SPLIT[j] != encrypted[i + j]) {
//                                break;
//                            }
//                            if (j == splitLen - 1) {
//                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
//                                isMatchSplit = true;
//                            }
//                        }
//                    }
//                } else {
//                    // split只有一位，则已经匹配了
//                    isMatchSplit = true;
//                }
//            }
//            if (isMatchSplit) {
//                byte[] part = new byte[i - latestStartIndex];
//                System.arraycopy(encrypted, latestStartIndex, part, 0, part.length);
//                byte[] decryptPart = decryptData(part, privateKey);
//                for (byte b : decryptPart) {
//                    allBytes.add(b);
//                }
//                latestStartIndex = i + splitLen;
//                i = latestStartIndex - 1;
//            }
//        }
//        byte[] bytes = new byte[allBytes.size()];
//        {
//            int i = 0;
//            for (Byte b : allBytes) {
//                bytes[i++] = b.byteValue();
//            }
//        }
//        return bytes;
//    }


    /**
     * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
     *
     * @param keyBytes 公钥byte[]
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @return 返回公钥
     */
    public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes 私钥byte[]
     * @return 私钥
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 使用N、e值还原公钥
     *
     * @param modulus
     * @param publicExponent
     * @return 公钥
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String modulus, String publicExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 使用N、d值还原私钥
     *
     * @param modulus
     * @param privateExponent
     * @return 私钥
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String modulus, String privateExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64Utils.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr
     * @return 私钥
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64Utils.decode(privateKeyStr);
            // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param in 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(InputStream in) throws Exception {
        try {
            return loadPrivateKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 读取密钥信息
     * --------------------
     * CONTENT
     * --------------------
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }

        return sb.toString();
    }

}
