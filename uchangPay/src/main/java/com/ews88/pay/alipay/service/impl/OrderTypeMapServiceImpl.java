package com.ews88.pay.alipay.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ews88.pay.alipay.dao.OrderTypeMapDao;
import com.ews88.pay.alipay.po.TOrderTypeMap;
import com.ews88.pay.alipay.service.OrderTypeMapService;
import com.ews88.pay.common.service.impl.BaseServiceImpl;

@Service("orderTypeMapService")
public class OrderTypeMapServiceImpl extends BaseServiceImpl<TOrderTypeMap> implements OrderTypeMapService{
	
	private OrderTypeMapDao orderTypeMapDao;

	@Resource(name = "orderTypeMapDao")
	public void setOrderTypeMapDao(OrderTypeMapDao orderTypeMapDao) {
		super.setBasedao(orderTypeMapDao);
		this.orderTypeMapDao = orderTypeMapDao;
	}
	
}
