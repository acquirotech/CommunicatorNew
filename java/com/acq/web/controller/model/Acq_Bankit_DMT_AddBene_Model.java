package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class Acq_Bankit_DMT_AddBene_Model {

@NotNull
@Pattern(regexp="^[0-9]{1,30}$",message="invalid session id")	
private String sessionId;
@NotNull
@Pattern(regexp="^[0-9]{1,20}$",message="invalid Customer id")	
private String customerId;
/*@NotNull
@Pattern(regexp="^[0-9]{0,20}$",message="invalid agent code")	
*/private String agentCode;

@NotNull
//@Pattern(regexp="^[a-zA-Z0-9//s//.]{2,30}$",message="Invalid bank Name")
private String bankName;

private String dateTime;

@Pattern(regexp="^[a-zA-Z0-9]{0,50}$",message="invalid Account")	
private String accountNo;

@NotNull
@Pattern(regexp="^[a-zA-Z0-9]{1,20}$",message="invalid ifsc")	
private String ifsc;

@NotNull
@Pattern(regexp="^[0-9]{10,12}$",message="invalid  Mobile")	
private String mobileNo;

@NotNull
@Pattern(regexp="^[a-zA-Z0-9]{2,50}$",message="Invalid recipientName")
private String recipientName;



	public String getDateTime() {
	return dateTime;
}
public void setDateTime(String dateTime) {
	this.dateTime = dateTime;
}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getIfsc() {
		return ifsc;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	
	
	
	
	
}
