package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class Acq_TW_DoRecharge_Model {

@NotNull
@Pattern(regexp="^[0-9]{1,30}$",message="invalid session id")	
private String sessionId;
@NotNull
@Pattern(regexp="^[0-9]{1,20}$",message="invalid subscriber id")	
private String subscriberId;
@NotNull
@Pattern(regexp="^[0-9]{1,5}$",message="invalid Operator")	
private String operator;

@Pattern(regexp="^[0-9]{0,5}$",message="invalid Circle")	
private String circle;
@NotNull
@Pattern(regexp="^(TOPUP||POSTPAID)?$",message="Invalid Recharge Type")
private String rechargeType;
@NotNull
@Pattern(regexp="^(CARD||CASH)?$",message="Invalid Payment Mode")
private String paymentMode;
private String rechargeId;
private String cardTransactionId;
@NotNull
@Pattern(regexp="^(MOBILE||DTH)?$",message="Invalid Service Type")
private String serviceType;
@NotNull
@Size(min=1,max=10,message="Invalid Amount")
@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{1,3}$)?$",message="Invalid Amount")
private String amount;
	


	public String getAmount() {
	return amount;
}
public void setAmount(String amount) {
	this.amount = amount;
}
	public String getRechargeId() {
	return rechargeId;
}
public void setRechargeId(String rechargeId) {
	this.rechargeId = rechargeId;
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
