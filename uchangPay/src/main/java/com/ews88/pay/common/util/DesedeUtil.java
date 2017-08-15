package com.ews88.pay.common.util;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 3Des加解密工具
 * 
 * @version 1.0
 */
public abstract class DesedeUtil {

	public static final String KEY_ALGORITHM = "DESede";

	public static final String CIPHER_ALGORITHM = "DESede/ECB/ZeroBytePadding";

	/**
	 * 转换成密钥
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws Exception {

		DESedeKeySpec dks = new DESedeKeySpec(key);

		Security.addProvider(new BouncyCastleProvider());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);

		SecretKey secretKey = keyFactory.generateSecret(dks);

		return secretKey;
	}

	/**
	 * 解密
	 * 3DES EBC 0填充
	 * @param data
	 * @param key
	 * @return 
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {

		Key k = toKey(key);

		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

		cipher.init(Cipher.DECRYPT_MODE, k);

		return cipher.doFinal(data);
	}

	/**
	 * 加密
	 * 3DES EBC 0填充
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {

		Key k = toKey(key);

		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

		cipher.init(Cipher.ENCRYPT_MODE, k);

		return cipher.doFinal(data);
	}

	
	/**
	 * 加密
	 * 
	 * @param data 十六进制字符串
	 * @param key 三倍长密钥
	 * @return 十六进制字符串
	 * @throws Exception
	 */
	public static String encrypt(String data, byte[] key) throws Exception {
		byte[] encrypt = encrypt(Hex.decodeHex(data.toCharArray()), key);
		return Hex.encodeHexString(encrypt).toUpperCase();
	}
	
	/**
	 * 解密
	 * 
	 * @param data 十六进制字符串
	 * @param key 三倍长密钥
	 * @return 十六进制字符串
	 * @throws Exception
	 */
	public static String decrypt(String data, byte[] key) throws Exception {
		byte[] encrypt = decrypt(Hex.decodeHex(data.toCharArray()), key);
		return new String(Hex.encodeHex(encrypt, false));
	}
	
	/**
	 * 生成一个密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] initKey() throws Exception {

		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);

		kg.init(168);

		SecretKey secretKey = kg.generateKey();

		return secretKey.getEncoded();
	}
}
