package com.acq.web.dto.impl;

import java.io.Serializable;

import com.acq.web.dto.DbDtoInf;
import com.acq.web.dto.Page;

public class DbDto2<T> implements DbDtoInf<T> , Serializable{

	
	int statusCode;
	String statusMessage;
	T body;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	
	
}
