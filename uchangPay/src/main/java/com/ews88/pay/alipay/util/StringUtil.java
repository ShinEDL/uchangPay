package com.ews88.pay.alipay.util;

/**
 * 判断字符串的util类
 * 
 * @author lhj
 * 
 */
public class StringUtil {

	/**
	 * 判断是否为null
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		return null == str ? false : true;
	}

	/**
	 * 判断是否为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNullStr(String str) {
		return null != str && !str.trim().equals("") ? true : false;
	}

	/**
	 * 判断两字符串相同
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public static boolean isEqual(String one, String two) {
		return one.equals(two) && isNotNullStr(one) && isNotNullStr(two);
	}
	
	public static Float toFloat(String str)
	{
		if(isNotNullStr(str))
		{
		 return 	Float.parseFloat(str);
		}else
		{
			return  Float.parseFloat("0");
		}
	}
	/**
	 * 字符串左边补0
	 * @param str
	 * @param length
	 * @return
	 */
	public static String addZeroLeft(String str, int length){
		int len = str.length();
		StringBuffer newStrBuf =  new StringBuffer();
		for (int i = 0; i < length-len; i++) {
			newStrBuf.append(0);
		}
		newStrBuf.append(str);
		return newStrBuf.toString();
	}
	
	/**
	 * 字符串右边补0
	 * @param str
	 * @param length
	 * @return
	 */
	public static String addZeroRight(String str, int length){
		int len = str.length();
		StringBuffer newStrBuf =  new StringBuffer();
		newStrBuf.append(str);
		for (int i = len; i < length; i++) {
			newStrBuf.append(0);
		}
		return newStrBuf.toString();
	}
}
