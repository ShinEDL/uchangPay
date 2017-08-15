package com.ews88.pay.alipay.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	public static final String YMD = "yyyyMMdd";
	public static final String YMD_SLASH = "yyyy/MM/dd";
	public static final String YMD_DASH = "yyyy-MM-dd";
	public static final String YM_DASH = "yyyy-MM";
	public static final String YMD_DASH_WITH_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String YMD_DASH_WITH_TIME_NO = "yyyyMMddHHmmss";
	public static final String YDM_SLASH = "yyyy/dd/MM";
	public static final String YDM_DASH = "yyyy-dd-MM";
	public static final String HMS = "HHmmss";
	public static final String HMS_COLON = "HH:mm:ss";
	
	
	/**
	 * 按指定的格式返回指定日期的字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateString(Date date, String format) {
		String dateString = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			dateString = simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}
	

	/**
	 * 按指定的格式返回昨天的字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getYesterdayString(String format) {
		String dateString = "";
		try {
			// 此时打印它获取的是系统当前时间
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			dateString = new SimpleDateFormat(format)
					.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}
	/**
	 * 取得指定日期是本年的第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(date);
		return g.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 取得下一天的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNextDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		return date;
	}


	/**
	 * 按指定的格式返回上个月今天的字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getLastMonthString(String format) {
		String dateString = "";
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, -1);
			Date date = calendar.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			dateString = simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}

	
	/**
	 * 
	 * @return
	 */
	public static String getDateString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		return format.format(new Date());
	}

	/**
	 * 
	 * @return
	 */
	public static String getTimeString() {
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");

		return format.format(new Date());
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {
		String formattedDate = "";

		int length = date.length();
		String year = "";
		String month = "";
		String day = "";

		switch (length) {
		case 4: // MMdd
			year = getDateString().substring(0, 4);
			month = date.substring(0, 2);
			day = date.substring(2, 4);
			formattedDate = year + "-" + month + "-" + day;
			break;
		case 6: // yyMMdd
			year = "20" + date.substring(0, 2);
			month = date.substring(2, 4);
			day = date.substring(4, 6);
			formattedDate = year + "-" + month + "-" + day;
			break;
		case 8: // yyyyMMdd
			year = date.substring(0, 4);
			month = date.substring(4, 6);
			day = date.substring(6, 8);
			formattedDate = year + "-" + month + "-" + day;
			break;
		case 0:
		default:
			date = getDateString();
			year = date.substring(0, 4);
			month = date.substring(4, 6);
			day = date.substring(6, 8);
			formattedDate = year + "-" + month + "-" + day;
			break;
		}

		return formattedDate;
	}
	

	/**
	 * 格式化日期时间
	 * 
	 * @param datetime
	 * @return
	 */
	public static String formatDateTime(String datetime) {
		String timeString = "";
		if (datetime != null && (datetime.length() == 12)) {
			timeString = datetime.substring(0, 4) + "-"
					+ datetime.substring(4, 6) + "-" + datetime.substring(6, 8)
					+ " " + datetime.substring(8, 10) + ":"
					+ datetime.substring(10, 12);
		}
		return timeString;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(String time) {
		String formattedTime = "";

		int length = time.length();
		String hour = "";
		String minute = "";
		String second = "";

		switch (length) {
		case 4: // HHmm
			hour = time.substring(0, 2);
			minute = time.substring(2, 4);
			second = "00";
			formattedTime = hour + ":" + minute + ":" + second;
			break;
		case 6: // HHmmss
			hour = time.substring(0, 2);
			minute = time.substring(2, 4);
			second = time.substring(4, 6);
			formattedTime = hour + ":" + minute + ":" + second;
			break;
		case 0:
		default:
			time = getDateString();
			hour = time.substring(0, 2);
			minute = time.substring(2, 4);
			second = time.substring(4, 6);
			formattedTime = hour + ":" + minute + ":" + second;
			break;
		}

		return formattedTime;
	}

	/**
	 * 获取昨天的日期格式为yyyyMMdd
	 * 
	 * @return
	 */
	public static String yesterday() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, -1);
		String yestedayDate = new SimpleDateFormat("yyyyMMdd").format(calendar
				.getTime());
		return yestedayDate;

	}

	/**
	 * 获取今天的日期格式为yyyyMMdd
	 * 
	 * @return
	 */
	public static String today() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		String todayDate = new SimpleDateFormat("yyyyMMdd").format(calendar
				.getTime());
		return todayDate;
	}

	/**
	 * 
	 * @return
	 */
	public static String getLastMonthDateString() {
		String month1stString = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);

		Date date = calendar.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		month1stString = format.format(date);

		return month1stString;
	}

	public static String getDateString(Date date) {
		String dateString = "";
		if (date == null) {
			return dateString;
		}
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			dateString = simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}

	/**
	 * 按指定的格式返回当前日期的字符串
	 * 
	 * @param farmat
	 * @return
	 */
	public static String getTodayString(String format) {
		String dateString = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			dateString = simpleDateFormat.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date stringToDate(String dateString, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date;
		try {
			date = simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			date = new Date();
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 取得两个时间间隔天数
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int betweenDays(String beginDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar beginCalendar = Calendar.getInstance();;
		Calendar endCalendar = Calendar.getInstance();
		try {
			Date begin = sdf.parse(beginDate);
			Date end = sdf.parse(endDate);
			beginCalendar.setTime(begin);
			endCalendar.setTime(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int) Math.abs((endCalendar.getTimeInMillis() - beginCalendar
				.getTimeInMillis())
				/ (1000 * 60 * 60 * 24));
	}
	
	/** 
	    * 取得指定日期的后几天
	    * @param specifiedDay 
	    * @return 
	    */  
	    public static String getSpecifiedDay(String specifiedDay ,int count){  
	        Calendar c = Calendar.getInstance();  
	        Date date=null;  
	        try {  
	            date = new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);  
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        }  
	        c.setTime(date);  
	        int day=c.get(Calendar.DATE);  
	        c.set(Calendar.DATE,day+count);  
	  
	        String dayAfter=new SimpleDateFormat("yyyyMMdd").format(c.getTime());  
	        return dayAfter;  
	    }  
	
	    /**
	     * 获得"yyyyMM"格式的当前日期
	     * @return 返回"yyyyMM"格式的当前日期
	     */
	    public static String getShortYM()
	    {
	        return newShortYMFormat().format(new Date());
	    }
	    
	    
	    /**
	     * 获得"yyyyMMdd"格式的当前日期
	     * @return 返回"yyyyMMdd"格式的当前日期
	     */
	    public static String getShortYMD()
	    {
	        return newShortYMDFormat().format(new Date());
	    }
	    
	    
	    /**
	     * 获得"HHmmss"格式的当前日期
	     * @return 返回"HHmmss"格式的当前时间
	     */
	    public static String getShortHMS()
	    {
	        return newShortHMSFormat().format(new Date());
	    }

	    /**
	     * 获得"yyyyMMddHHmmss"格式的当前日期
	     * @return 返回"yyyyMMddHHmmss"格式的当前时间
	     */
	    public static String getShortYMDHMS()
	    {
	        return newShortYMDHMSFormat().format(new Date());
	    }
	    
	    /**
	     * 获得"yyyy-MM"格式的当前日期
	     * @return 返回"yyyy-MM"格式的当前日期
	     */
	    public static String getLongYM()
	    {
	        return newLongYMFormat().format(new Date());
	    }
	    
	    /**
	     * 获得"yyyy-MM-dd"格式的当前日期
	     * @return 返回"yyyy-MM-dd"格式的当前日期
	     */
	    public static String getLongYMD()
	    {
	        return newLongYMDFormat().format(new Date());
	    }
	    
	    
	    /**
	     * 获得"HH:mm:ss"格式的当前日期
	     * @return 返回"HH:mm:ss"格式的当前时间
	     */
	    public static String getLongHMS()
	    {
	        return newLongHMSFormat().format(new Date());
	    }

	    /**
	     * 获得"yyyy-MM-dd HH:mm:ss"格式的当前日期
	     * @return 返回"yyyy-MM-dd HH:mm:ss"格式的当前时间
	     */
	    public static String getLongYMDHMS()
	    {
	        return newLongYMDHMSFormat().format(new Date());
	    }
	    
		/**
	     * 创建一个"yyyyMM"日期的格式化对象
	     * @return "yyyyMM"日期的格式化对象
	     */
	    private static SimpleDateFormat newShortYMFormat()
	    {
	        return new SimpleDateFormat("yyyyMM");
	    }
	    
	    
	    /**
	     * 创建一个"yyyyMMdd"日期的格式化对象
	     * @return "yyyyMMdd"日期的格式化对象
	     */
	    private static SimpleDateFormat newShortYMDFormat()
	    {
	        return new SimpleDateFormat("yyyyMMdd");
	    }
	    
	    
	    /**
	     * 创建一个"HHmmss"日期的格式化对象
	     * @return "HHmmss"日期的格式化对象
	     */
	    private static SimpleDateFormat newShortHMSFormat()
	    {
	        return new SimpleDateFormat("HHmmss");
	    }
	    
	    
	    /**
	     * 创建一个"yyyyMMddHHmmss"日期的格式化对象
	     * @return "yyyyMMddHHmmss"日期的格式化对象
	     */
	    private static SimpleDateFormat newShortYMDHMSFormat()
	    {
	        return new SimpleDateFormat("yyyyMMddHHmmss");
	    }
	    
	    
	    /**
	     * 创建一个"yyyy-MM"日期的格式化对象
	     * @return "yyyy-MM"日期的格式化对象
	     */
	    private static SimpleDateFormat newLongYMFormat()
	    {
	        return new SimpleDateFormat("yyyy-MM");
	    }

	    /**
	     * 创建一个"yyyy-MM-dd"日期的格式化对象
	     * @return "yyyy-MM-dd"日期的格式化对象
	     */
	    private static SimpleDateFormat newLongYMDFormat()
	    {
	        return new SimpleDateFormat("yyyy-MM-dd");
	    }

	    /**
	     * 创建一个"HH:mm:ss"日期的格式化对象
	     * @return "HH:mm:ss"日期的格式化对象
	     */
	    private static SimpleDateFormat newLongHMSFormat()
	    {
	        return new SimpleDateFormat("HH:mm:ss");
	    }

	    /**
	     * 创建一个"yyyy-MM-dd HH:mm:ss"日期的格式化对象
	     * @return "yyyy-MM-dd HH:mm:ss"日期的格式化对象
	     */
	    private static SimpleDateFormat newLongYMDHMSFormat()
	    {
	        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    }
	    
	public static void main(String [] agrs ){
	/*	System.out.println(DateUtils.betweenDays("20120227", "20120301"));
		System.out.println(DateUtils.getSpecifiedDay("20120227", 5));*/
		String startDate = "2012-12-17";
		System.out.println();
	}
}