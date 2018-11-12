package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Acq_TransTrac_GetTMK_Model {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,3}$",message="Invalid UAT")
	private String uat;
	
	@NotNull
	@Pattern(regexp="^[0-9a-zA-Z]{3,20}$",message="Invalid DeviceId")
	private String deviceId;
	
	private String stan;
	
	@Pattern(regexp="^[0-9]{0,50}$",message="Invalid DE44")
	private String de44;
	
	private String kin;
	
	
	public String getKin() {
		return kin;
	}

	public void setKin(String kin) {
		this.kin = kin;
	}

	public String getDe44() {
		return de44;
	}

	public void setDe44(String de44) {
		this.de44 = de44;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUat() {
		return uat;
	}

	public void setUat(String uat) {
		this.uat = uat;
	}
}
