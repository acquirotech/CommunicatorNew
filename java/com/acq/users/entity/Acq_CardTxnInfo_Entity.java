package com.acq.users.entity;
import java.io.Serializable;

public class Acq_CardTxnInfo_Entity implements Serializable {
	
	private Long id;
	private Long userId;
	private Long orgId;
	private Long merchantId;
	private String totalAmount;
	private String ipAddress;
	private String latitude;
	private String logitude;
	private String imeiNo;
	private String cardHolderName;
	private String cardType;
	private String cardExpDate;
	private String dateTime;
	private String custMobile;
	private String custEmail;
	private String cardPanNo;
	private String txnStatus;
	
	private Acq_CardTxnMdrInfo_Entity mdrdetailentity;
	
	
	public String getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}
	
	
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
   
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLogitude() {
		return logitude;
	}
	public void setLogitude(String logitude) {
		this.logitude = logitude;
	}
	
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}
	
	public String getCardPanNo() {
		return cardPanNo;
	}
	public void setCardPanNo(String cardPanNo) {
		this.cardPanNo = cardPanNo;
	}
	
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getCardExpDate() {
		return cardExpDate;
	}
	public void setCardExpDate(String cardExpDate) {
		this.cardExpDate = cardExpDate;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	
	public String getCustMobile() {
		return custMobile;
	}
	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}
	
	
	public Acq_CardTxnMdrInfo_Entity getmdrdetailentity() {
		return mdrdetailentity;
	}
	public void setmdrdetailentity(Acq_CardTxnMdrInfo_Entity mdrdetailentity) {
		this.mdrdetailentity = mdrdetailentity;
	}
}