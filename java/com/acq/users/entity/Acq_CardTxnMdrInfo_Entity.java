package com.acq.users.entity;
public class Acq_CardTxnMdrInfo_Entity {

	private Long id;
	private String mdrCat;
	private String bankMdr;
	private String ezMdr;
	private String serviceTax;
	private Acq_CardTxnInfo_Entity detailsEntity;
	
	
	public Acq_CardTxnInfo_Entity getDetailsEntity() {
		return detailsEntity;
	}
	public void setDetailsEntity(Acq_CardTxnInfo_Entity detailsEntity) {
		this.detailsEntity = detailsEntity;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getMdrCat() {
		return mdrCat;
	}
	public void setMdrCat(String mdrCat) {
		this.mdrCat = mdrCat;
	}
	
	
	public String getBankMdr() {
		return bankMdr;
	}
	public void setBankMdr(String bankMdr) {
		this.bankMdr = bankMdr;
	}
	
	
	public String getEzMdr() {
		return ezMdr;
	}
	public void setEzMdr(String ezMdr) {
		this.ezMdr = ezMdr;
	}
	
	
	public String getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(String serviceTax) {
		this.serviceTax = serviceTax;
	}
}
