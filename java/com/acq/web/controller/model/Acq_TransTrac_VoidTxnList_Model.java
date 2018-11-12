package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Acq_TransTrac_VoidTxnList_Model {

	@NotNull
	@Pattern(regexp="^[0-9A-Z]{10,20}$",message="Invalid MaskPan")
	private String maskPan;
	
	@NotNull
	@Pattern(regexp="^[0-9A-Z]{3,30}$",message="Invalid TID")
	private String tid;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid SessionId")
	private String sessionId;
	
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getMaskPan() {
		return maskPan;
	}
	public void setMaskPan(String maskPan) {
		this.maskPan = maskPan;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
}
