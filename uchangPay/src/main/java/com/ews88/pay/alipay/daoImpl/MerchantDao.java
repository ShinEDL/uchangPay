package com.ews88.pay.alipay.daoImpl;

import org.springframework.stereotype.Component;

import com.ews88.pay.alipay.dao.IMerchantDao;
import com.ews88.pay.alipay.po.TMerchant;
import com.ews88.pay.common.dao.impl.BaseDaoImpl;

@Component("merchantDaoShop")
public class MerchantDao extends BaseDaoImpl<TMerchant> implements
		IMerchantDao {

}
