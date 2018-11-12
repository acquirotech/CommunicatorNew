package com.acq.users.entity;

import java.sql.Timestamp;


public class Acq_CardTransaction_Entity {	
	private int id;	
	private Integer merchantId;	
	private Integer orgId;	
	private Integer userid;	
	private String mobile;	
	private String amount;	
	private String email;	
	private int status;	
	private String ezMdr;	
	private String bankMdr;	
	private String serviceTax;		
	private Timestamp date;	
	private Timestamp otpDateTime;
	private String description;
	private String txnType;
	private String carPanNo;
	private String customerId;
	private int payoutStatus;
	private Timestamp payoutDateTime;
	private int loyaltyId;
	private int usedPoints; 
	private String acquirerCode;
	private String cashBackAmt;
	private String acquirerMdr;
	
	private String appCertificate;
	private String aid;
	private String systemUtilityFee;
	private String scriptResult;
	
	
	
	
	
	public String getScriptResult() {
		return scriptResult;
	}
	public void setScriptResult(String scriptResult) {
		this.scriptResult = scriptResult;
	}
	public String getSystemUtilityFee() {
		return systemUtilityFee;
	}
	public void setSystemUtilityFee(String systemUtilityFee) {
		this.systemUtilityFee = systemUtilityFee;
	}
	
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getAppCertificate() {
		return appCertificate;
	}
	public void setAppCertificate(String appCertificate) {
		this.appCertificate = appCertificate;
	}
	public String getAcquirerMdr() {
		return acquirerMdr;
	}
	public void setAcquirerMdr(String acquirerMdr) {
		this.acquirerMdr = acquirerMdr;
	}
	public String getCashBackAmt() {
		return cashBackAmt;
	}
	public void setCashBackAmt(String cashBackAmt) {
		this.cashBackAmt = cashBackAmt;
	}
	
	public String getAcquirerCode() {
		return acquirerCode;
	}
	public void setAcquirerCode(String acquirerCode) {
		this.acquirerCode = acquirerCode;
	}
	public int getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(int loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public int getUsedPoints() {
		return usedPoints;
	}
	public void setUsedPoints(int usedPoints) {
		this.usedPoints = usedPoints;
	}
	public String getCarPanNo() {
		return carPanNo;
	}
	public void setCarPanNo(String carPanNo) {
		this.carPanNo = carPanNo;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getEzMdr() {
		return ezMdr;
	}
	public void setEzMdr(String ezMdr) {
		this.ezMdr = ezMdr;
	}
	public String getBankMdr() {
		return bankMdr;
	}
	public void setBankMdr(String bankMdr) {
		this.bankMdr = bankMdr;
	}
	public String getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(String serviceTax) {
		this.serviceTax = serviceTax;
	}
	
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Timestamp getOtpDateTime() {
		return otpDateTime;
	}
	public void setOtpDateTime(Timestamp otpDateTime) {
		this.otpDateTime = otpDateTime;
	}
	
	
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public Timestamp getPayoutDateTime() {
		return payoutDateTime;
	}
	public void setPayoutDateTime(Timestamp payoutDateTime) {
		this.payoutDateTime = payoutDateTime;
	}
	public int getPayoutStatus() {
		return payoutStatus;
	}
	public void setPayoutStatus(int payoutStatus) {
		this.payoutStatus = payoutStatus;
	}
	
	
}
