package com.ews88.pay.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class DES {

	private static String encoding = "UTF-8";
	//加密秘钥
	private String sKey = "";

	public DES(String sKey) {
		this.sKey = sKey;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String[] sa = new String[3];
		String serial_No = "02000452";
		String terminal_id = "00000602";
		byte[] terminalNo = terminal_id.getBytes();
		byte[] snNo = serial_No.getBytes();
		byte[] mkey =  new byte[16];
		byte[] tmpKey = new byte[8];
		System.arraycopy(str2Bcd(terminal_id), 0, tmpKey, 0, 4);
		System.arraycopy(str2Bcd(serial_No), 0, tmpKey, 4, 4);
		for(int i=0;i<8;i++){
			mkey[i] = (byte) (tmpKey[i] ^ terminalNo[i]);
			mkey[i+8] = (byte) (tmpKey[i] ^ snNo[i]);
		}
		System.out.println();
	}
	/* 16进制字符串转换为数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] hex2Byte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }
	/**
	 *加密字符串
	 */
	public String encrypto(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = symmetricEncrypto(str.getBytes(encoding));
				result = Base64.encode(encodeByte);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 解密字符串
	 */
	public String decrypto(String str) {
		String result = str;
		if (str != null && str.length() > 0) {
			try {
				byte[] encodeByte = Base64.decode(str);

				byte[] decoder = symmetricDecrypto(encodeByte);
				result = new String(decoder, encoding);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 加密byte[]
	 */
	public byte[] encrypto(byte[] str) {
		byte[] result = null;
		if (str != null && str.length > 0) {
			try {
				byte[] encodeByte = symmetricEncrypto(str);
				result = Base64.encode(encodeByte).getBytes();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 解密byte[]
	 */
	public byte[] decrypto(byte[] str) {
		byte[] result = null;
		if (str != null && str.length > 0) {
			try {

				byte[] encodeByte = Base64.decode(new String(str, encoding));
				// byte[] encodeByte = base64decoder.decodeBuffer(new
				// String(str));
				byte[] decoder = symmetricDecrypto(encodeByte);
				result = new String(decoder).getBytes(encoding);
				result = new String(decoder).getBytes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 对称加密字节数组并返回
	 * 
	 * @param byteSource
	 *            需要加密的数据
	 * @return 经过加密的数据
	 * @throws Exception
	 */
	public byte[] symmetricEncrypto(byte[] byteSource) throws Exception {
		try {
			int mode = Cipher.ENCRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = sKey.getBytes();
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);

			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	/**
	 * 对称解密字节数组并返回
	 * 
	 * @param byteSource
	 *            需要解密的数据
	 * @return 经过解密的数据
	 * @throws Exception
	 */
	public byte[] symmetricDecrypto(byte[] byteSource) throws Exception {
		try {
			int mode = Cipher.DECRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = sKey.getBytes();
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] result = cipher.doFinal(byteSource);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	/**
	 * 散列算法
	 * 
	 * @param byteSource
	 *            需要散列计算的数据
	 * @return 经过散列计算的数据
	 * @throws Exception
	 */
	public static byte[] hashMethod(byte[] byteSource) throws Exception {
		try {
			MessageDigest currentAlgorithm = MessageDigest.getInstance("SHA-1");
			currentAlgorithm.reset();
			currentAlgorithm.update(byteSource);
			return currentAlgorithm.digest();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 对文件srcFile进行加密输出到文件distFile
	 * 
	 * @param srcFile
	 *            明文文件
	 * @param distFile
	 *            加密后的文件
	 * @throws Exception
	 */
	public void EncryptFile(String srcFile, String distFile) throws Exception {

		InputStream is = null;
		OutputStream out = null;
		CipherInputStream cis = null;
		try {
			int mode = Cipher.ENCRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = sKey.getBytes();
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			is = new FileInputStream(srcFile);
			out = new FileOutputStream(distFile);
			cis = new CipherInputStream(is, cipher);
			byte[] buffer = new byte[1024];
			int r;
			while ((r = cis.read(buffer)) > 0) {
				out.write(buffer, 0, r);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cis.close();
			is.close();
			out.close();
		}
	}

	/**
	 * 解密文件srcFile到目标文件distFile
	 * 
	 * @param srcFile
	 *            密文文件
	 * @param distFile
	 *            解密后的文件
	 * @throws Exception
	 */
	public void DecryptFile(String srcFile, String distFile) throws Exception {

		InputStream is = null;
		OutputStream out = null;
		CipherOutputStream cos = null;
		try {
			int mode = Cipher.DECRYPT_MODE;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			byte[] keyData = sKey.getBytes();
			DESKeySpec keySpec = new DESKeySpec(keyData);
			Key key = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(mode, key);
			byte[] buffer = new byte[1024];
			is = new FileInputStream(srcFile);
			out = new FileOutputStream(distFile);
			cos = new CipherOutputStream(out, cipher);

			int r;
			while ((r = is.read(buffer)) >= 0) {
				cos.write(buffer, 0, r);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			cos.close();
			is.close();
			out.close();
		}
	}

	/**
	 * 对文件进行加密64位编码
	 * 
	 * @param srcFile
	 *            源文件
	 * @param distFile
	 *            目标文件
	 */
	public void BASE64EncoderFile(String srcFile, String distFile) {
		InputStream inputStream = null;
		OutputStream out = null;
		try {
			inputStream = new FileInputStream(srcFile);

			out = new FileOutputStream(distFile);
			byte[] buffer = new byte[1024];
			while (inputStream.read(buffer) > 0) {
				out.write(encrypto(buffer));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * 对文件进行解密64位解码
	 * 
	 * @param srcFile
	 *            源文件
	 * @param distFile
	 *            目标文件
	 */
	public void BASE64DecoderFile(String srcFile, String distFile) {
		InputStream inputStream = null;
		OutputStream out = null;
		try {
			inputStream = new FileInputStream(srcFile);

			out = new FileOutputStream(distFile);
			byte[] buffer = new byte[1412];

			while (inputStream.read(buffer) > 0) {
				out.write(decrypto(buffer));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	/*
	 * 从字节数组到十六进制字符串转换
	 */
	public static String Bytes2HexString(byte[] b) {
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}
}