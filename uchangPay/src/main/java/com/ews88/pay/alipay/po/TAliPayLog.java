package com.ews88.pay.alipay.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "TAliPayLog")
public class TAliPayLog implements java.io.Serializable {
	@Id
	@Column(name = "FId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer fid;
	@Column(name = "FDate")
	private String fdate;
	@Column(name = "FTime")
	private String ftime;
	@Column(name = "FoperatorId")
	private String foperatorId;
	@Column(name = "FstoreId")
	private String fstoreId;
	@Column(name = "FterminalId")
	private String fterminalId;
	@Column(name = "FaliPayStatus")
	private String faliPayStatus;
	@Column(name = "FoutTradeNo")
	private String foutTradeNo;
	@Column(name = "Fscene")
	private String fscene;
	@Column(name = "FauthCode")
	private String fauthCode;
	@Column(name = "FtradeNo")
	private String ftradeNo;
	@Column(name = "FtimeExpire")
	private String ftimeExpire;
	@Column(name = "FsellerId")
	private String fsellerId;
	@Column(name = "Fsubject")
	private String fsubject;
	@Column(name = "FtotalAmount")
	private Integer ftotalAmount;
	@Column(name = "FdiscountableAmount")
	private Integer fdiscountableAmount;
	@Column(name = "FundiscountableAmount")
	private Integer fundiscountableAmount;
	@Column(name = "Fbody")
	private String fbody;
	@Column(name = "Fcode")
	private String fcode;
	@Column(name = "Fmsg")
	private String fmsg;
	@Column(name = "FsubCode")
	private String fsubCode;
	@Column(name = "FsubMsg")
	private String fsubMsg;
	@Column(name = "FopenId")
	private String fopenId;
	@Column(name = "FbuyerLogonId")
	private String fbuyerLogonId;
	@Column(name = "FrecTotalAmount ")
	private Integer frecTotalAmount;
	@Column(name = "FreceiptAmount")
	private Integer freceiptAmount;
	@Column(name = "FinvoiceAmount")
	private Integer finvoiceAmount;
	@Column(name = "FbuyerPayAmount")
	private Integer fbuyerPayAmount;
	@Column(name = "FpointAmount")
	private Integer fpointAmount;
	@Column(name = "FgmtPayment")
	private String fgmtPayment;
	@Column(name = "FfundBillList")
	private String ffundBillList;

	@Column(name = "FrefundFee")
	private Integer frefundFee;
	@Column(name = "FgmtRefundPay")
	private String fgmtRefundPay;

	@Column(name = "FFee")
	private Float ffee;
	@Column(name = "FIsAudit")
	private String fisAudit;
	@Column(name = "FAuditDate")
	private String fauditDate;
	@Column(name = "FAuditTime")
	private String fauditTime;
	@Column(name = "FStatus")
	private String fstatus;
	@Column(name = "FAppID")
	private String fappId;
	
	
	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public String getFaliPayStatus() {
		return faliPayStatus;
	}

	public void setFaliPayStatus(String faliPayStatus) {
		this.faliPayStatus = faliPayStatus;
	}

	public String getFoutTradeNo() {
		return foutTradeNo;
	}

	public void setFoutTradeNo(String foutTradeNo) {
		this.foutTradeNo = foutTradeNo;
	}

	public String getFscene() {
		return fscene;
	}

	public void setFscene(String fscene) {
		this.fscene = fscene;
	}

	public String getFauthCode() {
		return fauthCode;
	}

	public void setFauthCode(String fauthCode) {
		this.fauthCode = fauthCode;
	}

	public String getFtradeNo() {
		return ftradeNo;
	}

	public void setFtradeNo(String ftradeNo) {
		this.ftradeNo = ftradeNo;
	}

	public String getFtimeExpire() {
		return ftimeExpire;
	}

	public void setFtimeExpire(String ftimeExpire) {
		this.ftimeExpire = ftimeExpire;
	}

	public String getFsellerId() {
		return fsellerId;
	}

	public void setFsellerId(String fsellerId) {
		this.fsellerId = fsellerId;
	}

	public String getFsubject() {
		return fsubject;
	}

	public void setFsubject(String fsubject) {
		this.fsubject = fsubject;
	}

	public Integer getFtotalAmount() {
		return ftotalAmount;
	}

	public void setFtotalAmount(Integer ftotalAmount) {
		this.ftotalAmount = ftotalAmount;
	}

	public String getFbody() {
		return fbody;
	}

	public void setFbody(String fbody) {
		this.fbody = fbody;
	}

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public String getFmsg() {
		return fmsg;
	}

	public void setFmsg(String fmsg) {
		this.fmsg = fmsg;
	}

	public String getFsubCode() {
		return fsubCode;
	}

	public void setFsubCode(String fsubCode) {
		this.fsubCode = fsubCode;
	}

	public String getFsubMsg() {
		return fsubMsg;
	}

	public void setFsubMsg(String fsubMsg) {
		this.fsubMsg = fsubMsg;
	}

	public String getFopenId() {
		return fopenId;
	}

	public void setFopenId(String fopenId) {
		this.fopenId = fopenId;
	}

	public String getFbuyerLogonId() {
		return fbuyerLogonId;
	}

	public void setFbuyerLogonId(String fbuyerLogonId) {
		this.fbuyerLogonId = fbuyerLogonId;
	}

	public Integer getFinvoiceAmount() {
		return finvoiceAmount;
	}

	public void setFinvoiceAmount(Integer finvoiceAmount) {
		this.finvoiceAmount = finvoiceAmount;
	}

	public Integer getFbuyerPayAmount() {
		return fbuyerPayAmount;
	}

	public void setFbuyerPayAmount(Integer fbuyerPayAmount) {
		this.fbuyerPayAmount = fbuyerPayAmount;
	}

	public Integer getFpointAmount() {
		return fpointAmount;
	}

	public void setFpointAmount(Integer fpointAmount) {
		this.fpointAmount = fpointAmount;
	}

	public String getFgmtPayment() {
		return fgmtPayment;
	}

	public void setFgmtPayment(String fgmtPayment) {
		this.fgmtPayment = fgmtPayment;
	}

	public String getFfundBillList() {
		return ffundBillList;
	}

	public void setFfundBillList(String ffundBillList) {
		this.ffundBillList = ffundBillList;
	}

	public Float getFfee() {
		return ffee;
	}

	public void setFfee(Float ffee) {
		this.ffee = ffee;
	}

	public String getFisAudit() {
		return fisAudit;
	}

	public void setFisAudit(String fisAudit) {
		this.fisAudit = fisAudit;
	}

	public String getFauditDate() {
		return fauditDate;
	}

	public void setFauditDate(String fauditDate) {
		this.fauditDate = fauditDate;
	}

	public String getFauditTime() {
		return fauditTime;
	}

	public void setFauditTime(String fauditTime) {
		this.fauditTime = fauditTime;
	}

	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}

	public Integer getFdiscountableAmount() {
		return fdiscountableAmount;
	}

	public void setFdiscountableAmount(Integer fdiscountableAmount) {
		this.fdiscountableAmount = fdiscountableAmount;
	}

	public Integer getFundiscountableAmount() {
		return fundiscountableAmount;
	}

	public void setFundiscountableAmount(Integer fundiscountableAmount) {
		this.fundiscountableAmount = fundiscountableAmount;
	}

	public Integer getFrecTotalAmount() {
		return frecTotalAmount;
	}

	public void setFrecTotalAmount(Integer frecTotalAmount) {
		this.frecTotalAmount = frecTotalAmount;
	}

	public Integer getFreceiptAmount() {
		return freceiptAmount;
	}

	public void setFreceiptAmount(Integer freceiptAmount) {
		this.freceiptAmount = freceiptAmount;
	}

	public Integer getFrefundFee() {
		return frefundFee;
	}

	public void setFrefundFee(Integer frefundFee) {
		this.frefundFee = frefundFee;
	}

	public String getFgmtRefundPay() {
		return fgmtRefundPay;
	}

	public void setFgmtRefundPay(String fgmtRefundPay) {
		this.fgmtRefundPay = fgmtRefundPay;
	}

	public String getFoperatorId() {
		return foperatorId;
	}

	public void setFoperatorId(String foperatorId) {
		this.foperatorId = foperatorId;
	}

	public String getFstoreId() {
		return fstoreId;
	}

	public void setFstoreId(String fstoreId) {
		this.fstoreId = fstoreId;
	}

	public String getFterminalId() {
		return fterminalId;
	}

	public void setFterminalId(String fterminalId) {
		this.fterminalId = fterminalId;
	}

	public String getFappId() {
		return fappId;
	}

	public void setFappId(String fappId) {
		this.fappId = fappId;
	}


}
