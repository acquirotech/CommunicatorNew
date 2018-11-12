package com.acq.web.controller.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.json.simple.JSONObject;

public class Acq_TransTrac_VoidTxn_Model {
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TransactionId")
	private String transactionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9a-zA-Z]{1,20}$",message="Invalid DeviceId")
	private String deviceId;
	
	@NotNull
	@Pattern(regexp="^[0-9a-zA-Z]{10,25}$",message="Invalid MaskPan")
	private String maskPan;
	
	@NotNull
	@Pattern(regexp="^[0-9]{0,1}$",message="Invalid UAT")
	private String uat;
	
	@NotNull
	@Pattern(regexp="^[A-Z]{3,10}$",message="Invalid TxnType")
	private String txnType;
	
	@NotNull
	@Size(min=1,max=10,message="Invalid Amount")
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{1,3}$)?$",message="Invalid Amount")
	private String amount;
		
	@NotNull
	@Pattern(regexp="^(True||False)?$",message="Invalid EMV")
	private String emv;
		
	@NotNull
	@Valid
	private JSONObject isoData;
	
	private String scriptResponse;
	
	private String tid;
	private String mid;
	private String mcc;
	private String de13;
	private String de61;
	

	private String gmtDateTime;
	private String localTime;
	private String addtionalAmount;
	private String acquirerId;
	private String stan;
	private String rrNo;
	
	
	
	public String getDe13() {
		return de13;
	}
	public void setDe13(String de13) {
		this.de13 = de13;
	}
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
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getAddtionalAmount() {
		return addtionalAmount;
	}
	public void setAddtionalAmount(String addtionalAmount) {
		this.addtionalAmount = addtionalAmount;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
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

	public String getTransactionId() {
		return transactionId;
	}

public void setTransactionId(String transactionId) {
	this.transactionId = transactionId;
}

	public String getScriptResponse() {
	return scriptResponse;
}

public void setScriptResponse(String scriptResponse) {
	this.scriptResponse = scriptResponse;
}



	public String getEmv() {
		return emv;
	}

	public void setEmv(String emv) {
		this.emv = emv;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getLocalTime() {
		return localTime;
	}

	public void setLocalTime(String localTime) {
		this.localTime = localTime;
	}

	

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
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
