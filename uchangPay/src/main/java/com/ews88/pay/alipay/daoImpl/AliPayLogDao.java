package com.ews88.pay.alipay.daoImpl;

import org.springframework.stereotype.Repository;

import com.ews88.pay.alipay.dao.IAlipayLogDao;
import com.ews88.pay.alipay.po.TAliPayLog;
import com.ews88.pay.common.dao.impl.BaseDaoImpl;

@Repository("aliPayLogDao")
public class AliPayLogDao extends BaseDaoImpl<TAliPayLog> implements
		IAlipayLogDao {

}
