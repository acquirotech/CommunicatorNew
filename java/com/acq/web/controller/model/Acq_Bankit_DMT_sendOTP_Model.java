package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class Acq_Bankit_DMT_sendOTP_Model {

@NotNull
@Pattern(regexp="^[0-9]{1,30}$",message="invalid session id")	
private String sessionId;
@NotNull
@Pattern(regexp="^[0-9]{1,20}$",message="invalid Customer id")	
private String customerId;

//@Pattern(regexp="^[0-9]{0,20}$",message="invalid agent code")	
private String agentCode;

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

	
	
	
	
}
