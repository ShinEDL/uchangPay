package com.ews88.pay.uchang.action;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ews88.pay.alipay.dao.IAlipayLogDao;
import com.ews88.pay.alipay.po.TAliPayLog;
import com.ews88.pay.alipay.util.StringUtil;
import com.ews88.pay.common.util.AES;
import com.ews88.pay.common.util.DesedeUtil;
import com.ews88.pay.common.util.HqlQueryHelper;
import com.ews88.pay.common.util.MainKeyUtil;
import com.ews88.pay.common.util.MyX509TrustManager;
import com.ews88.pay.common.util.PropertiesUtils;
import com.ews88.pay.uchang.service.UchangPayService;
import com.opensymphony.xwork2.ActionSupport;


/**
 * 聚合支付，支持微信,支付宝。
 * 功能：将点餐终端的请求转发到优畅系统
 * @author ShinEDL
 *
 */
@Controller("uchangRouterAction")
@Scope("prototype")
public class UchangRouterAction extends ActionSupport {
	
	private static final long serialVersionUID = -2684191448342892479L;
	private Logger logger = Logger.getLogger(UchangRouterAction.class);
	
	@Resource(name = "uchangPayService")
	private UchangPayService uchangPayService;
	
	@Resource(name = "aliPayLogDao")
	private IAlipayLogDao aliPayLogDao;
	
	// 商户支付密钥map，减少http请求
	private static Map<String, String> MkeyMap = new HashMap<String, String>();
	
	/**
	 * 常量定义
	 */
	// uline
	private final static String UCHANG_MCH_HOST = PropertiesUtils.getProperty("UCHANG_MCH_HOST");
	private final static String EWS_CHANNEL = PropertiesUtils.getProperty("EWS_CHANNEL");
	
	private final static Integer WX_MICROPAY_BASESTR = 1; // 微信条码支付原字符串
	private final static Integer WX_SCANPAY_BASESTR = 2; // 微信扫码支付原字符串
	private final static Integer ALI_MICROPAY_BASESTR = 3; // 支付宝条码支付原字符串
	private final static Integer ALI_SCANPAY_BASESTR = 4; // 支付宝扫码支付原字符串
	private final static Integer QUERY_BASESTR = 11; // 查询原字符串
	private final static Integer CANCEL_BASESTR = 111; // 撤销原字符串
	private final static Integer WX_REFUND_BASESTR = 1111; // 微信退款原字符串
	private final static Integer ALI_REFUND_BASESTR = 2222; // 支付宝退款原字符串
	private String NONCESTR = UUID.randomUUID().toString().replace("-", "");
	private final static String APIKEY = PropertiesUtils.getProperty("APIKEY"); //渠道号的APIKEY
	private final static String ALI_NOTIFY_URL = PropertiesUtils.getProperty("ALI_NOTIFY_URL"); // 支付宝回调通知接口
//	private final static String WX_NOTIFY_URL = PropertiesUtils.getProperty("WX_NOTIFY_URL"); // 微信回调通知接口

	
	// test
	public static void main(String[] args) throws IOException {
		String MkeyStr = getMkeyByHttpGet("100020854792", "10000120269");//100015468237
		JSONObject MkeyJson1 = JSONObject.fromObject(MkeyStr);
		System.out.println("MkeyJson1:　" + MkeyJson1);
		String Mkeystr2 = MkeyJson1.getString("content");
		System.out.println("content: " + Mkeystr2);
		JSONObject MkeyJson2 = JSONObject.fromObject(Mkeystr2);
		String mkeyEncode = MkeyJson2.getString("mch_pay_key");
		System.out.println("mkeyEncode: " + mkeyEncode);
		try {
			String MKEY = AES.decrypt(mkeyEncode, APIKEY);
			System.out.println("MKEY：" + MKEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 获取商户支付秘钥
	 * @param mch_id 商户id（门店编号）
	 * @param ewsChannel 亿万商渠道号
	 */
	private static String getMkeyByHttpGet(String mch_id, String ewsChannel){
		HttpsURLConnection conn = null;
		URL url = null;
		InputStream in = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		Calendar cd = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
		String date = sdf.format(cd.getTime());
		System.out.println("date: " + date);
		String md5str = "GET&/v1/mch/mchpaykey&" + date + "&0&" + APIKEY; 
		System.out.println("md5str: " + md5str);
		String signature = "";
		try {
			signature = Md5(md5str, "").toLowerCase();
			System.out.println("signature: " + signature);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		
		try {
			
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化     
            TrustManager[] tm = { new MyX509TrustManager() };    
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");    
            sslContext.init(null, tm, new java.security.SecureRandom());    
              
            // 从上述SSLContext对象中得到SSLSocketFactory对象     
            SSLSocketFactory ssf = sslContext.getSocketFactory(); 
			
			url = new URL(UCHANG_MCH_HOST + "/v1/mch/mchpaykey?mch_id=" + mch_id);
			System.out.println("url: " + UCHANG_MCH_HOST + "/v1/mch/mchpaykey?mch_id=" + mch_id);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf); 
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Authorization", "Uline " + ewsChannel + ":" + signature);
			conn.setRequestProperty("Date", date);

			conn.setAllowUserInteraction(true);
			conn.setInstanceFollowRedirects(true);
			conn.connect();

			int code = conn.getResponseCode();

			if (code == HttpURLConnection.HTTP_OK) {
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
	 * 支付
	 * 功能：支付方式跳转 (1=支付宝条码支付，2=微信条码支付，3=支付宝扫码支付，4=微信扫码支付（下单） 5=支付宝JSAPI支付)
	 */
	public void pay(){
		logger.info("------支付操作跳转------");
		
		// 接收请求，初始化
		HttpServletRequest request = ServletActionContext.getRequest();
		JSONObject jsonObj = new JSONObject();
		
		// 提取参数并解包
		Map<String, String> paramsMap = getParamsAndDecode(request);
		String paramStr = paramsMap.get("paramStr");
		
		// 根据参数跳转到相应的支付方法
		if (StringUtil.isNotNullStr(paramStr)) {			
			try{
				JSONObject paramJson = JSONObject.fromObject(paramStr);
				String trans_type = paramJson.getString("trans_type");
				int pay_type = Integer.parseInt(trans_type);
				String baseStr = ""; // 用于MD5加密
				String sign = ""; // MD5加密字符串
				
				String mch_id = paramJson.getString("mch_id");
				String MKEY = ""; // MD5商户(门店)密钥
				if(MkeyMap.get(mch_id) != null) {
					MKEY = MkeyMap.get(mch_id);
					logger.info("从MkeyMap中获取MKEY: " + MKEY);
				}else {
					String MkeyStr = getMkeyByHttpGet(mch_id, EWS_CHANNEL);
					JSONObject MkeyJson1 = JSONObject.fromObject(MkeyStr);
					String Mkeystr2 = MkeyJson1.getString("content");
					JSONObject MkeyJson2 = JSONObject.fromObject(Mkeystr2);
					String mkeyEncode = MkeyJson2.getString("mch_pay_key");
					MKEY = AES.decrypt(mkeyEncode, APIKEY);
					MkeyMap.put(mch_id, MKEY);
					logger.info("从优畅后台获取MKEY: " + MKEY);
				}
				
				// 调用支付服务接口
				switch(pay_type){
					case 1:
						logger.info("支付宝条码支付");
						// 加密的JSONObject
						//auth_code=?&body=?&mch_id=?&nonce_str=?&notify_url=?&out_trade_no=?&scene=?&spbill_create_ip=?&total_fee=?
						JSONObject md5JsonObj1 = new JSONObject();
						md5JsonObj1.put("auth_code", paramJson.get("auth_code"));
						md5JsonObj1.put("body", paramJson.get("body"));
						md5JsonObj1.put("mch_id", paramJson.get("mch_id"));
						md5JsonObj1.put("out_trade_no", paramJson.get("out_trade_no"));
						md5JsonObj1.put("spbill_create_ip", paramJson.get("spbill_create_ip"));
						md5JsonObj1.put("total_fee", paramJson.get("total_fee"));
						md5JsonObj1.put("scene", "bar_code");
						
						// Md5加密
						baseStr = getBaseStr(ALI_MICROPAY_BASESTR, md5JsonObj1);
						sign = Md5(baseStr, MKEY);
						md5JsonObj1.put("sign", sign);
						md5JsonObj1.put("nonce_str", NONCESTR);
						md5JsonObj1.put("scene", "bar_code");
						md5JsonObj1.put("notify_url", ALI_NOTIFY_URL);
						paramJson.put("sign", sign);
						paramJson.put("nonce_str", NONCESTR);
						paramJson.put("scene", "bar_code");
						paramJson.put("notify_url", ALI_NOTIFY_URL);
						paramJson.put("serial_No", paramsMap.get("serial_No"));
						paramJson.put("terminal_id", paramsMap.get("terminal_id"));
						jsonObj = uchangPayService.alipayMicropay(paramJson, md5JsonObj1, pay_type);
						break;
					case 2:
						logger.info("微信条码支付");
						// 加密的JSONObject
						JSONObject md5JsonObj2 = new JSONObject();
						md5JsonObj2.put("auth_code", paramJson.get("auth_code"));
						md5JsonObj2.put("body", paramJson.get("body"));
						md5JsonObj2.put("mch_id", paramJson.get("mch_id"));
						md5JsonObj2.put("out_trade_no", paramJson.get("out_trade_no"));
						md5JsonObj2.put("spbill_create_ip", paramJson.get("spbill_create_ip"));
						md5JsonObj2.put("total_fee", paramJson.get("total_fee"));
						md5JsonObj2.put("detail", "detail");
						
						// Md5加密
						baseStr = getBaseStr(WX_MICROPAY_BASESTR, md5JsonObj2);
						sign = Md5(baseStr, MKEY);
						md5JsonObj2.put("sign", sign);
						md5JsonObj2.put("nonce_str", NONCESTR);
						paramJson.put("sign", sign);
						paramJson.put("nonce_str", NONCESTR);
						paramJson.put("serial_No", paramsMap.get("serial_No"));
						paramJson.put("terminal_id", paramsMap.get("terminal_id"));
						jsonObj = uchangPayService.wxMicropay(paramJson, md5JsonObj2, pay_type);
						break;
					case 3:
						// TODO:支付宝扫码支付
						logger.info("支付宝扫码支付");
						uchangPayService.alipayScanpay();
						break;
					case 4:
						// TODO:微信扫码支付
						logger.info("微信扫码支付");
						uchangPayService.wxScanpay();
						break;
					default:
						break;
				}
				
			}catch(Exception e) {
				jsonObj.put("code", "ERROR");
				jsonObj.put("msg", "解析异常");
				logger.error("解析异常: ",e);
			}
		}else {
			jsonObj.put("code", "ERROR");
			jsonObj.put("msg", "获取参数信息失败");
			logger.error("获取参数信息失败");
		}
		
		// 初始化response
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 返回数据
		callback(out, paramsMap.get("terminal_id"), paramsMap.get("serial_No"),
				jsonObj);
		logger.info("------支付操作跳转完成------");
	}

	/**
	 * 查询
	 * 功能：查询方式跳转 (1=支付宝，2=微信)
	 */
	public void query(){
		logger.info("------查询操作跳转------");
		// 接收请求，初始化
		HttpServletRequest request = ServletActionContext.getRequest();
		JSONObject jsonObj = new JSONObject();
		
		// 提取参数并解包
		Map<String, String> paramsMap = getParamsAndDecode(request);
		String paramStr = paramsMap.get("paramStr");
		
		// 根据参数跳转到相应的查询方法
		if (StringUtil.isNotNullStr(paramStr)) {
			try{
				JSONObject paramJson = JSONObject.fromObject(paramStr);
				// 调用查询服务接口
				String trans_type = paramJson.getString("trans_type");
				int query_type = Integer.parseInt(trans_type);
				String baseStr = ""; // 用于MD5加密
				String sign = ""; // MD5加密字符串
				
				String mch_id = paramJson.getString("mch_id");
				String MKEY = ""; // MD5商户(门店)密钥
				if(MkeyMap.get(mch_id) != null) {
					MKEY = MkeyMap.get(mch_id);
					logger.info("从MkeyMap中获取MKEY: " + MKEY);
				}else {
					String MkeyStr = getMkeyByHttpGet(mch_id, EWS_CHANNEL);
					JSONObject MkeyJson1 = JSONObject.fromObject(MkeyStr);
					String Mkeystr2 = MkeyJson1.getString("content");
					JSONObject MkeyJson2 = JSONObject.fromObject(Mkeystr2);
					String mkeyEncode = MkeyJson2.getString("mch_pay_key");
					MKEY = AES.decrypt(mkeyEncode, APIKEY);
					MkeyMap.put(mch_id, MKEY);
					logger.info("从优畅后台获取MKEY: " + MKEY);
				}
				
				// 加密的JSONObject
				//mch_id=?&nonce_str=?&out_trade_no=?
				JSONObject md5JsonObj = new JSONObject();
				md5JsonObj.put("mch_id", paramJson.get("mch_id"));
				md5JsonObj.put("out_trade_no", paramJson.get("out_trade_no"));
				
				
				// Md5加密
				baseStr = getBaseStr(QUERY_BASESTR, md5JsonObj);
				sign = Md5(baseStr, MKEY);
				md5JsonObj.put("sign", sign);
				md5JsonObj.put("nonce_str", NONCESTR);
				paramJson.put("sign", sign);
				paramJson.put("nonce_str", NONCESTR);
				paramJson.put("serial_No", paramsMap.get("serial_No"));
				paramJson.put("terminal_id", paramsMap.get("terminal_id"));
				switch(query_type){
					case 1:
						logger.info("支付宝订单查询");
						jsonObj = uchangPayService.alipayQuery(paramJson, md5JsonObj, query_type);
						break;
					case 2: 
						logger.info("微信订单查询");
						jsonObj = uchangPayService.wxQuery(paramJson, md5JsonObj, query_type);
						break;
				}
			}catch(Exception e) {
				jsonObj.put("code", "ERROR");
				jsonObj.put("msg", "解析异常");
				logger.error("解析异常：",e);
			}
		}else {
			jsonObj.put("code", "ERROR");
			jsonObj.put("msg", "获取参数信息失败");
			logger.error("获取参数信息失败");
		}
		
		// 初始化response
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 返回数据
		callback(out, paramsMap.get("terminal_id"), paramsMap.get("serial_No"),
					jsonObj);
		logger.info("------查询操作跳转完成------");
	}
	
	/**
	 * 撤销订单
	 * 1=支付宝撤销 2=微信撤销
	 */
	public void cancelOrder(){
		logger.info("------撤销操作跳转------");
		// 接收请求，初始化
		HttpServletRequest request = ServletActionContext.getRequest();
		JSONObject jsonObj = new JSONObject();
		
		// 提取参数并解包
		Map<String, String> paramsMap = getParamsAndDecode(request);
		String paramStr = paramsMap.get("paramStr");
		
		// 根据参数跳转到相应的查询方法
		if (StringUtil.isNotNullStr(paramStr)) {
			try {
				// 
				JSONObject paramJson = JSONObject.fromObject(paramStr);
				// 调用撤销服务接口
				String trans_type = paramJson.getString("trans_type");
				int cancel_type = Integer.parseInt(trans_type);
				String baseStr = ""; // 用于MD5加密
				String sign = ""; // MD5加密字符串
				
				String mch_id = paramJson.getString("mch_id");
				String MKEY = ""; // MD5商户(门店)密钥
				if(MkeyMap.get(mch_id) != null) {
					MKEY = MkeyMap.get(mch_id);
					logger.info("从MkeyMap中获取MKEY: " + MKEY);
				}else {
					String MkeyStr = getMkeyByHttpGet(mch_id, EWS_CHANNEL);
					JSONObject MkeyJson1 = JSONObject.fromObject(MkeyStr);
					String Mkeystr2 = MkeyJson1.getString("content");
					JSONObject MkeyJson2 = JSONObject.fromObject(Mkeystr2);
					String mkeyEncode = MkeyJson2.getString("mch_pay_key");
					MKEY = AES.decrypt(mkeyEncode, APIKEY);
					MkeyMap.put(mch_id, MKEY);
					logger.info("从优畅后台获取MKEY: " + MKEY);
				}
				
				// 加密的JSONObject
				//mch_id=?&nonce_str=?&out_trade_no=?
				JSONObject md5JsonObj = new JSONObject();
				md5JsonObj.put("mch_id", paramJson.get("mch_id"));
				md5JsonObj.put("out_trade_no", paramJson.get("out_trade_no"));
				
				// Md5加密
				baseStr = getBaseStr(CANCEL_BASESTR, paramJson);
				sign = Md5(baseStr, MKEY);
				md5JsonObj.put("sign", sign);
				md5JsonObj.put("nonce_str", NONCESTR);
				paramJson.put("sign", sign);
				paramJson.put("nonce_str", NONCESTR);
				paramJson.put("serial_No", paramsMap.get("serial_No"));
				paramJson.put("terminal_id", paramsMap.get("terminal_id"));
				switch (cancel_type) {
					case 1:
						logger.info("支付宝订单撤销");
						jsonObj = uchangPayService.alipayCancel(paramJson, md5JsonObj, cancel_type);
						break;
					case 2:
						logger.info("微信订单撤销");
						jsonObj = uchangPayService.wxCancel(paramJson, md5JsonObj, cancel_type);
						break;
				}
			} catch (Exception e) {
				jsonObj.put("code", "ERROR");
				jsonObj.put("msg", "解析异常");
				logger.error("解析异常：", e);
			}
		} else {
			jsonObj.put("code", "ERROR");
			jsonObj.put("msg", "获取参数信息失败");
			logger.error("获取参数信息失败");
		}
		
		// 初始化response
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 返回数据
		callback(out, paramsMap.get("terminal_id"), paramsMap.get("serial_No"),
				jsonObj);
		logger.info("------撤销操作跳转完成------");
	}
	
	/**
	 * 退款
	 * 1=支付宝退款 2=微信退款
	 */
	public void refundOrder(){
		logger.info("------退款操作跳转------");
		// 接收请求，初始化
		HttpServletRequest request = ServletActionContext.getRequest();
		JSONObject jsonObj = new JSONObject();
		
		// 提取参数并解包
		Map<String, String> paramsMap = getParamsAndDecode(request);
		String paramStr = paramsMap.get("paramStr");
		
		// 根据参数跳转到相应的查询方法
		if (StringUtil.isNotNullStr(paramStr)) {
			try {
				JSONObject paramJson = JSONObject.fromObject(paramStr);
				// 调用撤销服务接口
				String trans_type = paramJson.getString("trans_type");
				int refund_type = Integer.parseInt(trans_type);
				String baseStr = ""; // 用于MD5加密
				String sign = ""; // MD5加密字符串
				
				String mch_id = paramJson.getString("mch_id");
				String MKEY = ""; // MD5商户(门店)密钥
				if(MkeyMap.get(mch_id) != null) {
					MKEY = MkeyMap.get(mch_id);
					logger.info("从MkeyMap中获取MKEY: " + MKEY);
				}else {
					String MkeyStr = getMkeyByHttpGet(mch_id, EWS_CHANNEL);
					JSONObject MkeyJson1 = JSONObject.fromObject(MkeyStr);
					String Mkeystr2 = MkeyJson1.getString("content");
					JSONObject MkeyJson2 = JSONObject.fromObject(Mkeystr2);
					String mkeyEncode = MkeyJson2.getString("mch_pay_key");
					MKEY = AES.decrypt(mkeyEncode, APIKEY);
					MkeyMap.put(mch_id, MKEY);
					logger.info("从优畅后台获取MKEY: " + MKEY);
				}
				
				switch (refund_type) {
					case 1:
						logger.info("支付宝退款");
						
						// 加密的JSONObject
						// mch_id=?&nonce_str=?&op_user_id=?&out_refund_no=?&out_trade_no=?&refund_fee=?
						JSONObject md5JsonObj = new JSONObject();
						md5JsonObj.put("mch_id", paramJson.get("mch_id"));
						md5JsonObj.put("out_trade_no", paramJson.get("out_trade_no"));
						md5JsonObj.put("op_user_id", paramJson.get("op_user_id"));
						md5JsonObj.put("out_refund_no", paramJson.get("out_refund_no"));
						md5JsonObj.put("refund_fee", paramJson.get("refund_fee"));
						
						// Md5加密
						baseStr = getBaseStr(ALI_REFUND_BASESTR, paramJson);
						sign = Md5(baseStr, MKEY);
						md5JsonObj.put("sign", sign);
						md5JsonObj.put("nonce_str", NONCESTR);
						paramJson.put("sign", sign);
						paramJson.put("nonce_str", NONCESTR);
						paramJson.put("serial_No", paramsMap.get("serial_No"));
						paramJson.put("terminal_id", paramsMap.get("terminal_id"));
						jsonObj = uchangPayService.alipayRefund(paramJson, md5JsonObj, refund_type);
						break;
					case 2:
						logger.info("微信退款");
						
						// 加密的JSONObject
						//mch_id=?&nonce_str=?&op_user_id=?&out_refund_no=?&out_trade_no=?&refund_fee=?&total_fee=? 
						JSONObject md5JsonObj2 = new JSONObject();
						md5JsonObj2.put("mch_id", paramJson.get("mch_id"));
						md5JsonObj2.put("out_trade_no", paramJson.get("out_trade_no"));
						md5JsonObj2.put("op_user_id", paramJson.get("op_user_id"));
						md5JsonObj2.put("out_refund_no", paramJson.get("out_refund_no"));
						md5JsonObj2.put("refund_fee", paramJson.get("refund_fee"));
						md5JsonObj2.put("total_fee", paramJson.get("total_fee"));
						
						// Md5加密
						baseStr = getBaseStr(WX_REFUND_BASESTR, paramJson);
						sign = Md5(baseStr, MKEY);
						md5JsonObj2.put("sign", sign);
						md5JsonObj2.put("nonce_str", NONCESTR);
						paramJson.put("sign", sign);
						paramJson.put("nonce_str", NONCESTR);
						paramJson.put("serial_No", paramsMap.get("serial_No"));
						paramJson.put("terminal_id", paramsMap.get("terminal_id"));
						jsonObj = uchangPayService.wxRefund(paramJson, md5JsonObj2, refund_type);
						break;
				}
			} catch (Exception e) {
				jsonObj.put("code", "ERROR");
				jsonObj.put("msg", "解析异常");
				logger.error("解析异常", e);
			}
		} else {
			jsonObj.put("code", "ERROR");
			jsonObj.put("msg", "获取参数信息失败");
			logger.error("获取参数信息失败");
		}
		
		// 初始化response
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("GB2312");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 返回数据
		callback(out, paramsMap.get("terminal_id"), paramsMap.get("serial_No"),
				jsonObj);
		logger.info("------退款操作跳转完成------");
	}
	
	/**
	 * 关闭订单
	 * 1=支付宝关闭订单 2=微信关闭订单
	 */
	public void closeOrder(){
		// TODO: 关闭订单
	}
	
	/**
	 * 支付宝异步通知
	 */
	public void alipayNotify(){
		logger.info("------优畅支付宝异步通知------");
		
		// 接收请求，初始化
		HttpServletRequest request = ServletActionContext.getRequest();
		String requestResult = getPostParameter(request);
		JSONObject json = JSONObject.fromObject(requestResult);
		
		//TODO:处理优畅返回的数据temp
		if("SUCCESS".equalsIgnoreCase(json.getString("result_code"))){
			if(json.getString("trade_state").equalsIgnoreCase("TRADE_SUCCESS")) {
				List<TAliPayLog> alipayLogList = null;
				HqlQueryHelper hqlQueryHelper = new HqlQueryHelper(TAliPayLog.class, "a");
				hqlQueryHelper.addCondition(" a.foutTradeNo =? ", json.getString("out_trade_no")); 
				try{
					alipayLogList = aliPayLogDao.selectWithHqlQueryHelper(hqlQueryHelper);
					if(alipayLogList != null && alipayLogList.size() > 0){
						TAliPayLog aliPayLog = alipayLogList.get(0);
						aliPayLog.setFaliPayStatus("1");  //支付成功
						aliPayLog.setFcode("SUCCESS");
						aliPayLog.setFmsg("交易成功");
						aliPayLogDao.update(aliPayLog);
					}
				}catch (Exception e) {
					logger.error("执行" + this.getClass().getSimpleName()
							+ "中的方法saveTranscationData 查询时出现错误 "
							+ e.getMessage());
				}
				
			}
		}
		
		// 初始化response
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<xml><return_code>SUCCESS</return_code></xml>");
		out.print(sb);
		out.close();
		logger.info("------优畅支付宝异步通知结束------");
	}
	
	/**
	 * 微信异步通知
	 */
	public void wxNotify(){

	}
	
	/**
	 * 健康检查接口
	 */
	public void health(){
		logger.info("健康检查接口");
		// 初始化response
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("{\"code\":\"health\"}");
		out.print(sb);
		out.close();
	}
	
	/**
	 * 获取原参数字符串,用于Md5加密
	 * 支付：1=微信条码支付原字符串 2=微信扫码支付原字符串 3=支付宝条码支付原字符串 4=支付宝扫码支付原字符串
	 * 查询：11=查询
	 * 撤销：111=撤销
	 * 退款：1111=退款
	 * @param type
	 * @param paramJson
	 * @return
	 */
	private String getBaseStr(Integer type, JSONObject paramJson){
		
		String result = "";
		switch(type){
			case 1: 
				// 微信条码支付 auth_code=?&body=?&detail=?&mch_id=?&nonce_str=?&out_trade_no=?&spbill_create_ip=?&total_fee=?
				String auth_code1 = paramJson.getString("auth_code");
				String body1 = paramJson.getString("body");
				String mch_id1 = paramJson.getString("mch_id");
				String out_trade_no1 = paramJson.getString("out_trade_no");
				String spbill_create_ip1 = paramJson.getString("spbill_create_ip");
				Integer total_fee1 = paramJson.getInt("total_fee");
				//String detail1 = paramJson.getString("detail");
				result = "auth_code=" + auth_code1 + "&body=" + body1 + "&detail=detail" + "&mch_id=" + mch_id1 + "&nonce_str=" + NONCESTR
					+ "&out_trade_no=" + out_trade_no1 + "&spbill_create_ip=" + spbill_create_ip1 + "&total_fee=" + total_fee1;
				break;
			case 2: 
				// 微信扫码支付
				break;
			case 3: 
				// 支付宝条码支付 auth_code=?&body=?&mch_id=?&nonce_str=?&notify_url=?&out_trade_no=?&scene=?&spbill_create_ip=?&total_fee=?
				String auth_code3 = paramJson.getString("auth_code");
				String body3 = paramJson.getString("body");
				String mch_id3 = paramJson.getString("mch_id");
				String out_trade_no3 = paramJson.getString("out_trade_no");
				String spbill_create_ip3 = paramJson.getString("spbill_create_ip");
				Integer total_fee3 = paramJson.getInt("total_fee");
				String scene3 = paramJson.getString("scene");
				result = "auth_code=" + auth_code3 + "&body=" + body3 + "&mch_id=" + mch_id3 + "&nonce_str=" + NONCESTR + "&notify_url=" + ALI_NOTIFY_URL
						+ "&out_trade_no=" + out_trade_no3 + "&scene=" + scene3 + "&spbill_create_ip=" + spbill_create_ip3 + "&total_fee=" + total_fee3;
				break;
			case 4: 
				// 支付宝扫码支付
				break;
			case 11: 
				// 查询：mch_id=?&nonce_str=?&out_trade_no=?
				String mch_id11 = paramJson.getString("mch_id");
				String out_trade_no11 = paramJson.getString("out_trade_no");
				result = "mch_id=" + mch_id11 + "&nonce_str=" + NONCESTR + "&out_trade_no=" + out_trade_no11;
				break;
			case 111: 
				// 撤销：mch_id=?&nonce_str=?&out_trade_no=?
				String mch_id111 = paramJson.getString("mch_id");
				String out_trade_no111 = paramJson.getString("out_trade_no");
				result = "mch_id=" + mch_id111 + "&nonce_str=" + NONCESTR + "&out_trade_no=" + out_trade_no111;
				break;
			case 1111: 
				// 微信退款：mch_id=?&nonce_str=?&op_user_id=?&out_refund_no=?&out_trade_no=?&refund_fee=?&total_fee=? 
				String mch_id1111 = paramJson.getString("mch_id");
				String op_user_id1111 = paramJson.getString("op_user_id");
				String out_refund_no1111 = paramJson.getString("out_refund_no");
				String out_trade_no1111 = paramJson.getString("out_trade_no");
				String refund_fee1111 = paramJson.getString("refund_fee");
				Integer total_fee1111 = paramJson.getInt("total_fee");
				result = "mch_id=" + mch_id1111 + "&nonce_str=" + NONCESTR + "&op_user_id=" + op_user_id1111
						+ "&out_refund_no=" + out_refund_no1111 + "&out_trade_no=" + out_trade_no1111 + "&refund_fee=" 
						+ refund_fee1111 + "&total_fee=" + total_fee1111;
				break;
			case 2222: 
				// 支付宝退款：mch_id=?&nonce_str=?&op_user_id=?&out_refund_no=?&out_trade_no=?&refund_fee=?
				String mch_id2222 = paramJson.getString("mch_id");
				String op_user_id2222 = paramJson.getString("op_user_id");
				String out_refund_no2222 = paramJson.getString("out_refund_no");
				String out_trade_no2222 = paramJson.getString("out_trade_no");
				String refund_fee2222 = paramJson.getString("refund_fee");
				result = "mch_id=" + mch_id2222 + "&nonce_str=" + NONCESTR + "&op_user_id=" + op_user_id2222
						+ "&out_refund_no=" + out_refund_no2222 + "&out_trade_no=" + out_trade_no2222 + "&refund_fee=" 
						+ refund_fee2222;
				break;
			
		}
		logger.info("Md5加密参数字符串: " + result);
		return result;
	}
	
	
	/**
	 * Md5加密获取 sign
	 * @param baseStr 原参数字符串
	 * @param mKey 商户密钥
	 * @return
	 */
	private static String Md5(String baseStr, String mKey) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		//输入的字符串转换成字节数组
		String str = "";
		if(mKey.equalsIgnoreCase("")) {
			// 普通md5加密
			str = baseStr;
		}else {
			// 商户密钥加密
			str = baseStr + "&key=" + mKey;
		}
        byte[] inputByteArray = str.getBytes();
        //inputByteArray是输入字符串转换得到的字节数组
        md5.update(inputByteArray);
        //转换并返回结果，也是字节数组，包含16个元素
        byte[] resultByteArray = md5.digest();

        //首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        //new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符）
        char[] resultCharArray = new char[resultByteArray.length*2];
        //遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for(byte b : resultByteArray){
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        
        //字符数组组合成字符串返回
        return new String(resultCharArray).toUpperCase();
        
	}
	
	/**
	 * 提取参数并解包
	 */
	private Map<String, String> getParamsAndDecode(HttpServletRequest request) {
		// 提取参数
		String paramStr = getPostParameter(request);
		logger.info("终端传过来的参数："+ paramStr);
		JSONObject temp = JSONObject.fromObject(paramStr);
		String serial_No = temp.getString("serial_No");
		String terminal_id = temp.getString("terminal_id");
		String cipherTextHexEncoded = temp.getString("message");
		
		// 将加密数据进行解包
		try {
			paramStr = getJsonStrFromCipherText(terminal_id, serial_No, cipherTextHexEncoded);
			logger.info("解包后的数据："+ paramStr);
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put("paramStr", paramStr); // 终端传递过来的参数(解密后)
		result.put("serial_No", serial_No);
		result.put("terminal_id", terminal_id);
		return result;
	}

	/**
	 * 响应返回
	 */
	private void callback(PrintWriter out, String terminal_id, String serial_No, JSONObject jsonObj){
		// 将jsonObj加密
		String cipherText = "";
		logger.info("返回的json为：" + jsonObj);
		try {
			if(terminal_id != null && serial_No != null)
				cipherText = buildCipherText(jsonObj, MainKeyUtil.getKey(terminal_id, serial_No));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 将密文封装好发送
		JSONObject outObj = new JSONObject();
		outObj.put("terminal_id", terminal_id);
		outObj.put("serial_No", serial_No);
		outObj.put("message", cipherText);
		logger.info("响应数据为：" + outObj);

		out.print(outObj);
		out.close();
	}

	
	/**
	 * 根据request获取Post参数
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private String getPostParameter(HttpServletRequest request) {
		BufferedInputStream buf = null;
		int iContentLen = request.getContentLength();
		byte sContent[] = new byte[iContentLen];
		String sContent2 = null;
		try {
			buf = new BufferedInputStream(request.getInputStream());
			buf.read(sContent, 0, sContent.length);
			sContent2 = new String(sContent, 0, iContentLen, "GBK");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				buf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sContent2;
	}
	  
	/**
	 * 根据request传过来的json进行解密
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 * @throws DecoderException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 *             {"serial_No":"02000452","terminal_id":"00000602","message":
	 *             "912AAE6B014EF036"}
	 */
	private String getJsonStrFromCipherText(String terminal_id, String serial_No, String cipherTextHexEncoded)
			throws Exception {

		// 获取密钥
		byte[] key = MainKeyUtil.getKey(terminal_id, serial_No);

		// 获取十六进制编码字符串，前四位是表示长度
		String hexStr = DesedeUtil.decrypt(cipherTextHexEncoded, key).substring(4);

		// 十六进制解码
		String plainText = new String(Hex.decodeHex(hexStr.toCharArray()), "GB2312");
		return "{" + plainText + "}";
	}
	
	 
	
	
	/**
	  * 将json转成3des加密后的密文
	  * @param request
	  * @return
	  * @throws Exception 
	  * @throws IOException
	  * {"serial_No":"02000452","terminal_id":"00000602","message":"912AAE6B014EF036"}
	  */
	  private String buildCipherText(JSONObject jsonObj, byte[] key) throws Exception{
		  String plainText = jsonObj.toString().replaceAll("[{}]", "");
		  
		  // 使用GB2312编码
		  byte[] content = plainText.getBytes("GB2312");
		  byte[] contentWithHead = new byte[content.length + 2];
		  
		  // 两字节表示长度
		  contentWithHead[0] = (byte) ((content.length & 0x0000ff00) >> 8);
		  contentWithHead[1] = (byte) (content.length & 0x000000ff);
		  System.arraycopy(content, 0, contentWithHead, 2, content.length);

		  // 十六进制编码
		  String hexStr = new String(Hex.encodeHex(contentWithHead, false));
		  
		  logger.info("数据长度: " + content.length);
		  logger.info("加密前的数据: " + hexStr);
		  // 3des编码
		  String cipherText = DesedeUtil.encrypt(hexStr, key);
		  
		  logger.info("加密后的数据: " + cipherText);
		  
		  //String cipherText = DesedeUtil.decrypt(hexStr, key);

		  return cipherText;
	  }
	
	
	
}
