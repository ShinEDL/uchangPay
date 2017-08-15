package com.ews88.pay.wxserver.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ews88.pay.alipay.po.TAliPayParam;
import com.ews88.pay.alipay.po.TMerchant;
import com.ews88.pay.alipay.po.TMerchantPos;

@SuppressWarnings("serial")
@Entity
@Table(name = "TSN")
public class TSN implements Serializable{
	private Integer fid;
	private String FSN;
	private String FStatus;
	private String FDate;
	private String FWechatSubMerNo;
	private String FBindType;
	private TMerchant merchant;
	private TMerchantPos merchantPos;
	private TAliPayParam alipayParam;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public String getFSN() {
		return FSN;
	}
	public void setFSN(String fSN) {
		FSN = fSN;
	}
	public String getFStatus() {
		return FStatus;
	}
	public void setFStatus(String fStatus) {
		FStatus = fStatus;
	}
	public String getFDate() {
		return FDate;
	}
	public void setFDate(String fDate) {
		FDate = fDate;
	}
	public String getFWechatSubMerNo() {
		return FWechatSubMerNo;
	}
	public void setFWechatSubMerNo(String fWechatSubMerNo) {
		FWechatSubMerNo = fWechatSubMerNo;
	}
	public String getFBindType() {
		return FBindType;
	}
	public void setFBindType(String fBindType) {
		FBindType = fBindType;
	}
	@ManyToOne
	@JoinColumn(name = "FMerchantId")
	public TMerchant getMerchant() {
		return merchant;
	}
	public void setMerchant(TMerchant merchant) {
		this.merchant = merchant;
	}
	@ManyToOne
	@JoinColumn(name = "FPosID")
	public TMerchantPos getMerchantPos() {
		return merchantPos;
	}
	public void setMerchantPos(TMerchantPos merchantPos) {
		this.merchantPos = merchantPos;
	}
	
	@ManyToOne
	@JoinColumn(name = "FAlipayParamId")
	public TAliPayParam getAlipayParam() {
		return alipayParam;
	}
	public void setAlipayParam(TAliPayParam alipayParam) {
		this.alipayParam = alipayParam;
	}
	public TSN() {
		super();
	}

}
