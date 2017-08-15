package com.ews88.pay.wxserver.dao.impl;

import org.springframework.stereotype.Repository;

import com.ews88.pay.common.dao.impl.BaseDaoImpl;
import com.ews88.pay.wxserver.dao.WxMicroPayLogDao;
import com.ews88.pay.wxserver.po.TWxMicroPayLog;

@Repository("wxMicroPayLogDao")
public class WxMicroPayLogDaoImpl extends BaseDaoImpl<TWxMicroPayLog> implements WxMicroPayLogDao{

}
