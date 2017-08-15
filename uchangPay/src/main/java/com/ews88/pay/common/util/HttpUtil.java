package com.ews88.pay.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class HttpUtil {

	/**
	 * HTTP POST 请求
	 * 
	 * @param urlAdds
	 *            地址
	 * @param params
	 *            参数
	 * @return String
	 */
	@SuppressWarnings("static-access")
	public static String httpPost(String urlAdds, String params) {
		HttpURLConnection conn = null;
		URL url = null;
		InputStream in = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			url = new URL(urlAdds);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			conn.setAllowUserInteraction(true);
			conn.setInstanceFollowRedirects(true);
			conn.connect();

			os = conn.getOutputStream();
			os.write(params.getBytes("UTF-8"));

			int code = conn.getResponseCode();

			if (code == conn.HTTP_OK) {
				in = conn.getInputStream();
				isr = new InputStreamReader(in, "UTF-8");
				br = new BufferedReader(isr);
				String line = "";
				StringBuffer sb = new StringBuffer();
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (null != os) {
					os.flush();
					os.close();
				}
				if (null != br) {
					br.close();
				}
				if (null != isr) {
					isr.close();
				}
				if (null != in) {
					in.close();
				}
				if (null != conn) {
					conn.disconnect();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
/**
 * http接口传json数据
 * @param urlAdds
 * @param obj
 * @return
 * @throws IOException
 */
	public static String httpPostJson(String urlAdds, JSONObject obj) throws IOException {
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		String encoding = "GBK";
		try {
			byte[] data = obj.toString().getBytes(encoding);
			// 创建连接
			URL url = new URL(urlAdds);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-javascript; charset=" + encoding);
			connection.setRequestProperty("Content-Length", String.valueOf(data.length));
			connection.connect();

			// POST请求
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(data);
			out.flush();
			out.close();

			// 读取响应
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lines;
			StringBuffer sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "UTF-8");
				sb.append(lines);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 断开连接
			connection.disconnect();
		}
	}
	
	/**
	 * http接口传json数据
	 * @param urlAdds
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String httpPostJsonGb2312(String urlAdds, JSONObject obj) throws IOException {
//		BufferedReader reader = null;
		HttpURLConnection connection = null;
		String encoding = "GB2312";
		try {
			byte[] data = obj.toString().getBytes(encoding);
			// 创建连接
			URL url = new URL(urlAdds);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/x-javascript; charset=" + encoding);
			connection.setRequestProperty("Content-Length", String.valueOf(data.length));
			connection.connect();

			// POST请求
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(data);
			out.flush();
			out.close();

			String resultStr = inputStreamToString(connection.getInputStream());
			return resultStr;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 断开连接
			connection.disconnect();
		}
	}
	
	
	public static String httpPostJsonStrUtf8(String urlAdds, String jsonStr) throws IOException {
//		BufferedReader reader = null;
		HttpURLConnection connection = null;
		String encoding = "UTF-8";
		try {
			byte[] data = jsonStr.getBytes(encoding);
			// 创建连接
			URL url = new URL(urlAdds);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json; charset=" + encoding);
			connection.setRequestProperty("Content-Length", String.valueOf(data.length));
			connection.connect();

			// POST请求
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(data);
			out.flush();
			out.close();
			String resultStr = inputStreamToString(connection.getInputStream());
			return resultStr;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 断开连接
			connection.disconnect();
		}
	}

	
	private static String inputStreamToString(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString("UTF-8");
	}
	
	
	/**
	 * 组合参数（无序）
	 * 
	 * @param params
	 *            集合
	 * @return
	 */
	public static String getParamsByMap(Map<String, Object> paramMap) {
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
		String params = "";
		StringBuffer str = new StringBuffer();
		for (String paramKey : paramMap.keySet()) {
			String param = paramKey + "=" + paramMap.get(paramKey);
			str.append("&").append(param);
		}
		if (str != null && str.length() > 0) {
			params = str.substring(1, str.length());
		}
		return params;
	}
	/**
	 * 组合参数（按字典序）
	 * 
	 * @param params
	 *            集合
	 * @return
	 */
	public static String getSortParamsByMap(Map<String, Object> paramMap){
		ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:paramMap.entrySet()){
            if(entry.getValue()!=""){
                list.add( "&" + entry.getKey() + "=" + entry.getValue() );
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        String params = "";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        if (sb != null && sb.length() > 0) {
			params = sb.substring(1, sb.length());
		}
        return params;
	}
	public static void main(String[] args) throws IOException {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("merchantNo", "601307550052692"); // 601307550052692
//		paramMap.put("totalFee", "20000");
//		paramMap.put("transType", "93");
//		paramMap.put("orderNo", "1000235686323017");
//		paramMap.put("authCode", "151515665");
//		String param = getParamsByMap(paramMap);
//		 String result =
//		 httpPost("http://127.0.0.1:9090/wuchadaoPaySys/alipay_barPay.do",param);
		// System.out.println(result);
		JSONObject obj = new JSONObject();
//		obj.put("sub_mch_id", "1332714401");
//		obj.put("bill_date", "20170421");
//		obj.put("bill_type", "ALL");
//		obj.put("tar_type", "GZIP");
//		String resultString = httpPostJson("http://127.0.0.1:9000/ewspay/wxserverpay/skzf_downloadBill.do", obj);
//		obj.put("mch_id", "1269356201");
//		String resultString = httpPostJson("http://127.0.0.1:9000/ewspay/wxserverpay/skzf_getSignKey.do", obj);
		obj.put("out_refund_no", "8300086931704333");
		obj.put("total_fee", "552");
		obj.put("refund_fee", "552");
		
		obj.put("out_trade_no", "830008693170427103247");
		obj.put("sub_mch_id", "1269904301");
		String resultString = httpPostJson("http://127.0.0.1:9000/ewspay/agentpay/payManageForTerminal_pay.do", obj); //refundQuery    query
		System.out.println(resultString);
	}
	

}
