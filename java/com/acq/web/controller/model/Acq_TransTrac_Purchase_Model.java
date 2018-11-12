package com.acq.web.controller.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.json.simple.JSONObject;


public class Acq_TransTrac_Purchase_Model{

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9\\.]{1,20}$",message="Invalid Latitude")
	private String latitude;
	
	@NotNull
	@Pattern(regexp="^[0-9\\.]{1,20}$",message="Invalid Longitude")
	private String longitude;
	
	@NotNull
	@Pattern(regexp="^[0-9\\.]{1,20}$",message="Invalid IpAddress")
	private String ipAddress;
	
	@NotNull
	@Pattern(regexp="^[0-9a-zA-Z]{1,20}$",message="Invalid DeviceId")
	private String deviceId;
	
	@NotNull
	@Pattern(regexp="^[0-9a-zA-Z]{10,25}$",message="Invalid MaskPan")
	private String maskPan;	
	
	@NotNull
	@Pattern(regexp="^(True||False)?$",message="Invalid EMV")
	private String emv;
	
	@NotNull
	@Pattern(regexp="^(True||False)?$",message="Invalid FallBack")
	private String fallBack;
	
	@NotNull
	@Pattern(regexp="^[0-9]{0,1}$",message="Invalid UAT")
	private String uat;
	
	
	@Pattern(regexp="^[0-9a-zA-Z]{0,40}$",message="Invalid AID")
	private String aid;
	
	@NotNull
	@Pattern(regexp="^(True||False)?$",message="Invalid PinEntered")
	private String pinEntered;
	
	@NotNull
	@Valid
	private JSONObject isoData;
	
	@Size(min=0,max=100,message="Invalid CardHolderName")
	private String cardHolderName;
	
	@Size(min=0,max=50,message="Invalid ApplicationCertificate")
	private String applicationCertificate;
	
	@Size(min=0,max=60,message="Invalid ApplicationName")
	private String applicationName;
	
	private String rrNo;
	private String gmtDateTime;
	
	private String tid;//for internal use
	private String mid;//for internal use
	private String merchantAddress;
	private String merchantPinCode;
	private String mcc;
	private String de61;
	private String requestIso;
	private String respnseIso;
	private String requestTime;
	private String txnId;
	private String de12;
	private String de13;
	private String acquirerId;
	private String stan;
	private String cardType;
	
	
	
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getDe13() {
		return de13;
	}
	public void setDe13(String de13) {
		this.de13 = de13;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getStan() {
		return stan;
	}
	public void setStan(String stan) {
		this.stan = stan;
	}
	public String getAcquirerId() {
		return acquirerId;
	}
	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public String getApplicationCertificate() {
		return applicationCertificate;
	}
	public void setApplicationCertificate(String applicationCertificate) {
		this.applicationCertificate = applicationCertificate;
	}
	public String getDe12() {
		return de12;
	}
	public void setDe12(String de12) {
		this.de12 = de12;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getDe61() {
		return de61;
	}
	public void setDe61(String de61) {
		this.de61 = de61;
	}
	public String getRequestIso() {
		return requestIso;
	}
	public void setRequestIso(String requestIso) {
		this.requestIso = requestIso;
	}
	public String getRespnseIso() {
		return respnseIso;
	}
	public void setRespnseIso(String respnseIso) {
		this.respnseIso = respnseIso;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMerchantAddress() {
		return merchantAddress;
	}
	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}
	public String getMerchantPinCode() {
		return merchantPinCode;
	}
	public void setMerchantPinCode(String merchantPinCode) {
		this.merchantPinCode = merchantPinCode;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getRrNo() {
		return rrNo;
	}
	public void setRrNo(String rrNo) {
		this.rrNo = rrNo;
	}
	public String getGmtDateTime() {
		return gmtDateTime;
	}
	public void setGmtDateTime(String gmtDateTime) {
		this.gmtDateTime = gmtDateTime;
	}
	public String getUat() {
		return uat;
	}
	public void setUat(String uat) {
		this.uat = uat;
	}
	public String getPinEntered() {
		return pinEntered;
	}
	public void setPinEntered(String pinEntered) {
		this.pinEntered = pinEntered;
	}
	public String getFallBack() {
		return fallBack;
	}
	public void setFallBack(String fallBack) {
		this.fallBack = fallBack;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getEmv() {
		return emv;
	}
	public void setEmv(String emv) {
		this.emv = emv;
	}
	/*public String getMerchantData() {
		return merchantData;
	}
	public void setMerchantData(String merchantData) {
		this.merchantData = merchantData;
	}*/
	public JSONObject getIsoData() {
		return isoData;
	}
	public void setIsoData(JSONObject isoData) {
		this.isoData = isoData;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getMaskPan() {
		return maskPan;
	}
	public void setMaskPan(String maskPan) {
		this.maskPan = maskPan;
	}
	/*public List<OGSInitiateModel> getIsoData() {
		return isoData;
	}
	public void setIsoData(List<OGSInitiateModel> isoData) {
		this.isoData = isoData;
	}*/
		
	
	/*@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Latitude")
	private String latitude;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Longitude")
	private String longitude;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid IpAddress")
	private String ipAddress;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid DeviceId")
	private String deviceId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TID")
	private String tid;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid MaskPan")
	private String maskPan;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid MerchantData")
	private String merchantData;
	
	@NotNull
	private String emv;
	
	private String fallBack;
	
	private String aid;
	
	private String track2Data;
	
	private String pinEntered;
	
	@NotNull
	@Valid
	private JSONObject isoData;
	
	
	
	public String getPinEntered() {
		return pinEntered;
	}
	public void setPinEntered(String pinEntered) {
		this.pinEntered = pinEntered;
	}
	public String getFallBack() {
		return fallBack;
	}
	public void setFallBack(String fallBack) {
		this.fallBack = fallBack;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getTrack2Data() {
		return track2Data;
	}
	public void setTrack2Data(String track2Data) {
		this.track2Data = track2Data;
	}
	public String getEmv() {
		return emv;
	}
	public void setEmv(String emv) {
		this.emv = emv;
	}
	public String getMerchantData() {
		return merchantData;
	}
	public void setMerchantData(String merchantData) {
		this.merchantData = merchantData;
	}
	public JSONObject getIsoData() {
		return isoData;
	}
	public void setIsoData(JSONObject isoData) {
		this.isoData = isoData;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getMaskPan() {
		return maskPan;
	}
	public void setMaskPan(String maskPan) {
		this.maskPan = maskPan;
	}*/
	
}
