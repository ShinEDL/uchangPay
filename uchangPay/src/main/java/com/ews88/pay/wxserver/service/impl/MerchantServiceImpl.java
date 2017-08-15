package com.ews88.pay.wxserver.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ews88.pay.alipay.dao.IMerchantDao;
import com.ews88.pay.alipay.po.TMerchant;
import com.ews88.pay.alipay.service.MerchantService;
import com.ews88.pay.common.dao.BaseDao;
import com.ews88.pay.common.service.impl.BaseServiceImpl;
import com.ews88.pay.common.util.HqlQueryHelper;
import com.ews88.pay.wxserver.po.TWxMicroPayLog;
import com.ews88.pay.wxserver.service.WxMicroPayLogService;

@Service("merchantService")
public class MerchantServiceImpl extends BaseServiceImpl<TMerchant> implements MerchantService{
	
	@Resource(name = "merchantDaoShop")
	private IMerchantDao merchantDaoShop;
	@Override
	public void setBasedao(BaseDao<TMerchant> basedao) {
		super.setBasedao(basedao);
	}

	/**
	 * 获取舞茶道总店的商户信息
	 * 
	 * @param merchantNo
	 * @return
	 */
	public TMerchant findByMerchantNo(String merchantNo) {
		HqlQueryHelper hqlQueryHelper = new HqlQueryHelper(TMerchant.class, "merchant");
		TMerchant merchant = null;
		if (null != merchantNo && !merchantNo.trim().equals("")) {
			hqlQueryHelper.addCondition(" merchant.fmerchantNo =? ", merchantNo); 
		}
		List<TMerchant> merchants = merchantDaoShop.selectWithHqlQueryHelper(hqlQueryHelper);
		if(null != merchants && merchants.size()>0){
			merchant = merchants.get(0);
		}
		return merchant;
	}

}
