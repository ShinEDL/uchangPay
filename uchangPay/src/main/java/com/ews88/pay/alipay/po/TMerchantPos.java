package com.ews88.pay.alipay.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "TMerchantPos")
public class TMerchantPos implements java.io.Serializable {
	@Id
	@Column(name = "FId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer fid;
	@Column(name = "FTermNo")
	private String ftermNo;
	@ManyToOne
	@JoinColumn(name = "FMerchant_ID")
	private TMerchant tmerchant;
	@Column(name = "FStartDate")
	private String fstartDate;
	@Column(name = "FIp")
	private String fip;
	@Column(name = "FStatus")
	private String fstatus;
	@Column(name = "FCurrentDate")
	private String fcurrentDate;
	@Column(name = "FActiveStatus")
	private String factiveStatus;
	@Column(name = "FPort")
	private String fport;
	@Column(name = "FPosVersion")
	private String fposVersion;
	@Column(name = "FIsOffline")
	private String fisOffline;
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
 
	public String getFtermNo() {
		return ftermNo;
	}
	public void setFtermNo(String ftermNo) {
		this.ftermNo = ftermNo;
	}
	public TMerchant getTmerchant() {
		return tmerchant;
	}
	public void setTmerchant(TMerchant tmerchant) {
		this.tmerchant = tmerchant;
	}
	public String getFstartDate() {
		return fstartDate;
	}
	public void setFstartDate(String fstartDate) {
		this.fstartDate = fstartDate;
	}
	public String getFip() {
		return fip;
	}
	public void setFip(String fip) {
		this.fip = fip;
	}
	public String getFstatus() {
		return fstatus;
	}
	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	public String getFcurrentDate() {
		return fcurrentDate;
	}
	public void setFcurrentDate(String fcurrentDate) {
		this.fcurrentDate = fcurrentDate;
	}
	public String getFactiveStatus() {
		return factiveStatus;
	}
	public void setFactiveStatus(String factiveStatus) {
		this.factiveStatus = factiveStatus;
	}
	public String getFport() {
		return fport;
	}
	public void setFport(String fport) {
		this.fport = fport;
	}
	public String getFposVersion() {
		return fposVersion;
	}
	public void setFposVersion(String fposVersion) {
		this.fposVersion = fposVersion;
	}
	public String getFisOffline() {
		return fisOffline;
	}
	public void setFisOffline(String fisOffline) {
		this.fisOffline = fisOffline;
	}
	
	
	
	

}
