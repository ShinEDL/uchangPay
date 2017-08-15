package com.ews88.pay.alipay.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "TOrderTypeMap")
public class TOrderTypeMap implements Serializable {
	private Integer fid = 0;
	private String fwxorderNo = ""; // 微信（或支付宝）回馈的订单号
	private String forderNo = ""; // 亿万商自己的订单号
	private String forderType = ""; // 订单类型 1、支付宝条码 2、微信条码 3、支付宝扫码 4、微信扫码
									// 5、支付宝退款 6、微信退款
	private String ftotalAmount = "";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getFwxorderNo() {
		return fwxorderNo;
	}

	public void setFwxorderNo(String fwxorderNo) {
		this.fwxorderNo = fwxorderNo;
	}

	public String getForderNo() {
		return forderNo;
	}

	public void setForderNo(String forderNo) {
		this.forderNo = forderNo;
	}

	public String orderType() {
		if (StringUtils.isNotBlank(forderType)) {
			if ("1".equals(forderType) || "3".equals(forderType) || "5".equals(forderType)) {
				return "1";
			} else if ("2".equals(forderType) || "4".equals(forderType) || "6".equals(forderType)) {
				return "2";
			}
		}
		return forderType;
	}

	public String getForderType() {
		return forderType;
	}

	public void setForderType(String forderType) {
		this.forderType = forderType;
	}

	public TOrderTypeMap() {
	}

	public String getFtotalAmount() {
		return ftotalAmount;
	}

	public void setFtotalAmount(String ftotalAmount) {
		this.ftotalAmount = ftotalAmount;
	}

	public TOrderTypeMap(String fwxorderNo, String forderNo, String forderType) {
		super();
		this.fwxorderNo = fwxorderNo;
		this.forderNo = forderNo;
		this.forderType = forderType;
	}

	public TOrderTypeMap(Integer fid, String fwxorderNo, String forderNo, String forderType, String ftotalAmount) {
		super();
		this.fid = fid;
		this.fwxorderNo = fwxorderNo;
		this.forderNo = forderNo;
		this.forderType = forderType;
		this.ftotalAmount = ftotalAmount;
	}

	public TOrderTypeMap(String fwxorderNo, String forderNo, String forderType, String ftotalAmount) {
		super();
		this.fwxorderNo = fwxorderNo;
		this.forderNo = forderNo;
		this.forderType = forderType;
		this.ftotalAmount = ftotalAmount;
	}
	
	

}
