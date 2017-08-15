package com.ews88.pay.wxserver.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ews88.pay.common.service.impl.BaseServiceImpl;
import com.ews88.pay.wxserver.dao.TSNDao;
import com.ews88.pay.wxserver.po.TSN;
import com.ews88.pay.wxserver.service.TSNService;

@Service("tsnService")
public class TSNServiceImpl extends BaseServiceImpl<TSN> implements TSNService{
	
	private TSNDao tsnDao;

	@Resource
	public void setTsnDao(TSNDao tsnDao) {
		super.setBasedao(tsnDao);
		this.tsnDao = tsnDao;
	}
	
	

}
