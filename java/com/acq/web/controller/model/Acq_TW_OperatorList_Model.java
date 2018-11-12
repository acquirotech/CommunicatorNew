package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class Acq_TW_OperatorList_Model {

@NotNull
@Pattern(regexp="^[0-9]{1,30}$",message="invalid session id")	
private String sessionId;

private String date;

	public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
}
