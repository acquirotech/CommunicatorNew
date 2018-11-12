package com.acq.users.entity;


public class Acq_Merchant_Entity {
	
	private String merchantId;
	private String marketingName;
	private String merchantName;
	private String merchantTID;
	private String merchantPinCode;
	private String address1;
	
	
	public String getMerchantPinCode() {
		return merchantPinCode;
	}




	public void setMerchantPinCode(String merchantPinCode) {
		this.merchantPinCode = merchantPinCode;
	}




	public String getAddress1() {
		return address1;
	}




	public void setAddress1(String address1) {
		this.address1 = address1;
	}




	public Acq_Merchant_Entity(){}
	
	
	
	
	public String getMerchantTID() {
		return merchantTID;
	}




	public void setMerchantTID(String merchantTID) {
		this.merchantTID = merchantTID;
	}




	public String getMerchantName() {
		return merchantName;
	}




	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}




	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}	
	
	public String getMarketingName() {
		return marketingName;
	}
	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}
}
