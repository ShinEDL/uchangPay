package com.ews88.pay.uchang.service;

import net.sf.json.JSONObject;

/**
 * 优畅支付接口类
 * @author ShinEDL
 *
 */
public interface UchangPayService {
	// TAliPayLog,TWxMicroPayLog
	
	// 微信条码支付
	public JSONObject wxMicropay(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 微信扫码支付
	public void wxScanpay();
	// 微信冲正接口
	public JSONObject wxCancel(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 微信查询
	public JSONObject wxQuery(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 微信退款
	public JSONObject wxRefund(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 微信关闭订单
	public JSONObject wxClose(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 支付宝扫码支付
	public void alipayScanpay();
	// 支付宝条码支付
	public JSONObject alipayMicropay(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 支付宝撤销订单
	public JSONObject alipayCancel(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 支付宝查询
	public JSONObject alipayQuery(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 支付宝退款
	public JSONObject alipayRefund(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);
	// 支付宝关闭订单
	public JSONObject alipayClose(JSONObject paramJson, JSONObject md5JsonObj, Integer subType);

}
