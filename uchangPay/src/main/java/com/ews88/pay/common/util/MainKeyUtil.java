package com.ews88.pay.common.util;

import java.io.UnsupportedEncodingException;

public class MainKeyUtil {
	
	/**
	 * @param terminalId
	 * @param serialNo
	 * @return 将双倍长（16字节）密钥转为三倍长（24字节）
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getKey(String terminalId, String serialNo) throws UnsupportedEncodingException {
		serialNo = serialNo.substring(serialNo.length() - 8, serialNo.length());
		byte[] mKeyBuf = new byte[24];
		
		byte[] key = new byte[8]; 
		
		byte[] left = asc2bcd(terminalId.getBytes(), terminalId.length());
		byte[] right = asc2bcd(serialNo.getBytes(), serialNo.length());
		
		System.arraycopy(left, 0, key, 0, 4);
		System.arraycopy(right, 0, key, 4, 4);
		
		for (int i = 0; i < 8; i++) {
			mKeyBuf[i] = (byte) (key[i] ^ terminalId.getBytes()[i]);
			mKeyBuf[i + 8] = (byte) (key[i] ^ serialNo.getBytes()[i]);
		}
		System.arraycopy(mKeyBuf, 0, mKeyBuf, 16, 8);
		return mKeyBuf;
	}
	
	/**
	 * @param terminalId
	 * @param serialNo
	 * @return 双倍长密钥
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getMainKey(String terminalId, String serialNo) throws UnsupportedEncodingException {
		byte[] mKeyBuf = new byte[16];
		
		byte[] key = new byte[8]; 
		
		byte[] left = asc2bcd(terminalId.getBytes(), terminalId.length());
		byte[] right = asc2bcd(serialNo.getBytes(), serialNo.length());
		
		System.arraycopy(left, 0, key, 0, 4);
		System.arraycopy(right, 0, key, 4, 4);
		
		for (int i = 0; i < 8; i++) {
			mKeyBuf[i] = (byte) (key[i] ^ terminalId.getBytes()[i]);
			mKeyBuf[i + 8] = (byte) (key[i] ^ serialNo.getBytes()[i]);
		}
		
		return mKeyBuf;
	}
  
    private static byte asc_to_bcd(byte asc) {  
        byte bcd;  
  
        if ((asc >= '0') && (asc <= '9'))  
            bcd = (byte) (asc - '0');  
        else if ((asc >= 'A') && (asc <= 'F'))  
            bcd = (byte) (asc - 'A' + 10);  
        else if ((asc >= 'a') && (asc <= 'f'))  
            bcd = (byte) (asc - 'a' + 10);  
        else  
            bcd = (byte) (asc - 48);  
        return bcd;  
    }  
  
    private static byte[] asc2bcd(byte[] ascii, int asc_len) {  
        byte[] bcd = new byte[asc_len / 2];  
        int j = 0;  
        for (int i = 0; i < (asc_len + 1) / 2; i++) {  
            bcd[i] = asc_to_bcd(ascii[j++]);  
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));  
        }  
        return bcd;  
    }  
  
    public static String bcd2Str(byte[] bytes) {  
        char temp[] = new char[bytes.length * 2], val;  
  
        for (int i = 0; i < bytes.length; i++) {  
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);  
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');  
  
            val = (char) (bytes[i] & 0x0f);  
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');  
        }  
        return new String(temp);  
    }
    
    /**
     * 计算要传输报文的长度
     * @param str 要传输的报文，没经过十六进制编码的内容
     * @return
     */
    public static String getTextLength(String str) {
    	char[] initCharArr = new char[] {'0', '0', '0', '0'};
    	char[] plainTextLenHex = Integer.toHexString(str.getBytes().length).toCharArray();
    	System.out.println(new String(plainTextLenHex));
    	for (int i = plainTextLenHex.length - 1, j = 3; i >= 0; i--, j--) {
    		initCharArr[j] = plainTextLenHex[i];
		}
    	return new String(initCharArr).toUpperCase();
    }
}
