package com.ews88.pay.alipay.daoImpl;

import org.springframework.stereotype.Component;

import com.ews88.pay.alipay.dao.OrderTypeMapDao;
import com.ews88.pay.alipay.po.TOrderTypeMap;
import com.ews88.pay.common.dao.impl.BaseDaoImpl;

@Component("orderTypeMapDao")
public class OrderTypeMapDaoImpl extends BaseDaoImpl<TOrderTypeMap> implements
		OrderTypeMapDao {

}
