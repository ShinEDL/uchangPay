package com.ews88.pay.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Properties;


public class PropertiesUtils {

	private static URL url = null;
	private static String path = "";
	private static File file = null;
	private static FileInputStream input = null;
	private static FileOutputStream output = null;
	/**
	 * 动态获取配置文件里的value
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		Properties config = new Properties();
		url = PropertiesUtils.class.getResource("/");
		path = url.getPath();
		int index = path.indexOf(":") - 1;
		path = path.substring(index);
		path = path.replaceAll("%20", " ");
		file = new File(path + "/globalConfig.properties");
			try {
				input = new FileInputStream(file);
				config.load(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return config.getProperty(key);
	}
	/**
	 * 动态设置配置文件里的value
	 * @param key,value
	 * @return
	 */
	public static void setProperty(String value) {
		url = PropertiesUtils.class.getResource("/");
		path = url.getPath();
		int index = path.indexOf(":") - 1;
		path = path.substring(index);
		//获取项目文件的根目录，而非发布到tomcat等下的根目录
		file = new File(path+ "/globalConfig.properties");
		try {
			output = new FileOutputStream(file);
			byte[] bytes = (value).getBytes();
			output.write(bytes);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取项目名称
	 * @param key,value
	 * @return
	 */
	public static String getUrl() {
		url = PropertiesUtils.class.getResource("/");
		path = url.getPath();
		int index = path.indexOf(":") - 1;
		path = path.substring(index);
		path = path.replaceAll("%20", " ");
		return path;
	}
}
