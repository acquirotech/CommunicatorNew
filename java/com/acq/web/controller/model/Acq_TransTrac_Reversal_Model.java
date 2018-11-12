package com.acq.web.controller.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.json.simple.JSONObject;

public class Acq_TransTrac_Reversal_Model {
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TransactionId")
	private String transactionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9\\.]{1,20}$",message="Invalid Latitude")
	private String latitude;
	
	@NotNull
	@Pattern(regexp="^[0-9\\.]{1,20}$",message="Invalid Longitude")
	private String longitude;
	
	@NotNull
	@Pattern(regexp="^[0-9\\.]{1,20}$",message="Invalid IpAddress")
	private String ipAddress;
	
	@NotNull
	@Pattern(regexp="^[0-9a-zA-Z]{1,20}$",message="Invalid DeviceId")
	private String deviceId;
	
	
	@NotNull
	@Pattern(regexp="^[0-9a-zA-Z]{10,25}$",message="Invalid MaskPan")
	private String maskPan;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1}$",message="Invalid UAT")
	private String uat;	
	
	
	
	@NotNull
	@Pattern(regexp="^(True||False)?$",message="Invalid EMV")
	private String emv;
	
	@NotNull
	@Valid
	private JSONObject isoData;
	
	
	private String de61;
	private String de38;
	
	private String tid;
	private String mid;
	private String merchantAddress;
	private String merchantPinCode;
	private String mcc;
	private String de12;
	private String de54;
	private String txnType;
	private String acquirerId;
	private String stan;
	private String rrNo;
	
	
	
	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getRrNo() {
		return rrNo;
	}

	public void setRrNo(String rrNo) {
		this.rrNo = rrNo;
	}

	public String getAcquirerId() {
		return acquirerId;
	}

	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getDe54() {
		return de54;
	}

	public void setDe54(String de54) {
		this.de54 = de54;
	}

	public String getDe12() {
		return de12;
	}

	public void setDe12(String de12) {
		this.de12 = de12;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public String getMerchantPinCode() {
		return merchantPinCode;
	}

	public void setMerchantPinCode(String merchantPinCode) {
		this.merchantPinCode = merchantPinCode;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getDe38() {
		return de38;
	}

	public void setDe38(String de38) {
		this.de38 = de38;
	}

	public String getDe61() {
		return de61;
	}

	public void setDe61(String de61) {
		this.de61 = de61;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}	

	public String getEmv() {
		return emv;
	}

	public void setEmv(String emv) {
		this.emv = emv;
	}

	public String getUat() {
		return uat;
	}

	public void setUat(String uat) {
		this.uat = uat;
	}

	public JSONObject getIsoData() {
		return isoData;
	}

	public void setIsoData(JSONObject isoData) {
		this.isoData = isoData;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMaskPan() {
		return maskPan;
	}

	public void setMaskPan(String maskPan) {
		this.maskPan = maskPan;
	}



}
