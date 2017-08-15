package com.ews88.pay.alipay.service;

import com.ews88.pay.alipay.po.TMerchant;
import com.ews88.pay.common.service.BaseService;

public interface MerchantService extends BaseService<TMerchant>{
	public TMerchant findByMerchantNo(String merchantNo);
}
