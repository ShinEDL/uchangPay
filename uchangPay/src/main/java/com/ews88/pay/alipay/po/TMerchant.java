package com.ews88.pay.alipay.po;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "TMerchant")
public class TMerchant implements java.io.Serializable {

	@Id
	@Column(name = "FId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer fid;

	@Column(name = "FMerchantNo")
	private String fmerchantNo;
	@Column(name = "FLogName")
	private String flogName;
	@Column(name = "FLogPwd")
	private String flogPwd;
	@Column(name = "FMerchantName")
	private String fmerchantName;
	@Column(name = "FOrderCostRate")
	private Integer forderCostRate;
	@Column(name = "FCapCost")
	private Integer fcapCost;
	@Column(name = "FBookCostType")
	private Integer fbookCostType;
	@Column(name = "FBankAccount")
	private String fbankAccount;
	@Column(name = "FBank")
	private String fbank;
	@Column(name = "FContact")
	private String fcontact;
	@Column(name = "FProvince")
	private String fprovince;
	@Column(name = "FCity")
	private String fcity;
	@Column(name = "FDistrict")
	private String fdistrict;
	@Column(name = "FAddr")
	private String faddr;
	@Column(name = "FBusinessAddr")
	private String fbusinessAddr;
	@Column(name = "FBusinessNo")
	private String fbusinessNo;
	@Column(name = "FPostCode")
	private String fpostCode;
	@Column(name = "FPhone")
	private String fphone;
	@Column(name = "FFax")
	private String ffax;
	@Column(name = "FWebsite")
	private String fwebsite;
	@Column(name = "FEmail")
	private String femail;
	@Column(name = "FType")
	private String ftype;
	@Column(name = "FStatus")
	private String fstatus;
	@Column(name = "FRegDate")
	private String fregDate;
	@Column(name = "FUpdateDate")
	private String fupdateDate;
	@Column(name = "FlastLoginDate")
	private String flastLoginDate;
	@Column(name = "FCurrentLogin")
	private String fcurrentLogin;
	@Column(name = "FDescription")
	private String fdescription;
	@Column(name = "FLatitude")
	private Double flatitude;
	@Column(name = "FLongitude")
	private Double flongitude;
	@Column(name = "FActionPath")
	private String factionPath;
	@Column(name = "FShowLevel")
	private Integer fshowLevel;
	@Column(name = "FMainItem")
	private String fmainItem;
	@Column(name = "FBusiTime")
	private String fbusiTime;
	@Column(name = "FDelivery")
	private String fdelivery;
	@Column(name = "FPreferences")
	private String fpreferences;
	@Column(name = "FTotalItems")
	private Integer ftotalItems;
	@Column(name = "FMerchantCategory")
	private String fmerchantCategory;
	@Column(name = "FJoinDate")
	private String fjoinDate;
	@Column(name = "FQuitDate")
	private String fquitDate;
	@Column(name = "FLeagueStatus")
	private String fleagueStatus;
	@Column(name = "FCardPayGiftRate")
	private Integer fcardPayGiftRate;
	@Column(name = "FIsNew")
	private String fisNew;
	@Column(name = "fisHot")
	private String FIsHot;
	@Column(name = "FListType")
	private String flistType;
	@Column(name = "FIsOnTime")
	private String fisOnTime;
	@Column(name = "FIsBook")
	private String fisBook;
	@Column(name = "FDeliverScope")
	private String fdeliverScope;
	@Column(name = "FDeliverTime")
	private String fdeliverTime;
	@Column(name = "FDeliverPriceDes")
	private String fdeliverPriceDes;
	@Column(name = "FDeliverNote")
	private String fdeliverNote;
	@Column(name = "FPromotion")
	private String fpromotion;
	@Column(name = "FNotice")
	private String fnotice;
	@Column(name = "FOpeningHours")
	private String fopeningHours;
	@Column(name = "FBookMoney")
	private Integer fbookMoney;
	@Column(name = "FServiceCost")
	private Integer fserviceCost;
	@Column(name = "FDeliverPrice")
	private Integer fdeliverPrice;
	@Column(name = "FMinChargeRoom")
	private Integer fminChargeRoom;
	@Column(name = "FMinChargeHall")
	private Integer fminChargeHall;
	@Column(name = "FFee")
	private Integer ffee;
	@Transient
	private Integer isOnline; // 标志是否pos离线
	@Transient
	private String isCheck; // 标志是否选中
	
	// 重写该方法，以进行集合的差集操作
	@Override
	public boolean equals(Object obj) {
		boolean result = true;
		TMerchant item = (TMerchant) obj;
		if (this.fid != item.fid) {
			result = false;
		}
		return result;
	}
	
	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getFmerchantNo() {
		return fmerchantNo;
	}

	public void setFmerchantNo(String fmerchantNo) {
		this.fmerchantNo = fmerchantNo;
	}

	public String getFlogName() {
		return flogName;
	}

	public void setFlogName(String flogName) {
		this.flogName = flogName;
	}

	public String getFlogPwd() {
		return flogPwd;
	}

	public void setFlogPwd(String flogPwd) {
		this.flogPwd = flogPwd;
	}

	public String getFmerchantName() {
		return fmerchantName.replaceAll("benandluya（", "").replaceAll("）", "");
	}

	public void setFmerchantName(String fmerchantName) {
		this.fmerchantName = fmerchantName;
	}

	public Integer getForderCostRate() {
		return forderCostRate;
	}

	public void setForderCostRate(Integer forderCostRate) {
		this.forderCostRate = forderCostRate;
	}

	public Integer getFcapCost() {
		return fcapCost;
	}

	public void setFcapCost(Integer fcapCost) {
		this.fcapCost = fcapCost;
	}

	public Integer getFbookCostType() {
		return fbookCostType;
	}

	public void setFbookCostType(Integer fbookCostType) {
		this.fbookCostType = fbookCostType;
	}

	public String getFbankAccount() {
		return fbankAccount;
	}

	public void setFbankAccount(String fbankAccount) {
		this.fbankAccount = fbankAccount;
	}

	public String getFbank() {
		return fbank;
	}

	public void setFbank(String fbank) {
		this.fbank = fbank;
	}

	public String getFcontact() {
		return fcontact;
	}

	public void setFcontact(String fcontact) {
		this.fcontact = fcontact;
	}

	public String getFprovince() {
		return fprovince;
	}

	public void setFprovince(String fprovince) {
		this.fprovince = fprovince;
	}

	public String getFcity() {
		return fcity;
	}

	public void setFcity(String fcity) {
		this.fcity = fcity;
	}

	public String getFdistrict() {
		return fdistrict;
	}

	public void setFdistrict(String fdistrict) {
		this.fdistrict = fdistrict;
	}

	public String getFaddr() {
		return faddr;
	}

	public void setFaddr(String faddr) {
		this.faddr = faddr;
	}

	public String getFbusinessAddr() {
		return fbusinessAddr;
	}

	public void setFbusinessAddr(String fbusinessAddr) {
		this.fbusinessAddr = fbusinessAddr;
	}

	public String getFbusinessNo() {
		return fbusinessNo;
	}

	public void setFbusinessNo(String fbusinessNo) {
		this.fbusinessNo = fbusinessNo;
	}

	public String getFpostCode() {
		return fpostCode;
	}

	public void setFpostCode(String fpostCode) {
		this.fpostCode = fpostCode;
	}

	public String getFphone() {
		return fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	public String getFfax() {
		return ffax;
	}

	public void setFfax(String ffax) {
		this.ffax = ffax;
	}

	public String getFwebsite() {
		return fwebsite;
	}

	public void setFwebsite(String fwebsite) {
		this.fwebsite = fwebsite;
	}

	public String getFemail() {
		return femail;
	}

	public void setFemail(String femail) {
		this.femail = femail;
	}

	public String getFtype() {
		return ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public String getFstatus() {
		return fstatus;
	}

	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}

	public String getFregDate() {
		return fregDate;
	}

	public void setFregDate(String fregDate) {
		this.fregDate = fregDate;
	}

	public String getFupdateDate() {
		return fupdateDate;
	}

	public void setFupdateDate(String fupdateDate) {
		this.fupdateDate = fupdateDate;
	}

	public String getFlastLoginDate() {
		return flastLoginDate;
	}

	public void setFlastLoginDate(String flastLoginDate) {
		this.flastLoginDate = flastLoginDate;
	}

	public String getFcurrentLogin() {
		return fcurrentLogin;
	}

	public void setFcurrentLogin(String fcurrentLogin) {
		this.fcurrentLogin = fcurrentLogin;
	}

	public String getFdescription() {
		return fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	public Double getFlatitude() {
		return flatitude;
	}

	public void setFlatitude(Double flatitude) {
		this.flatitude = flatitude;
	}

	public Double getFlongitude() {
		return flongitude;
	}

	public void setFlongitude(Double flongitude) {
		this.flongitude = flongitude;
	}

	public String getFactionPath() {
		return factionPath;
	}

	public void setFactionPath(String factionPath) {
		this.factionPath = factionPath;
	}

	public Integer getFshowLevel() {
		return fshowLevel;
	}

	public void setFshowLevel(Integer fshowLevel) {
		this.fshowLevel = fshowLevel;
	}

	public String getFmainItem() {
		return fmainItem;
	}

	public void setFmainItem(String fmainItem) {
		this.fmainItem = fmainItem;
	}

	public String getFbusiTime() {
		return fbusiTime;
	}

	public void setFbusiTime(String fbusiTime) {
		this.fbusiTime = fbusiTime;
	}

	public String getFdelivery() {
		return fdelivery;
	}

	public void setFdelivery(String fdelivery) {
		this.fdelivery = fdelivery;
	}

	public String getFpreferences() {
		return fpreferences;
	}

	public void setFpreferences(String fpreferences) {
		this.fpreferences = fpreferences;
	}

	public Integer getFtotalItems() {
		return ftotalItems;
	}

	public void setFtotalItems(Integer ftotalItems) {
		this.ftotalItems = ftotalItems;
	}

	public String getFmerchantCategory() {
		return fmerchantCategory;
	}

	public void setFmerchantCategory(String fmerchantCategory) {
		this.fmerchantCategory = fmerchantCategory;
	}

	public String getFjoinDate() {
		return fjoinDate;
	}

	public void setFjoinDate(String fjoinDate) {
		this.fjoinDate = fjoinDate;
	}


	public String getFquitDate() {
		return fquitDate;
	}

	public void setFquitDate(String fquitDate) {
		this.fquitDate = fquitDate;
	}

	public String getFleagueStatus() {
		return fleagueStatus;
	}

	public void setFleagueStatus(String fleagueStatus) {
		this.fleagueStatus = fleagueStatus;
	}

	public Integer getFcardPayGiftRate() {
		return fcardPayGiftRate;
	}

	public void setFcardPayGiftRate(Integer fcardPayGiftRate) {
		this.fcardPayGiftRate = fcardPayGiftRate;
	}

	public String getFisNew() {
		return fisNew;
	}

	public void setFisNew(String fisNew) {
		this.fisNew = fisNew;
	}

	public String getFIsHot() {
		return FIsHot;
	}

	public void setFIsHot(String fIsHot) {
		FIsHot = fIsHot;
	}

	public String getFlistType() {
		return flistType;
	}

	public void setFlistType(String flistType) {
		this.flistType = flistType;
	}

	public String getFisOnTime() {
		return fisOnTime;
	}

	public void setFisOnTime(String fisOnTime) {
		this.fisOnTime = fisOnTime;
	}

	public String getFisBook() {
		return fisBook;
	}

	public void setFisBook(String fisBook) {
		this.fisBook = fisBook;
	}

	public String getFdeliverScope() {
		return fdeliverScope;
	}

	public void setFdeliverScope(String fdeliverScope) {
		this.fdeliverScope = fdeliverScope;
	}

	public String getFdeliverTime() {
		return fdeliverTime;
	}

	public void setFdeliverTime(String fdeliverTime) {
		this.fdeliverTime = fdeliverTime;
	}

	public String getFdeliverPriceDes() {
		return fdeliverPriceDes;
	}

	public void setFdeliverPriceDes(String fdeliverPriceDes) {
		this.fdeliverPriceDes = fdeliverPriceDes;
	}

	public String getFdeliverNote() {
		return fdeliverNote;
	}

	public void setFdeliverNote(String fdeliverNote) {
		this.fdeliverNote = fdeliverNote;
	}

	public String getFpromotion() {
		return fpromotion;
	}

	public void setFpromotion(String fpromotion) {
		this.fpromotion = fpromotion;
	}

	public String getFnotice() {
		return fnotice;
	}

	public void setFnotice(String fnotice) {
		this.fnotice = fnotice;
	}

	public String getFopeningHours() {
		return fopeningHours;
	}

	public void setFopeningHours(String fopeningHours) {
		this.fopeningHours = fopeningHours;
	}

	public Integer getFbookMoney() {
		return fbookMoney;
	}

	public void setFbookMoney(Integer fbookMoney) {
		this.fbookMoney = fbookMoney;
	}

	public Integer getFserviceCost() {
		return fserviceCost;
	}

	public void setFserviceCost(Integer fserviceCost) {
		this.fserviceCost = fserviceCost;
	}

	public Integer getFdeliverPrice() {
		return fdeliverPrice;
	}

	public void setFdeliverPrice(Integer fdeliverPrice) {
		this.fdeliverPrice = fdeliverPrice;
	}

	public Integer getFminChargeRoom() {
		return fminChargeRoom;
	}

	public void setFminChargeRoom(Integer fminChargeRoom) {
		this.fminChargeRoom = fminChargeRoom;
	}

	public Integer getFminChargeHall() {
		return fminChargeHall;
	}

	public void setFminChargeHall(Integer fminChargeHall) {
		this.fminChargeHall = fminChargeHall;
	}


	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public Integer getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Integer isOnline) {
		this.isOnline = isOnline;
	}

	public Integer getFfee() {
		return ffee;
	}

	public void setFfee(Integer ffee) {
		this.ffee = ffee;
	}

}
