package com.ews88.pay.common.util;

import java.util.Calendar;
import java.util.Random;

public class RefundUtil {

	public static String getRefundNo() {
		Calendar calendar = Calendar.getInstance();
		int y = calendar.get(Calendar.YEAR);
		int M = calendar.get(Calendar.MONTH) + 1;
		int d = calendar.get(Calendar.DAY_OF_YEAR);
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		int s = calendar.get(Calendar.SECOND);
		return "77" + y + M + d + h + m + s + getRandomStringByLength(3);
	}

	public static String getRandomStringByLength(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
