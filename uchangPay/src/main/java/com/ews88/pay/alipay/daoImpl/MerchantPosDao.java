package com.ews88.pay.alipay.daoImpl;

import org.springframework.stereotype.Component;

import com.ews88.pay.alipay.dao.IMerchantPosDao;
import com.ews88.pay.alipay.po.TMerchantPos;
import com.ews88.pay.common.dao.impl.BaseDaoImpl;

@Component("merchantPosDao")
public class MerchantPosDao extends BaseDaoImpl<TMerchantPos>
		implements IMerchantPosDao {

}
