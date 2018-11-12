package com.acq.users.entity;

import java.math.BigDecimal;

public class Acq_Tw_Recharge_Entity {
	private String id;
	private BigDecimal amount;
	private String sessionId;
	private String subscriberId;
	private String operator;
	private String circle;
	private String rechargeType;
	private String paymentMode;	
	private String cardTransactionId;
	private String serviceType;
	private String twId;
	private String status;
	private String dateTime;
	private String message;
	private String twStatusCode;
	private String optId;
	private String serviceTax;
	
	
	public String getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(String serviceTax) {
		this.serviceTax = serviceTax;
	}
	public String getOptId() {
		return optId;
	}
	public void setOptId(String optId) {
		this.optId = optId;
	}
	public String getTwStatusCode() {
		return twStatusCode;
	}
	public void setTwStatusCode(String twStatusCode) {
		this.twStatusCode = twStatusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getTwId() {
		return twId;
	}
	public void setTwId(String twId) {
		this.twId = twId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getRechargeType() {
		return rechargeType;
	}
	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getCardTransactionId() {
		return cardTransactionId;
	}
	public void setCardTransactionId(String cardTransactionId) {
		this.cardTransactionId = cardTransactionId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	



}
