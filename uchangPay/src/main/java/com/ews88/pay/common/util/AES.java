package com.ews88.pay.common.util;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;


public class AES {
	 //算法名
    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    public static final int KEY_LENGTH = 32;
    public static final String CHARST_NAME = "utf-8";
    public static final int IV_LENGTH = 16;

    //生成密钥
    public static byte[] generateKey(String key) throws Exception {
        if (key == null || key.length() != KEY_LENGTH) {
            System.out.print("Key长度不是" + KEY_LENGTH + "位");
            return null;
        }
        return key.getBytes(CHARST_NAME);
    }


    //生成iv
    public static AlgorithmParameters generateIV(String sIv) throws Exception {
        if (sIv != null && sIv.length() == IV_LENGTH) {
            byte[] bytes = sIv.getBytes();
            AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
            params.init(new IvParameterSpec(bytes));
            return params;
        } else {
            System.out.print("iv长度不是" + IV_LENGTH + "位");
            return null;
        }
    }

    //转化成JAVA的密钥格式
    public static Key convertToKey(byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }



    //解密
    public static String decrypt(String encryptedData, String password) throws Exception {
        if (encryptedData != null && encryptedData.length() > IV_LENGTH) {
            byte[] key = generateKey(password);
            String sIv = encryptedData.substring(0, IV_LENGTH);
            String content = encryptedData.substring(IV_LENGTH, encryptedData.length());
            AlgorithmParameters iv = generateIV(sIv);
            return decrypt(content, key, iv);
        }
        return null;
    }

    //解密
    public static String decrypt(String encryptedData, byte[] key, AlgorithmParameters iv) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Key secretKey = convertToKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] bytes = new BASE64Decoder().decodeBuffer(encryptedData);
        bytes = cipher.doFinal(bytes);
        String result = new String(bytes, CHARST_NAME);
        return result;
    }

}
