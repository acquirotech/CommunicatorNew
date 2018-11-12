package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class Acq_TW_TxnStatus_Model {

@NotNull
@Pattern(regexp="^[0-9]{1,30}$",message="invalid session id")	
private String sessionId;
@NotNull
@Pattern(regexp="^[0-9]{1,20}$",message="invalid subscriber id")	
private String subscriberId;

private String rechargeId;


	
	public String getRechargeId() {
	return rechargeId;
}
public void setRechargeId(String rechargeId) {
	this.rechargeId = rechargeId;
}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	
	
	
}
