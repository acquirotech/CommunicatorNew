package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Acq_TposLogin_Model {

	@Pattern(regexp="^[0-9]{10,10}$",message="Invalid Credentials")
	private String loginId;
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9\\!\\#\\@\\$\\_]{8,50}$",message="Invalid Credentials")
	private String password;
		

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
