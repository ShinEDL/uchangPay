package com.ews88.pay.wxserver.dao.impl;

import org.springframework.stereotype.Repository;

import com.ews88.pay.common.dao.impl.BaseDaoImpl;
import com.ews88.pay.wxserver.dao.PayResultNotifyLogDao;
import com.ews88.pay.wxserver.po.TPayResultNotifyLog;

@Repository("payResultNotifyLogDao")
public class PayResultNotifyLogDaoImpl extends BaseDaoImpl<TPayResultNotifyLog> implements PayResultNotifyLogDao{

}
