package com.ews88.pay.uchang.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ews88.pay.alipay.dao.IAlipayLogDao;
import com.ews88.pay.alipay.po.TAliPayLog;
import com.ews88.pay.alipay.util.DateUtils;
import com.ews88.pay.common.util.HqlQueryHelper;
import com.ews88.pay.common.util.PropertiesUtils;
import com.ews88.pay.uchang.service.UchangPayService;
import com.ews88.pay.wxserver.dao.WxMicroPayLogDao;
import com.ews88.pay.wxserver.po.TWxMicroPayLog;

/**
 * 优畅支付服务实现类
 * @author ShinEDL
 *
 */
@Service("uchangPayService")
public class UchangPayServiceImpl implements UchangPayService {
	
	private Logger logger = Logger.getLogger(UchangPayServiceImpl.class);
	
	@Resource(name = "wxMicroPayLogDao")
	private WxMicroPayLogDao wxMicroPayLogDao;
	
	@Resource(name = "aliPayLogDao")
	private IAlipayLogDao aliPayLogDao;
	
//	@Resource(name = "merchantService")
//	private MerchantService merchantService;
	
	
	/**
	 * 常量
	 */
	// uline
	private final String UCHANG_PAY_HOST = PropertiesUtils.getProperty("UCHANG_PAY_HOST");
	
	// wechat url
	private final String WX_ORDERS_URL = "/wechat/orders"; // 下单
	private final String WX_MICROPAY_URL = "/wechat/orders/micropay"; // 小额支付接口
	private final String WX_CLOSE_URL = "/wechat/orders/close"; // 关闭订单
	private final String WX_REVERSE_URL = "/wechat/orders/reverse"; // 冲正
	private final String WX_QUERY_URL = "/wechat/orders/query"; // 订单查询接口
	private final String WX_REFUNDS_URL = "/wechat/refunds"; // 退款
	private final String WX_REFUNDS_QUERY_URL = "/wechat/refunds/query"; // 退款查询接口
	// alipay url
	private final String ALI_MICROPAY_URL = "/alipay/orders/micropay"; // 刷卡支付
	private final String ALI_CREATE_URL = "/alipay/orders/create"; // JSAPI支付s
	private final String ALI_PRECREATE_URL = "/alipay/orders/precreate"; // 扫码支付
	private final String ALI_CLOSE_URL = "/alipay/orders/close"; // 关闭订单
	private final String ALI_CANCEL_URL = "/alipay/orders/cancel"; // 撤销订单
	private final String ALI_QUERY_URL = "/alipay/orders/query"; // 查询
	private final String ALI_REFUNDS_URL = "/alipay/refunds"; // 退款
	private final String ALI_REFUNDS_QUERY_URL = "/alipay/refunds/query"; // 退款查询
	
	/**
	 * 支付结果处理 
	 * @param operateType 1=支付 2=查询 3=撤销 4=退款 5=关闭订单
	 * @param subType 子操作类型
	 * @param result 优畅处理结果的json字符串
	 * @param jsonObj 返回终端的数据的json对象
	 * @param paramJson 终端请求数据
	 * 
	 */
	private void doResultLogic(Integer operateType, Integer subType, JSONObject resultJson,
			JSONObject jsonObj, JSONObject paramJson){
		//获取结果后的逻辑处理
		if(resultJson != null && resultJson.getString("return_code").equalsIgnoreCase("SUCCESS")){
			// 根据结果处理逻辑
			String result_code = (String) resultJson.get("result_code");
			// 判断是什么类型的操作
			switch(operateType){
				case 1:
					//支付
					subPay(subType, result_code, jsonObj, resultJson);
					if(subType == 1 || subType == 3){
						saveTranscationData(1, operateType, resultJson, paramJson);
					}else {
						saveTranscationData(2, operateType, resultJson, paramJson);
					}
					
					break;
				case 2:
					//查询
					subQuery(subType, result_code, jsonObj, resultJson);
					saveTranscationData(subType, operateType, resultJson, paramJson);
					break;
				case 3:
					//撤销
					subCancel(subType, result_code, jsonObj, resultJson);
					if(subType == 1){
						saveTranscationData(1, operateType, resultJson, paramJson);
					}else {
						saveTranscationData(2, operateType, resultJson, paramJson);
					}
					break;
				case 4:
					//退款
					subRefund(subType, result_code, jsonObj, resultJson);
					saveTranscationData(subType, operateType, resultJson, paramJson);
					break;
				case 5:
					//TODO:关闭订单
					break;
				default:
					break;
			}

		}else if(resultJson != null && resultJson.getString("return_code").equalsIgnoreCase("FAIL")){
			jsonObj.put("code", "FAIL");
			jsonObj.put("msg", resultJson.get("return_msg"));
			logger.info("返回失败 err_msg: " + resultJson.get("return_msg"));
		}else {
			jsonObj.put("code", "UNKNOW_FAIL");
			jsonObj.put("msg", "返回为空");
			logger.info("返回为空");
		}
	}

	/**
	 * 支付结果判断
	 * @param subType
	 * @param result_code
	 * @param jsonObj
	 * @param resultJson
	 */
	private void subPay(Integer subType, String result_code, JSONObject jsonObj, JSONObject resultJson){
		// 判断是哪一种支付方式
		switch(subType){
			case 1:
			case 3:
				// alipay
				if(result_code != null && result_code.equals("SUCCESS")){
					jsonObj.put("code", "WAIT_BUYER_PAY");
					jsonObj.put("msg", "支付宝支付请求接收成功，请查询订单状态");
					jsonObj.put("trans_type", subType);
					jsonObj.put("out_trade_no", resultJson.getString("out_trade_no")); // 商户系统交易单号
					jsonObj.put("trade_no", resultJson.getString("out_transaction_id")); // 支付宝交易单号
					jsonObj.put("total_amount", Integer.parseInt(resultJson.getString("total_fee")));
					jsonObj.put("buyer_logon_id", resultJson.getString("buyer_id")); // 买家支付宝账号
					jsonObj.put("receipt_amount", (resultJson.containsKey("receipt_amount"))?
							resultJson.get("receipt_amount"):"0");
					jsonObj.put("invoice_amount", (resultJson.containsKey("invoice_amount"))?
							resultJson.get("invoice_amount"):"0");
					jsonObj.put("buyer_pay_amount", resultJson.getString("total_fee"));
					String pointAmount = resultJson.containsKey("point_amount")?
							(String) resultJson.get("point_amount"): "0";
					Double doublePointAmount = Double.valueOf(pointAmount);
					jsonObj.put("point_amount", doublePointAmount.intValue() + "");
					jsonObj.put("gmt_payment", resultJson.getString("time_end"));//支付完成时间
					logger.info("支付宝支付请求接收成功");
//					jsonObj.put("code", "UNKNOW_FAIL");
//					logger.info("支付宝支付请求模拟失败");
				}else {
					if("ACQ.SYSTEM_ERROR".equalsIgnoreCase(resultJson.getString("err_code"))){
						jsonObj.put("code", "UNKNOW_FAIL");
						jsonObj.put("sub_code", resultJson.getString("err_code"));
						jsonObj.put("sub_msg", resultJson.containsKey("err_code_des")?
								resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
						logger.info("支付宝未知失败  err_msg: " + (resultJson.containsKey("err_code_des")?
								resultJson.getString("err_code_des"):resultJson.getString("err_msg")));
					}else {
						jsonObj.put("code", "FAIL");
						jsonObj.put("sub_code", resultJson.getString("err_code"));
						jsonObj.put("sub_msg", resultJson.containsKey("err_code_des")?
								resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
						String msg = (String) ((resultJson.containsKey("return_msg"))?
								resultJson.getString("return_msg"): resultJson.getString("err_code_des"));
						jsonObj.put("msg", msg);
						logger.info("支付宝支付失败  err_msg: " + (resultJson.containsKey("err_code_des")?
								resultJson.getString("err_code_des"):resultJson.getString("err_msg")));
					}
				}
				break;
			case 2:
			case 4:
				// wechat
				if(result_code != null && result_code.equals("SUCCESS")){
					jsonObj.put("code", "SUCCESS");
					jsonObj.put("msg", "微信支付成功");
					jsonObj.put("trade_no", resultJson.getString("transaction_id"));
					jsonObj.put("cash_fee", resultJson.getString("cash_fee"));
					jsonObj.put("coupon_fee", (resultJson.containsKey("coupon_fee"))? 
							resultJson.getString("coupon_fee"): "0");
					jsonObj.put("time_end", resultJson.getString("time_end"));
					jsonObj.put("out_trade_no", resultJson.getString("out_trade_no"));
					jsonObj.put("total_amount", resultJson.getString("total_fee"));
					logger.info("微信支付成功");
//					jsonObj.put("code", "UNKNOW_FAIL");
//					logger.info("微信支付请求模拟失败");
				}else {
					if("USERPAYING".equalsIgnoreCase(resultJson.getString("err_code"))){
						jsonObj.put("code", "WAIT_BUYER_PAY");
						jsonObj.put("msg", "微信等待用户输入密码");
						jsonObj.put("sub_code", resultJson.getString("err_code"));
						jsonObj.put("sub_msg", resultJson.containsKey("err_code_des")?
								resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
						logger.info("微信等待用户输入密码");
					}else if("SYSTEMERROR".equalsIgnoreCase(resultJson.getString("err_code"))
							|| "BANKERROR".equalsIgnoreCase(resultJson.getString("err_code"))){
						jsonObj.put("code", "UNKNOW_FAIL");
						jsonObj.put("sub_code", resultJson.getString("err_code"));
						jsonObj.put("sub_msg", resultJson.containsKey("err_code_des")?
								resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
						logger.info("微信未知失败  err_msg: " + resultJson.getString("err_code_des"));
					}else {
						jsonObj.put("code", "FAIL");
						jsonObj.put("err_code", resultJson.getString("err_code"));
						jsonObj.put("err_code_des", resultJson.containsKey("err_code_des")?
								resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
						String msg = (String) ((resultJson.containsKey("return_msg"))?
								resultJson.getString("return_msg"): resultJson.getString("err_code_des"));
						jsonObj.put("msg", msg);
						logger.info("微信支付失败  err_msg: " + msg);
					}
				
				}
				break;
		}
	}
	
	
	/**
	 * 查询结果判断
	 * @param subType
	 * @param result_code
	 * @param jsonObj
	 * @param resultJson
	 */
	private void subQuery(Integer subType, String result_code, JSONObject jsonObj, JSONObject resultJson){
		switch(subType){
		case 1:
			// alipay
			if(result_code != null && result_code.equals("SUCCESS")){
				jsonObj.put("code", "SUCCESS");
				jsonObj.put("msg", "支付宝订单查询成功");
				jsonObj.put("trans_type", subType);
				jsonObj.put("trade_no", resultJson.get("out_transaction_id"));
				jsonObj.put("out_trade_no", resultJson.get("out_trade_no"));
				jsonObj.put("total_amount", resultJson.getString("total_fee"));
				jsonObj.put("trade_status", resultJson.get("trade_state"));// 交易状态
				if("TRADE_SUCCESS".equalsIgnoreCase(resultJson.getString("trade_state"))) {
					jsonObj.put("buyer_logon_id", resultJson.get("buyer_id"));
					jsonObj.put("receipt_amount", (resultJson.containsKey("receipt_amount"))?
							resultJson.get("receipt_amount"):"0");
					jsonObj.put("invoice_amount", (resultJson.containsKey("invoice_amount"))?
							resultJson.get("invoice_amount"):"0");
					jsonObj.put("buyer_pay_amount", resultJson.getString("total_fee"));
					String pointAmount = resultJson.containsKey("point_amount")?
							(String) resultJson.get("point_amount"): "0";
					Double doublePointAmount = Double.valueOf(pointAmount);
					jsonObj.put("point_amount", doublePointAmount.intValue() + "");
					jsonObj.put("gmt_payment", resultJson.getString("time_end"));//支付完成时间
				}
				logger.info("支付宝订单查询成功");
//				jsonObj.put("code", "FAIL");
//				logger.info("支付宝查询请求模拟失败");
			}else {
				jsonObj.put("code", "FAIL");
				if(resultJson.containsKey("err_code") && "ACQ.SYSTEM_ERROR".equalsIgnoreCase(resultJson.getString("err_code"))) {
					jsonObj.put("code", "UNKNOW_FAIL");
				}
				
				String errorMsg = resultJson.containsKey("err_code_des")?
						resultJson.getString("err_code_des"):resultJson.getString("err_msg");
				jsonObj.put("sub_code", resultJson.get("err_code"));
				jsonObj.put("sub_msg", errorMsg);
				jsonObj.put("msg", errorMsg);
				logger.info("支付宝查询失败  err_msg: " + errorMsg);
			}
			break;
		case 2:
			// wechat
			if(result_code != null && result_code.equals("SUCCESS")){
				jsonObj.put("code", "SUCCESS");
				jsonObj.put("msg", "微信订单查询成功");
				String tradeState = "";
				if("USERPAYING".equalsIgnoreCase(resultJson.getString("trade_state"))) {
					tradeState = "WAIT_BUYER_PAY";
				} else if("CLOSED".equalsIgnoreCase(resultJson.getString("trade_state"))) {
					tradeState = "TRADE_CLOSED";
				} else if("NOTPAY".equalsIgnoreCase(resultJson.getString("trade_state"))) {
					tradeState = "NOTPAY";
				} else if("SUCCESS".equalsIgnoreCase(resultJson.getString("trade_state"))) {
					jsonObj.put("out_trade_no", resultJson.get("out_trade_no"));
					jsonObj.put("total_amount", resultJson.get("total_fee")+"");
					jsonObj.put("cash_fee", resultJson.get("cash_fee"));
					jsonObj.put("time_end", resultJson.get("time_end"));
					jsonObj.put("trade_no", resultJson.get("transaction_id"));
					tradeState = "TRADE_SUCCESS";
				} else {
					tradeState = "TRADE_FINISHED";
				}
				jsonObj.put("trade_status", tradeState);
				logger.info("微信订单查询成功");

//				jsonObj.put("code", "FAIL");
//				logger.info("微信查询请求模拟失败");
			}else if("SYSTEMERROR".equalsIgnoreCase(resultJson.getString("err_code"))) {
				jsonObj.put("code", "UNKNOW_FAIL");
				jsonObj.put("sub_code", resultJson.get("err_code"));
				jsonObj.put("sub_msg", resultJson.containsKey("err_code_des")?
						resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
				logger.info("微信订单查询未知失败");
			}else{
				jsonObj.put("code", "FAIL");
				String errorMsg = resultJson.containsKey("err_code_des")?
						resultJson.getString("err_code_des"):resultJson.getString("err_msg");
				if(resultJson.containsKey("err_code")){
					jsonObj.put("err_code", resultJson.getString("err_code"));
					jsonObj.put("err_code_des", errorMsg);
					jsonObj.put("msg", errorMsg);
				}else {
					jsonObj.put("msg", errorMsg);
				}
				logger.info("微信订单查询失败  err_msg："+ errorMsg);
			}
			
			break;
		}
	}

	
	/**
	 * 撤销结果判断
	 * @param subType
	 * @param result_code
	 * @param jsonObj
	 * @param resultJson
	 */
	private void subCancel(Integer subType, String result_code, JSONObject jsonObj, JSONObject resultJson){
		switch(subType){
		case 1:
			jsonObj.put("recall", resultJson.get("retry_flag"));
			if(result_code != null && result_code.equals("SUCCESS")){
				jsonObj.put("code", "SUCCESS");
				jsonObj.put("msg", "支付宝撤销成功");
				logger.info("支付宝撤销成功  out_trade_no: " + resultJson.get("out_trade_no"));

//				jsonObj.put("code", "UNKNOW_FAIL");
//				jsonObj.put("msg", "cancel_test");
//				logger.info("支付宝撤销请求模拟失败");
			}else {
				jsonObj.put("code", "FAIL");
				if("ACQ.SYSTEM_ERROR".equalsIgnoreCase(resultJson.getString("err_code"))) {
					jsonObj.put("code", "UNKNOW_FAIL");
				}
				String errorMsg = resultJson.containsKey("err_code_des")?
						resultJson.getString("err_code_des"):resultJson.getString("err_msg");
				if(resultJson.containsKey("err_code")){
					jsonObj.put("sub_code", resultJson.getString("err_code"));
					jsonObj.put("sub_msg", errorMsg);
					jsonObj.put("msg", errorMsg);
				}else{
					jsonObj.put("msg",  errorMsg);
				}
				logger.info("支付宝订单撤销失败  err_msg： "+ errorMsg);
			}
			break;
		case 2:
			if(result_code != null && result_code.equals("SUCCESS")){
				jsonObj.put("recall", resultJson.get("recall"));
				if("SUCCESS".equals(resultJson.getString("result_code"))){
					jsonObj.put("code", "SUCCESS");
					jsonObj.put("msg", "微信订单撤销成功");
					logger.info("微信订单撤销成功  out_trade_no: " + resultJson.get("out_trade_no"));
//					jsonObj.put("code", "UNKNOW_FAIL");
//					jsonObj.put("sub_msg", "wxcancel_test");
//					logger.info("微信撤销请求模拟失败");
				}else if("SYSTEMERROR".equalsIgnoreCase(resultJson.getString("err_code"))) {
					jsonObj.put("code", "UNKNOW_FAIL");
					jsonObj.put("sub_code", resultJson.get("err_code"));
					jsonObj.put("sub_msg", resultJson.containsKey("err_code_des")?
							resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
					logger.info("微信订单撤销未知失败");
				}else{
					jsonObj.put("code", "FAIL");
					String errorMsg = resultJson.containsKey("err_code_des")?
							resultJson.getString("err_code_des"):resultJson.getString("err_msg");
					if(resultJson.containsKey("err_code")){
						jsonObj.put("err_code", resultJson.getString("err_code"));
						jsonObj.put("err_code_des", errorMsg);
						jsonObj.put("msg", errorMsg);
					}else {
						jsonObj.put("msg", errorMsg);
					}
					logger.info("微信订单撤销失败  err_msg："+ errorMsg);
				}
			}else{
				jsonObj.put("code", "FAIL");
				jsonObj.put("msg", resultJson.containsKey("err_code_des")?
						resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
				logger.info("微信订单撤销失败");
			}
			break;
		}
		
	}

	/**
	 * 退款结果判断
	 * @param subType
	 * @param result_code
	 * @param jsonObj
	 * @param resultJson
	 */
	private void subRefund(Integer subType, String result_code, JSONObject jsonObj, JSONObject resultJson){
		switch(subType){
		case 1:
			if(result_code != null && result_code.equals("SUCCESS")){
				jsonObj.put("code", "SUCCESS");
				jsonObj.put("trade_no", resultJson.getString("out_transaction_id"));
				jsonObj.put("out_trade_no", resultJson.getString("out_trade_no"));
//				jsonObj.put("buyer_logon_id", resultJson.containsKey("buyer_logon_id")?
//						resultJson.getString("buyer_logon_id"):""); // 没有返回
				jsonObj.put("refund_amount", resultJson.getString("refund_fee"));
				if(resultJson.containsKey("gmt_refund_pay")) { // 不明确
					jsonObj.put("gmt_refund_pay", resultJson.getString("gmt_refund_pay"));
				}
				jsonObj.put("msg", "支付宝退款成功");
				logger.info("支付宝退款成功");
			}else {
				jsonObj.put("code", "ERROR");
				String errorMsg = resultJson.containsKey("err_code_des")?
						resultJson.getString("err_code_des"):resultJson.getString("err_msg");
				if(resultJson.containsKey("err_code")){
					if("ACQ.SYSTEM_ERROR".equalsIgnoreCase(resultJson.getString("sub_code"))) {
						jsonObj.put("code", "UNKNOW_FAIL");
					}
					jsonObj.put("sub_code", resultJson.get("err_code"));
					jsonObj.put("sub_msg", errorMsg);
					jsonObj.put("msg", errorMsg);
				}else{
					jsonObj.put("msg",  errorMsg);
				}
				logger.info("支付宝订单退款失败  err_msg: " + errorMsg);
			}
			break;
		case 2:
			if(result_code != null && result_code.equals("SUCCESS")){
				jsonObj.put("code", "SUCCESS");
				jsonObj.put("msg", "微信退款成功");
				jsonObj.put("trade_no", resultJson.getString("transaction_id"));
				jsonObj.put("out_trade_no", resultJson.getString("out_trade_no"));
				jsonObj.put("out_refund_no", resultJson.getString("out_refund_no"));
				jsonObj.put("refund_id", resultJson.getString("refund_id"));
				jsonObj.put("refund_amount", resultJson.getString("refund_fee") + "");
				jsonObj.put("total_amount", resultJson.getString("total_fee") + "");
				jsonObj.put("cash_fee", resultJson.getString("cash_fee") + "");
				logger.info("微信退款成功");
			}else if ("SYSTEMERROR".equalsIgnoreCase(resultJson.getString("err_code"))) {
				jsonObj.put("code", "UNKNOW_FAIL");
				jsonObj.put("err_code", resultJson.get("err_code"));
				jsonObj.put("err_code_des", resultJson.containsKey("err_code_des")?
						resultJson.getString("err_code_des"):resultJson.getString("err_msg"));
				logger.info("微信订单退款未知失败");
			}else {
				jsonObj.put("code", "FAIL");
				String errorMsg = resultJson.containsKey("err_code_des")?
						resultJson.getString("err_code_des"):resultJson.getString("err_msg");
				if (resultJson.containsKey("err_code")) {
					jsonObj.put("err_code", resultJson.getString("err_code"));
					jsonObj.put("err_code_des", errorMsg);
					jsonObj.put("msg", errorMsg);
				} else {
					jsonObj.put("msg", errorMsg);
				}
				logger.info("微信订单退款失败  err_msg: " + errorMsg);
			}
			break;
		}
	}
	
	/**
	 * 保存交易数据
	 * @param type 1=支付宝 2=微信 
	 * @param subType 1=支付 2=查询 3=撤销 4=退款 
	 * @param json 优畅返回的交易结果
	 * @param paramJson 终端请求数据
	 */
	private void saveTranscationData(Integer type, Integer subType, JSONObject json, JSONObject paramJson){
		switch(type){
		case 1:
			// alipay
			switch(subType){
			case 1:
				logger.info("保存支付宝支付交易数据");
				TAliPayLog newAliPayLog = new TAliPayLog();
				try{
					// 成功支付
					if("SUCCESS".equalsIgnoreCase(json.getString("result_code"))){
						newAliPayLog.setFstoreId(paramJson.getString("store_id"));
						newAliPayLog.setFterminalId(paramJson.getString("terminal_id"));
						newAliPayLog.setFoutTradeNo(paramJson.getString("out_trade_no"));
						newAliPayLog.setFscene(paramJson.getString("scene"));
						newAliPayLog.setFauthCode(paramJson.getString("auth_code"));
						int amount = Integer.parseInt(paramJson.getString("total_fee"));
						newAliPayLog.setFtotalAmount(amount);
						newAliPayLog.setFsellerId(null); //卖家支付宝用户ID,如果该值为空，则默认为商户签约账号对应的支付宝用户ID
						newAliPayLog.setFdiscountableAmount(0);
						newAliPayLog.setFundiscountableAmount(0);
						newAliPayLog.setFsubject(paramJson.getString("subject"));
						newAliPayLog.setFbody(paramJson.getString("body"));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time_expire = sdf.format(System.currentTimeMillis() + 30 * 60 * 1000);
						newAliPayLog.setFtimeExpire(time_expire);
						newAliPayLog.setFaliPayStatus("2");  //0=尚未支付 1=支付成功  2=支付处理中
						newAliPayLog.setFisAudit("0");
						newAliPayLog.setFstatus("1"); // 1=正常 2=已撤销 3=已退款 4=需人工处理
						newAliPayLog.setFdate(DateUtils.getDateString());
						newAliPayLog.setFtime(DateUtils.getTimeString());
						newAliPayLog.setFcode("WAIT_BUYER_PAY");
						newAliPayLog.setFmsg("等待支付");
						newAliPayLog.setFbuyerLogonId(json.getString("openid"));
						newAliPayLog.setFopenId(json.getString("buyer_id"));
						newAliPayLog.setFtradeNo(json.getString("out_transaction_id")); // 支付宝交易记录号
						newAliPayLog.setFreceiptAmount(Integer.parseInt(json.getString("total_fee")));
						//根据商户费率计算手续费
//						TMerchant merchant = merchantService.findByMerchantNo(newAliPayLog.getFstoreId());
//						if(null != merchant){
//							if(null != merchant.getFfee()){
//								BigDecimal fee = new BigDecimal(merchant.getFfee()).divide(new BigDecimal("100"))
//										.multiply(new BigDecimal(Integer.parseInt(json.getString("total_fee"))));
//								newAliPayLog.setFfee(fee.floatValue());
//								logger.info("手续费金额"+fee.floatValue()/100.0);
//							}
//						}
						aliPayLogDao.persist(newAliPayLog);
						logger.info("保存支付宝支付交易数据成功");
					}
				}catch(Exception e){
					logger.error("执行" + this.getClass().getSimpleName()
							+ "中的方法saveTranscationData 支付时出现错误 "
							+ e.getMessage());
				}
				
				break;
			case 2:
				// query
				logger.info("更新支付宝交易数据");
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
								logger.info("更新支付宝交易数据成功 out_trade_no：" + json.getString("out_trade_no"));
							}
						}catch (Exception e) {
							logger.error("执行" + this.getClass().getSimpleName()
									+ "中的方法saveTranscationData 查询时出现错误 "
									+ e.getMessage());
						}
						
					}
				}
				
				break;
			case 3:
				// cancel
				logger.info("更新支付宝交易数据");
				if("SUCCESS".equalsIgnoreCase(json.getString("result_code"))){
					List<TAliPayLog> alipayLogList = null;
					HqlQueryHelper hqlQueryHelper = new HqlQueryHelper(TAliPayLog.class, "a");
					hqlQueryHelper.addCondition(" a.foutTradeNo =? ", json.getString("out_trade_no")); 
					try{
						alipayLogList = aliPayLogDao.selectWithHqlQueryHelper(hqlQueryHelper);
						if(alipayLogList != null && alipayLogList.size() > 0){
							TAliPayLog aliPayLog = alipayLogList.get(0);
							aliPayLog.setFstatus("2");   //订单已撤销
							aliPayLog.setFmsg("已撤销");
							aliPayLog.setFaliPayStatus("0");
							aliPayLogDao.update(aliPayLog);
							logger.info("更新支付宝交易数据成功 out_trade_no：" + json.getString("out_trade_no"));
						}
					}catch (Exception e) {
						logger.error("执行" + this.getClass().getSimpleName()
								+ "中的方法saveTranscationData 撤销时出现错误 "
								+ e.getMessage());
					}
					
				}
				break;
			case 4:
				// refund
				logger.info("更新支付宝交易数据");
				if("SUCCESS".equalsIgnoreCase(json.getString("result_code"))){
					List<TAliPayLog> alipayLogList = null;
					HqlQueryHelper hqlQueryHelper = new HqlQueryHelper(TAliPayLog.class, "a");
					hqlQueryHelper.addCondition(" a.foutTradeNo =? ", json.getString("out_trade_no")); 
					try{
						alipayLogList = aliPayLogDao.selectWithHqlQueryHelper(hqlQueryHelper);
						if(alipayLogList != null && alipayLogList.size() > 0){
							TAliPayLog aliPayLog = alipayLogList.get(0);
							aliPayLog.setFstatus("3");   //订单已退款
							aliPayLog.setFmsg("已退款");
							aliPayLog.setFaliPayStatus("0");
							aliPayLogDao.update(aliPayLog);
							logger.info("更新支付宝交易数据成功 out_trade_no：" + json.getString("out_trade_no"));
						}
					}catch (Exception e) {
						logger.error("执行" + this.getClass().getSimpleName()
								+ "中的方法saveTranscationData 退款时出现错误 "
								+ e.getMessage());
					}
					
				}
				break;
			}
			
			break;
		case 2:
			// wechat
			switch(subType){
			case 1:
				try{
					logger.info("保存微信支付交易数据");
					TWxMicroPayLog tWxMicroPayLog = new TWxMicroPayLog();
					tWxMicroPayLog.setEws_merchant_no(paramJson.getString("store_id"));
					tWxMicroPayLog.setTotal_fee(json.get("total_fee") + "");
					tWxMicroPayLog.setOut_trade_no(json.getString("out_trade_no"));
					tWxMicroPayLog.setMch_id(paramJson.getString("mch_id"));
					tWxMicroPayLog.setResult_code("SUCCESS");
					tWxMicroPayLog.setSub_mch_id(paramJson.getString("mch_id"));
					tWxMicroPayLog.setEws_terminal_no(paramJson.getString("terminal_id"));
					tWxMicroPayLog.setReturn_code(json.getString("return_code"));
					tWxMicroPayLog.setReturn_msg(json.getString("return_msg"));
					tWxMicroPayLog.setNonce_str(paramJson.getString("nonce_str"));
					tWxMicroPayLog.setSign(paramJson.getString("sign"));
					tWxMicroPayLog.setErr_code(json.containsKey("err_code")?
							json.getString("err_code"):"");
					tWxMicroPayLog.setErr_code_des(json.containsKey("err_code_des")?
							json.getString("err_code_des"):"");
					tWxMicroPayLog.setOpenid(json.getString("openid"));
					tWxMicroPayLog.setIs_subscribe(json.getString("is_subscribe"));
					tWxMicroPayLog.setTrade_type(json.getString("trade_type"));
					tWxMicroPayLog.setBank_type(json.getString("bank_type"));
					tWxMicroPayLog.setFee_type(json.getString("fee_type"));
					tWxMicroPayLog.setTotal_fee(json.getString("total_fee"));
					tWxMicroPayLog.setCash_fee_type(json.getString("cash_fee_type"));
					tWxMicroPayLog.setCash_fee(json.getString("cash_fee"));
					tWxMicroPayLog.setTransaction_id(json.getString("transaction_id"));
					tWxMicroPayLog.setAttach(json.get("attach").toString());
					tWxMicroPayLog.setTime_end(json.getString("time_end"));
					wxMicroPayLogDao.persist(tWxMicroPayLog);
					logger.info("保存微信支付交易数据成功");
				}catch(Exception e) {
					logger.error("执行" + this.getClass().getSimpleName()
							+ "中的方法saveTranscationData 微信支付时出现错误 "
							+ e.getMessage());
				}
				
				break;
			case 2:
				break;
			case 3:
				// cancel
				try{
					// 更新交易状态
					logger.info("更新微信交易数据");
					if("SUCCESS".equalsIgnoreCase(json.getString("result_code"))) {
						HqlQueryHelper helper = new HqlQueryHelper(TWxMicroPayLog.class, "t");
						helper.addCondition("t.out_trade_no = ?", paramJson.getString("out_trade_no"));
						List<TWxMicroPayLog> list = wxMicroPayLogDao.selectWithHqlQueryHelper(helper);
						if(list != null && list.size() > 0) {
							TWxMicroPayLog tWxMicroPayLog = list.get(0);
							tWxMicroPayLog.setResult_code("reverse");
							wxMicroPayLogDao.update(tWxMicroPayLog);
							logger.info("更新微信交易数据成功 out_trade_no： " + paramJson.getString("out_trade_no"));
						}else{
							logger.info(paramJson.getString("out_trade_no")+"微信撤销成功，订单回写失败！");
						}
					}
				}catch(Exception e) {
					logger.error("执行" + this.getClass().getSimpleName()
							+ "中的方法saveTranscationData 微信撤销时出现错误 "
							+ e.getMessage());
				}
				
				break;
			case 4:
				// refund
				try{
					// 更新交易状态
					logger.info("更新微信交易数据");
					if("SUCCESS".equalsIgnoreCase(json.getString("result_code"))) {
						HqlQueryHelper helper = new HqlQueryHelper(TWxMicroPayLog.class, "t");
						helper.addCondition("t.out_trade_no = ?", paramJson.getString("out_trade_no"));
						List<TWxMicroPayLog> list = wxMicroPayLogDao.selectWithHqlQueryHelper(helper);
						if(list != null && list.size() > 0) {
							TWxMicroPayLog tWxMicroPayLog = list.get(0);
							tWxMicroPayLog.setResult_code("refund");
							wxMicroPayLogDao.update(tWxMicroPayLog);
							logger.info("更新微信交易数据成功 out_trade_no： " + paramJson.getString("out_trade_no"));
						}else{
							logger.info(paramJson.getString("out_trade_no")+"微信退款成功，订单回写失败！");
						}
					}
				}catch(Exception e) {
					logger.error("执行" + this.getClass().getSimpleName()
							+ "中的方法saveTranscationData 微信退款时出现错误 "
							+ e.getMessage());
				}
				
				break;
			}
			
			break;
		}
	}
	
	
	/**
	 * 微信条码支付
	 */
	public JSONObject wxMicropay(JSONObject paramJson, JSONObject md5JsonObj, Integer subType) {
		logger.info("微信条码支付-http调用");
		
		// 把json数据转换成xml字符串
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = washXML(xmlSerializer.write(md5JsonObj));
		// 发起http调用
		String orignResult = httpPostXML(UCHANG_PAY_HOST + WX_MICROPAY_URL, xml);

		// 转换成json字符串
		JSONObject jsonObj = new JSONObject();
		JSON jsonString = xmlSerializer.read(orignResult);
		JSONObject result = JSONObject.fromObject(jsonString.toString());
		logger.info("微信条码支付-优畅返回数据：" + result.toString());
		// 结果处理
		doResultLogic(1, subType, result, jsonObj, paramJson);
		return jsonObj;
	}

	/**
	 * 微信扫码支付
	 */
	@Override
	public void wxScanpay() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 微信查询接口
	 */
	@Override
	public JSONObject wxQuery(JSONObject paramJson, JSONObject md5JsonObj, Integer subType) {
		logger.info("微信查询接口-http调用");
		
		// 把json数据转换成xml字符串
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = washXML(xmlSerializer.write(md5JsonObj));

		// 发起http调用
		String orignResult = httpPostXML(UCHANG_PAY_HOST + WX_QUERY_URL, xml);
		
		// 转换成json字符串
		JSONObject jsonObj = new JSONObject();
		JSON jsonString = xmlSerializer.read(orignResult);
		JSONObject result = JSONObject.fromObject(jsonString.toString());	
		logger.info("微信查询-优畅返回数据：" + result.toString());
		// 结果处理
		doResultLogic(2, subType, result, jsonObj, paramJson);
		return jsonObj;
	}
	
	/**
	 * 微信冲正接口（撤销）
	 */
	@Override
	public JSONObject wxCancel(JSONObject paramJson, JSONObject md5JsonObj, Integer subType) {
		logger.info("微信冲正接口-http调用");
		
		// 把json数据转换成xml字符串
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = washXML(xmlSerializer.write(md5JsonObj));

		// 发起http调用
		String orignResult = httpPostXML(UCHANG_PAY_HOST + WX_REVERSE_URL, xml);
		
		// 转换成json字符串
		JSONObject jsonObj = new JSONObject();
		JSON jsonString = xmlSerializer.read(orignResult);
		JSONObject result = JSONObject.fromObject(jsonString.toString());	
		logger.info("微信冲正-优畅返回数据：" + result.toString());
		// 结果处理
		doResultLogic(3, subType, result, jsonObj, paramJson);
		return jsonObj;

	}

	/**
	 * 微信退款接口
	 */
	@Override
	public JSONObject wxRefund(JSONObject paramJson, JSONObject md5JsonObj, Integer subType) {
		logger.info("微信退款接口-http调用");
		
		// 把json数据转换成xml字符串
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = washXML(xmlSerializer.write(md5JsonObj));

		// 发起http调用
		String orignResult = httpPostXML(UCHANG_PAY_HOST + WX_REFUNDS_URL, xml);
		
		// 转换成json字符串
		JSONObject jsonObj = new JSONObject();
		JSON jsonString = xmlSerializer.read(orignResult);
		JSONObject result = JSONObject.fromObject(jsonString.toString());		
		logger.info("微信退款-优畅返回数据：" + result.toString());
		// 结果处理
		doResultLogic(4, subType, result, jsonObj, paramJson);
		return jsonObj;
		
	}
	
	/**
	 * TODO:微信关闭订单
	 */
	public JSONObject wxClose(JSONObject paramJson, JSONObject md5JsonObj, Integer subType){
		return null;
	}
	
	/**
	 * 支付宝条码支付
	 */
	public JSONObject alipayMicropay(JSONObject paramJson, JSONObject md5JsonObj, Integer subType) {
		logger.info("支付宝条码支付-http调用");

		// 把json数据转换成xml字符串
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = washXML(xmlSerializer.write(md5JsonObj));
		
		// 发起http调用
		String orignResult = httpPostXML(UCHANG_PAY_HOST + ALI_MICROPAY_URL, xml);
		
		// 转换成json字符串
		JSONObject jsonObj = new JSONObject();
		JSON jsonString = xmlSerializer.read(orignResult);
		JSONObject result = JSONObject.fromObject(jsonString.toString());
		logger.info("支付宝条码支付-优畅返回数据：" + result.toString());
		// 结果处理
		doResultLogic(1, subType, result, jsonObj, paramJson);
		return jsonObj;
	}
	
	/**
	 * 支付宝扫码支付
	 */
	@Override
	public void alipayScanpay() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * alipay查询
	 */
	@Override
	public JSONObject alipayQuery(JSONObject paramJson, JSONObject md5JsonObj, Integer subType) {
		logger.info("支付宝查询-http调用");
		
		// 把json数据转换成xml字符串
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = washXML(xmlSerializer.write(md5JsonObj));

		// 发起http调用
		String orignResult = httpPostXML(UCHANG_PAY_HOST + ALI_QUERY_URL, xml);
		
		// 转换成json字符串
		JSONObject jsonObj = new JSONObject();
		JSON jsonString = xmlSerializer.read(orignResult);
		JSONObject result = JSONObject.fromObject(jsonString.toString());		
		logger.info("支付宝查询-优畅返回数据：" + result.toString());
		// 结果处理
		doResultLogic(2, subType, result, jsonObj, paramJson);
		return jsonObj;
	}

	/**
	 * alipay撤销接口
	 */
	@Override
	public JSONObject alipayCancel(JSONObject paramJson, JSONObject md5JsonObj, Integer subType) {
		logger.info("支付宝撤销接口-http调用");
		
		// 把json数据转换成xml字符串
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = washXML(xmlSerializer.write(md5JsonObj));

		// 发起http调用
		String orignResult = httpPostXML(UCHANG_PAY_HOST + ALI_CANCEL_URL, xml);
		
		// 转换成json字符串
		JSONObject jsonObj = new JSONObject();
		JSON jsonString = xmlSerializer.read(orignResult);
		JSONObject result = JSONObject.fromObject(jsonString.toString());	
		logger.info("支付宝撤销-优畅返回数据：" + result.toString());
		// 结果处理
		doResultLogic(3, subType, result, jsonObj, paramJson);
		return jsonObj;
		
	}

	/**
	 * alipay退款
	 */
	@Override
	public JSONObject alipayRefund(JSONObject paramJson, JSONObject md5JsonObj, Integer subType) {
		logger.info("支付宝退款-http调用");
		
		// 把json数据转换成xml字符串
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = washXML(xmlSerializer.write(md5JsonObj));

		// 发起http调用
		String orignResult = httpPostXML(UCHANG_PAY_HOST + ALI_REFUNDS_URL, xml);
		
		// 转换成json字符串
		JSONObject jsonObj = new JSONObject();
		JSON jsonString = xmlSerializer.read(orignResult);
		JSONObject result = JSONObject.fromObject(jsonString.toString());		
		logger.info("支付宝退款-优畅返回数据：" + result.toString());
		// 结果处理
		doResultLogic(4, subType, result, jsonObj, paramJson);
		return jsonObj;
	}
	
	/**
	 * TODO：alipay关闭订单
	 */
	public JSONObject alipayClose(JSONObject paramJson, JSONObject md5JsonObj, Integer subType){
		return null;
	}
	

	/**
	 * 清洗xml字符串
	 * @param xml
	 * @return
	 */
	private String washXML(String xml){
		xml = xml.replace("<o>", "");
		xml = xml.replace("</o>", "");
		xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<xml>");
		xml = xml.replaceAll(" type=\"string\"", "");
		xml = xml.replaceAll(" type=\"number\"", "");
		xml = xml + "</xml>";
		logger.info("解析后的xml字符串：" + xml);
		return xml;
	}
	
	/**
	 * http传递XML数据参数
	 */
	private String httpPostXML(String urlAdds, String params) {
		HttpURLConnection conn = null;
		URL url = null;
		InputStream in = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		byte[] bb = params.getBytes();
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
			conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8"); 
			conn.setRequestProperty("Content-Length", String.valueOf(bb.length)); 
			
			conn.setAllowUserInteraction(true);
			conn.setInstanceFollowRedirects(true);
			conn.connect();

			os = conn.getOutputStream();
			os.write(params.getBytes("UTF-8"));

			int code = conn.getResponseCode();

			if (code == conn.HTTP_OK || code == conn.HTTP_ACCEPTED) {
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
	
}
