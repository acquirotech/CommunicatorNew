package com.acq.web.dto.impl;

import com.acq.web.dto.Page;
import com.acq.web.dto.ServiceDtoInf;

public class ServiceDto2<T> implements ServiceDtoInf<T> {

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
	public static ServiceDto2 createHandlerResponse(DbDto2 daoResponse){
		ServiceDto2 serviceResponse = new ServiceDto2();
		serviceResponse.setBody(daoResponse.getBody());
		serviceResponse.setStatusCode(daoResponse.getStatusCode());
		serviceResponse.setStatusMessage(daoResponse.getStatusMessage());
		return serviceResponse;
		
	}
	
}
