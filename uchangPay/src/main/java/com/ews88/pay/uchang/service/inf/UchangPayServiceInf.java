package com.ews88.pay.uchang.service.inf;

import net.sf.json.JSONObject;

/**
 * 优畅支付接口类
 * @author ShinEDL
 *
 */
public interface UchangPayServiceInf {
	// TAliPayLog,TWxMicroPayLog
	
	// 优畅统一操作方法
	public String operate(JSONObject json, String url, String note);
	
	// 微信条码支付
	public String wxMicropay(JSONObject json);
	// 微信扫码支付
	public void wxScanpay();
	// 微信冲正接口
	public String wxCancel(JSONObject json);
	// 微信查询
	public String wxQuery(JSONObject json);
	// 微信退款
	public String wxRefund(JSONObject json);
	// 支付宝扫码支付
	public void alipayScanpay();
	// 支付宝条码支付
	public String alipayMicropay(JSONObject json);
	// 支付宝撤销订单
	public String alipayCancel(JSONObject json);
	// 支付宝查询
	public String alipayQuery(JSONObject json);
	// 支付宝退款
	public String alipayRefund(JSONObject json);

}
