package com.ews88.pay.alipay.service;

import java.util.List;
import java.util.Map;

import com.ews88.pay.alipay.po.TAliPayLog;
import com.ews88.pay.common.service.BaseService;

public interface AlipayLogService extends BaseService<TAliPayLog>{
	public TAliPayLog findPayByOutTradeNo(String outTradeNo);
	public TAliPayLog findPayByTradeNo(String tradeNo);
	public List<TAliPayLog> findByParams(Map<String,String> params);
}
