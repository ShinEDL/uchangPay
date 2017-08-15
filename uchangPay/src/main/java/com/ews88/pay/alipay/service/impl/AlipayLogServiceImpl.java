package com.ews88.pay.alipay.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ews88.pay.alipay.dao.IAlipayLogDao;
import com.ews88.pay.alipay.po.TAliPayLog;
import com.ews88.pay.alipay.service.AlipayLogService;
import com.ews88.pay.alipay.util.StringUtil;
import com.ews88.pay.common.service.impl.BaseServiceImpl;
import com.ews88.pay.common.util.HqlQueryHelper;

@Service("alipayLogService")
public class AlipayLogServiceImpl extends BaseServiceImpl<TAliPayLog> implements AlipayLogService{
	private static Logger log = Logger.getLogger(AlipayLogService.class);
	private IAlipayLogDao aliPayLogDao;
	@Resource(name = "aliPayLogDao")
	public void setAliPayLogDao(IAlipayLogDao aliPayLogDao) {
		super.setBasedao(aliPayLogDao);
		this.aliPayLogDao = aliPayLogDao;
	}

	
	/** 根据商户订单号查寻交易
	 * 
	 * @param outTradeNo
	 * @return
	 */
	public TAliPayLog findPayByOutTradeNo(String outTradeNo){
		TAliPayLog alipayLog = null;
		try{
			HqlQueryHelper hqlQueryHelper = new HqlQueryHelper(TAliPayLog.class, "a");
			hqlQueryHelper.addCondition(" a.foutTradeNo =? ", outTradeNo); 
			List<TAliPayLog> list = aliPayLogDao.selectWithHqlQueryHelper(hqlQueryHelper);
			if(null != list && list.size() > 0){
				alipayLog = list.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("执行" + this.getClass().getSimpleName()
					+ "中的方法isPayLogExist(TAliPayLog aliPayLog)时出现错误 "
					+ e.getMessage());

		}
		return alipayLog;
	}

	/**
	 * 根据支付宝商户号查寻交易
	 * @param tradeNo
	 * @return
	 */
	public TAliPayLog findPayByTradeNo(String tradeNo){
		TAliPayLog alipayLog = null;
		try{
			HqlQueryHelper hqlQueryHelper = new HqlQueryHelper(TAliPayLog.class, "a");
			hqlQueryHelper.addCondition(" a.ftradeNo =? ", tradeNo); 
			List<TAliPayLog> list = aliPayLogDao.selectWithHqlQueryHelper(hqlQueryHelper);
			if(null != list && list.size() > 0){
				alipayLog = list.get(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error("执行" + this.getClass().getSimpleName()
					+ "中的方法isPayLogExist(TAliPayLog aliPayLog)时出现错误 "
					+ e.getMessage());

		}
		return alipayLog;
	}
	
	
	public List<TAliPayLog> findByParams(Map<String,String> params){
		List<TAliPayLog> resultList = null;
		if(null == params){
			return null;
		}
		String outTradeNo = params.get("outTradeNo");
		String tradeNo = params.get("tradeNo");
		HqlQueryHelper hqlQueryHelper = new HqlQueryHelper(TAliPayLog.class, "a");
		if(StringUtil.isNotNullStr(tradeNo)){
			hqlQueryHelper.addCondition(" a.ftradeNo =? ", tradeNo); 
		}
		if(StringUtil.isNotNullStr(outTradeNo)){
			hqlQueryHelper.addCondition(" a.foutTradeNo =? ", outTradeNo); 
		}
		try{
			resultList = aliPayLogDao.selectWithHqlQueryHelper(hqlQueryHelper);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("执行" + this.getClass().getSimpleName()
					+ "中的方法findByParams(Map<String,String> params)时出现错误 "
					+ e.getMessage());
		}
		
		return resultList;
	}
}
