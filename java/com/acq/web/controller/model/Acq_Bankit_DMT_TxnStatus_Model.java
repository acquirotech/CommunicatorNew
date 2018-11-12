package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class Acq_Bankit_DMT_TxnStatus_Model {

@NotNull
@Pattern(regexp="^[0-9]{1,30}$",message="invalid session id")	
private String sessionId;



@Pattern(regexp="^[a-zA-Z0-9]{0,50}$",message="Invalid recipientName")
private String clientRefId;

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
	public String getClientRefId() {
		return clientRefId;
	}
	public void setClientRefId(String clientRefId) {
		this.clientRefId = clientRefId;
	}

	
}
