package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class Acq_Bankit_DMT_AddSender_Model {

@NotNull
@Pattern(regexp="^[0-9]{1,30}$",message="invalid session id")	
private String sessionId;

@NotNull
@Pattern(regexp="^[0-9]{1,20}$",message="invalid Customer id")	
private String customerId;

/*@NotNull
@Pattern(regexp="^[0-9]{0,20}$",message="invalid agent code")	*/
private String agentCode;

@NotNull
@Pattern(regexp="^[a-zA-Z0-9//s//.]{2,30}$",message="Invalid Name")
private String name;


@Pattern(regexp="^[a-zA-Z0-9]{0,50}$",message="invalid address")	
private String address;

@NotNull
@Pattern(regexp="^[0-9\\-]{1,20}$",message="invalid Date Of Birth")	
private String dateOfBirth;

@NotNull
@Pattern(regexp="^[0-9]{6,6}$",message="invalid otp")	
private String otp;

private String dateTime;



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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}

	
	
	
	
}
