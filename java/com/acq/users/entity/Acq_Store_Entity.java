package com.acq.users.entity;


public class Acq_Store_Entity {
	private Long id;
	private String orgName;
	private Long merchantId;
	private String orgAddress1;
	private String orgAddress2;
	private String city;
	private String state;
	private String country;
	private String zip;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	
	public String getOrgAddress1() {
		return orgAddress1;
	}
	public void setOrgAddress1(String orgAddress1) {
		this.orgAddress1 = orgAddress1;
	}
	
	public String getOrgAddress2() {
		return orgAddress2;
	}
	public void setOrgAddress2(String orgAddress2) {
		this.orgAddress2 = orgAddress2;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	
	
}
