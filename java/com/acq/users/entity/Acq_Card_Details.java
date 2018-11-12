package com.acq.users.entity;

public class Acq_Card_Details {
	private int id;
	private int transactionId;
	private String terminalId;
	private String cardHolderName;
	private String cardType;
	private String ipAddress;
	private String imeiNo;
	private String latitude;
	private String longitude;
	private String rrNo;
	private String authCode;
	private String batchNo;
	private String stan;
	private String gmtDateTime;
	private String de61;
	private String pinEntered;
	private String applicationName;
	
	
	
	
	public String getPinEntered() {
		return pinEntered;
	}
	public void setPinEntered(String pinEntered) {
		this.pinEntered = pinEntered;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getDe61() {
		return de61;
	}
	public void setDe61(String de61) {
		this.de61 = de61;
	}
	public String getGmtDateTime() {
		return gmtDateTime;
	}
	public void setGmtDateTime(String gmtDateTime) {
		this.gmtDateTime = gmtDateTime;
	}
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getRrNo() {
		return rrNo;
	}
	public void setRrNo(String rrNo) {
		this.rrNo = rrNo;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getStan() {
		return stan;
	}
	public void setStan(String stan) {
		this.stan = stan;
	}
	
	
	
}