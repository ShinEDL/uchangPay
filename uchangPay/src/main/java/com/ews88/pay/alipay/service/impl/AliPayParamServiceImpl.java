package com.ews88.pay.alipay.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ews88.pay.alipay.dao.IAliPayParamDao;
import com.ews88.pay.alipay.dao.IMerchantPosDao;
import com.ews88.pay.alipay.po.TAliPayParam;
import com.ews88.pay.alipay.po.TMerchant;
import com.ews88.pay.alipay.po.TMerchantPos;
import com.ews88.pay.alipay.service.AliPayParamService;
import com.ews88.pay.common.dao.BaseDao;
import com.ews88.pay.common.service.impl.BaseServiceImpl;
import com.ews88.pay.common.util.HqlQueryHelper;

@Service("aliPayParamService")
public class AliPayParamServiceImpl extends BaseServiceImpl<TAliPayParam> implements AliPayParamService{
	
	@Resource(name = "aliPayParamDao")
	private IAliPayParamDao aliPayParamDao;
	@Resource(name = "merchantPosDao")
	private IMerchantPosDao merchantPosDao;
	@Override
	public void setBasedao(BaseDao<TAliPayParam> basedao) {
		super.setBasedao(basedao);
	}
	/**
	 * 根据终端号获取商户 再从商户获取参数 
	 **/
	public TAliPayParam getParamByPosNo(String posNo) {
		HqlQueryHelper hqlQueryHelper = new HqlQueryHelper(TMerchantPos.class, "p");
		hqlQueryHelper.addCondition("  p.ftermNo = ? ", posNo); //left join fetch p.tmerchant
		aliPayParamDao.selectWithHqlQueryHelper(hqlQueryHelper);
		List<TMerchantPos> poses = merchantPosDao.selectWithHqlQueryHelper(hqlQueryHelper);
		if (null != poses && poses.size() > 0) {
			TMerchantPos pos = poses.get(0);
			TMerchant merchant = pos.getTmerchant();
			if (null != merchant && null != merchant.getFid()) {
				HqlQueryHelper AliPayParamHqlQueryHelper = new HqlQueryHelper(TAliPayParam.class, "param");
				AliPayParamHqlQueryHelper.addCondition("  param.merchant.fid = ? ", merchant.getFid());
				List<TAliPayParam> params = aliPayParamDao.selectWithHqlQueryHelper(AliPayParamHqlQueryHelper);
				if (null != params && params.size() > 0) {
					return params.get(0);
				}
			}
		}
		return null;
	}
	
}
