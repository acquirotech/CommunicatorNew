package com.acq.users.entity;


public class Acq_TerminalInfo_Entity {

	private Long id;
	private Long userId;
	private String mdrDebit1;
	private String mdrDebit2;
	private String mdrCreditNpre;
	private String mdrCreditPre;
	private String mdrAmex;
	private String other;	
	private String mdrMobikwik;
	private String serialNo;
	private String deviceType;
	private String bankTid;
	private String mdrCashAtPos;
	private String rechargeBal;
	
	
	public String getRechargeBal() {
		return rechargeBal;
	}
	public void setRechargeBal(String rechargeBal) {
		this.rechargeBal = rechargeBal;
	}
	public String getMdrCashAtPos() {
		return mdrCashAtPos;
	}
	public void setMdrCashAtPos(String mdrCashAtPos) {
		this.mdrCashAtPos = mdrCashAtPos;
	}
	public String getBankTid() {
		return bankTid;
	}
	public void setBankTid(String bankTid) {
		this.bankTid = bankTid;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getMdrDebit1() {
		return mdrDebit1;
	}
	public void setMdrDebit1(String mdrDebit1) {
		this.mdrDebit1 = mdrDebit1;
	}
	
	public String getMdrDebit2() {
		return mdrDebit2;
	}
	public void setMdrDebit2(String mdrDebit2) {
		this.mdrDebit2 = mdrDebit2;
	}
	
	public String getMdrCreditNpre() {
		return mdrCreditNpre;
	}
	public void setMdrCreditNpre(String mdrCreditNpre) {
		this.mdrCreditNpre = mdrCreditNpre;
	}
	
	public String getMdrCreditPre() {
		return mdrCreditPre;
	}
	public void setMdrCreditPre(String mdrCreditPre) {
		this.mdrCreditPre = mdrCreditPre;
	}
	
	public String getMdrAmex() {
		return mdrAmex;
	}
	public void setMdrAmex(String mdrAmex) {
		this.mdrAmex = mdrAmex;
	}
	
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	public String getMdrMobikwik() {
		return mdrMobikwik;
	}
	public void setMdrMobikwik(String mdrMobikwik) {
		this.mdrMobikwik = mdrMobikwik;
	}
	
	
	
}