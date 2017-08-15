package com.ews88.pay.alipay.service;

import com.ews88.pay.alipay.po.TAliPayParam;
import com.ews88.pay.common.service.BaseService;

public interface AliPayParamService extends BaseService<TAliPayParam>{
	public TAliPayParam getParamByPosNo(String posNo);
}
