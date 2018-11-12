package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Acq_PurchaseAck_Model {
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TransactionId")
	private String transactionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid SessionId")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,1}$",message="Invalid StatusCode")
	private String statusCode;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,30}$",message="Invalid RRNo")
	private String rrNo;
	
	@Pattern(regexp="^[0-9a-zA-Z]{0,300}$",message="Invalid ScriptResponse")
	private String scriptResponse;
	
	
	private String signImage;
	
	@NotNull
	@Pattern(regexp="^[0-9]{0,12}$",message="Invalid MobileNumber")
	private String mobileNumber;	
	
	
	@NotNull
	@Pattern(regexp="^[0-9A-Z]{1,20}$",message="Invalid DeviceId")
	private String deviceId;
	
	@NotNull
	@Pattern(regexp="^[0-9A-Z]{1,20}$",message="Invalid TID")
	private String tid;
	
	
	private String emailId;
	
	
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getRrNo() {
		return rrNo;
	}
	public void setRrNo(String rrNo) {
		this.rrNo = rrNo;
	}
	public String getScriptResponse() {
		return scriptResponse;
	}
	public void setScriptResponse(String scriptResponse) {
		this.scriptResponse = scriptResponse;
	}
	
	public String getSignImage() {
		return signImage;
	}
	public void setSignImage(String signImage) {
		this.signImage = signImage;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	
}
