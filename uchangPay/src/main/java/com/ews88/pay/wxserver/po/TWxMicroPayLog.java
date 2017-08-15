package com.ews88.pay.wxserver.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tencent.protocol.pay_protocol.ScanPayResData;

@SuppressWarnings("serial")
@Entity
@Table(name = "TWxMicroPayLog")
public class TWxMicroPayLog implements Serializable {

	@Id
	@Column(name = "FId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fid;
	// 协议层
	@Column(name = "return_code")
	private String return_code = "";
	@Column(name = "return_msg")
	private String return_msg = "";

	// 协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
	@Column(name = "appid")
	private String appid = "";
	@Column(name = "sub_appid")
	private String sub_appid = "";
	@Column(name = "mch_id")
	private String mch_id = "";
	@Column(name = "sub_mch_id")
	private String sub_mch_id = "";
	@Column(name = "nonce_str")
	private String nonce_str = "";
	@Column(name = "sign")
	private String sign = "";
	@Column(name = "result_code")
	private String result_code = "";
	@Column(name = "err_code")
	private String err_code = "";
	@Column(name = "err_code_des")
	private String err_code_des = "";

	@Column(name = "device_info")
	private String device_info = "";

	// 业务返回的具体数据（以下字段在return_code 和result_code 都为SUCCESS 的时候有返回）
	@Column(name = "openid")
	private String openid = "";
	@Column(name = "is_subscribe")
	private String is_subscribe = "";
	@Column(name = "sub_openid")
	private String sub_openid = "";
	@Column(name = "sub_is_subscribe")
	private String sub_is_subscribe = "";
	@Column(name = "trade_type")
	private String trade_type = "";
	@Column(name = "bank_type")
	private String bank_type = "";
	@Column(name = "fee_type")
	private String fee_type = "";
	@Column(name = "total_fee")
	private String total_fee = "";
	@Column(name = "cash_fee")
	private String cash_fee = "";
	@Column(name = "cash_fee_type")
	private String cash_fee_type = "";
	@Column(name = "coupon_fee")
	private String coupon_fee = "";
	@Column(name = "transaction_id")
	private String transaction_id = "";
	@Column(name = "out_trade_no")
	private String out_trade_no = "";
	@Column(name = "attach")
	private String attach = "";
	@Column(name = "time_end")
	private String time_end = "";
	@Column(name = "ews_merchant_no")
	private String ews_merchant_no = "";
	@Column(name = "ews_terminal_no")
	private String ews_terminal_no = "";
	@Column(name = "refund_fee")
	private String refund_fee = "";
	@Column(name = "isAudit")
	private String isaudit = "0";

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSub_appid() {
		return sub_appid;
	}

	public void setSub_appid(String sub_appid) {
		this.sub_appid = sub_appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getSub_mch_id() {
		return sub_mch_id;
	}

	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_code_des() {
		return err_code_des;
	}

	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getIs_subscribe() {
		return is_subscribe;
	}

	public void setIs_subscribe(String is_subscribe) {
		this.is_subscribe = is_subscribe;
	}

	public String getSub_openid() {
		return sub_openid;
	}

	public void setSub_openid(String sub_openid) {
		this.sub_openid = sub_openid;
	}

	public String getSub_is_subscribe() {
		return sub_is_subscribe;
	}

	public void setSub_is_subscribe(String sub_is_subscribe) {
		this.sub_is_subscribe = sub_is_subscribe;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getCash_fee() {
		return cash_fee;
	}

	public void setCash_fee(String cash_fee) {
		this.cash_fee = cash_fee;
	}

	public String getCash_fee_type() {
		return cash_fee_type;
	}

	public void setCash_fee_type(String cash_fee_type) {
		this.cash_fee_type = cash_fee_type;
	}

	public String getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(String coupon_fee) {
		this.coupon_fee = coupon_fee;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getEws_merchant_no() {
		return ews_merchant_no;
	}

	public void setEws_merchant_no(String ews_merchant_no) {
		this.ews_merchant_no = ews_merchant_no;
	}

	public String getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(String refund_fee) {
		this.refund_fee = refund_fee;
	}

	public String getEws_terminal_no() {
		return ews_terminal_no;
	}

	public void setEws_terminal_no(String ews_terminal_no) {
		this.ews_terminal_no = ews_terminal_no;
	}

	

	public String getIsaudit() {
		return isaudit;
	}

	public void setIsaudit(String isaudit) {
		this.isaudit = isaudit;
	}

	public TWxMicroPayLog() {
		super();
	}

	public TWxMicroPayLog(ScanPayResData data, String ews_merchant_no) {
		super();
		this.return_code = data.getReturn_code();
		this.return_msg = data.getReturn_msg();
		this.appid = data.getAppid();
		this.sub_appid = data.getSub_appid();
		this.mch_id = data.getMch_id();
		this.sub_mch_id = data.getSub_mch_id();
		this.nonce_str = data.getNonce_str();
		this.sign = data.getSign();
		this.result_code = data.getResult_code();
		this.err_code = data.getErr_code();
		this.err_code_des = data.getErr_code_des();
		this.device_info = data.getDevice_info();
		this.openid = data.getOpenid();
		this.is_subscribe = data.getIs_subscribe();
		this.sub_openid = data.getSub_openid();
		this.sub_is_subscribe = data.getSub_is_subscribe();
		this.trade_type = data.getTrade_type();
		this.bank_type = data.getBank_type();
		this.fee_type = data.getFee_type();
		this.total_fee = data.getTotal_fee();
		this.cash_fee = data.getCash_fee();
		this.cash_fee_type = data.getCash_fee_type();
//		this.coupon_fee = data.getCoupon_fee();
		this.transaction_id = data.getTransaction_id();
		this.out_trade_no = data.getOut_trade_no();
		this.attach = data.getAttach();
		this.time_end = data.getTime_end();
		this.ews_merchant_no = ews_merchant_no;
	}

}
