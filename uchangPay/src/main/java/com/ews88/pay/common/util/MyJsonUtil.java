package com.ews88.pay.common.util;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class MyJsonUtil {
	  public static String getPostParameter(HttpServletRequest request){
		  BufferedInputStream buf = null;
		  int iContentLen = request.getContentLength();
		  byte sContent[] = new byte[iContentLen];
		  String sContent2 = null;
		  try {
			  buf = new BufferedInputStream(request.getInputStream());
			  buf.read(sContent, 0, sContent.length);
			  sContent2 = new String(sContent,0,iContentLen,"UTF-8");

		  } catch (IOException e) {
			e.printStackTrace();
		  } finally
		  {
			  try {
				  buf.close();
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		  }
		  return sContent2;
	  }
	  
	  public static String getPostParameterGb2312(HttpServletRequest request){
		  BufferedInputStream buf = null;
		  int iContentLen = request.getContentLength();
		  byte sContent[] = new byte[iContentLen];
		  String sContent2 = null;
		  try {
			  buf = new BufferedInputStream(request.getInputStream());
			  buf.read(sContent, 0, sContent.length);
			  sContent2 = new String(sContent,0,iContentLen,"GB2312");

		  } catch (IOException e) {
			e.printStackTrace();
		  } finally
		  {
			  try {
				  buf.close();
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		  }
		  return sContent2;
	  }

}
