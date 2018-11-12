package com.acq.users.model;

import java.util.HashSet;
import java.util.Set;

public class Acq_User{

	private String username;
	private String password;
	private boolean enabled;
	private String empRole;
	private String empName;
	private Set<Acq_UserRole> userRole = new HashSet<Acq_UserRole>(0);

	public Acq_User() {
	}

	public Acq_User(String username, String password, boolean enabled,String empRole) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.empRole = empRole;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Acq_User(String username, String password, boolean enabled, Set<Acq_UserRole> userRole) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.userRole = userRole;
	}

	public String getEmpRole() {
		return empRole;
	}

	public void setEmpRole(String empRole) {
		this.empRole = empRole;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Acq_UserRole> getUserRole() {
		return this.userRole;
	}

	public void setUserRole(Set<Acq_UserRole> userRole) {
		this.userRole = userRole;
	}

}
