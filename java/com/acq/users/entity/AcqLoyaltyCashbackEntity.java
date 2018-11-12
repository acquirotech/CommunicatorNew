package com.acq.users.entity;


public class AcqLoyaltyCashbackEntity {
	private String id;
	private String loyaltyId;
	private String type;
	private String cashBackAmount;
	private String minAmountCashBack;
	private String maxAmountCashBack;
	private String lastUpdatedOn;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCashBackAmount() {
		return cashBackAmount;
	}
	public void setCashBackAmount(String cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}
	public String getMinAmountCashBack() {
		return minAmountCashBack;
	}
	public void setMinAmountCashBack(String minAmountCashBack) {
		this.minAmountCashBack = minAmountCashBack;
	}
	public String getMaxAmountCashBack() {
		return maxAmountCashBack;
	}
	public void setMaxAmountCashBack(String maxAmountCashBack) {
		this.maxAmountCashBack = maxAmountCashBack;
	}
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	
}
