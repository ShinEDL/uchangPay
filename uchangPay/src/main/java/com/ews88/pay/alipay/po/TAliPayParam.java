package com.ews88.pay.alipay.po ;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TAliPayParam")
public class TAliPayParam {
	@Id
	@Column(name = "FId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer fid;
	@Column(name = "FAppID")
	private String fappId;
	@Column(name = "FAliPayPublicKey")
	private String faliPayPublicKey;// 支付宝公钥
	@Column(name = "FPrivateKey")
	private String fprivateKey;// 账户私钥
	@Column(name = "FPublicKey")
	private String fpublicKey;// 账户公钥

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getFappId() {
		return fappId;
	}

	public void setFappId(String fappId) {
		this.fappId = fappId;
	}

	public String getFaliPayPublicKey() {
		return faliPayPublicKey;
	}

	public void setFaliPayPublicKey(String faliPayPublicKey) {
		this.faliPayPublicKey = faliPayPublicKey;
	}

	public String getFprivateKey() {
		return fprivateKey;
	}

	public void setFprivateKey(String fprivateKey) {
		this.fprivateKey = fprivateKey;
	}

	public String getFpublicKey() {
		return fpublicKey;
	}

	public void setFpublicKey(String fpublicKey) {
		this.fpublicKey = fpublicKey;
	}

	@Override
	public String toString() {
		return "TAliPayParam [fid=" + fid + ", fappId=" + fappId + ", faliPayPublicKey=" + faliPayPublicKey
				+ ", fprivateKey=" + fprivateKey + ", fpublicKey=" + fpublicKey + "]";
	}

	
}
