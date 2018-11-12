package com.acq.users.model;

public class Acq_UserRole {

	private Integer userRoleId;
	private Acq_User user;
	private String role;

	public Acq_UserRole() {
	}

	public Acq_UserRole(Acq_User user, String role) {
		this.user = user;
		this.role = role;
	}

	public Integer getUserRoleId() {
		return this.userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Acq_User getUser() {
		return this.user;
	}

	public void setUser(Acq_User user) {
		this.user = user;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
