package com.acq.web.dto.impl;

import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse2;

public class ControllerResponse2<T> implements ResponseInf<T> {

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


	public static ControllerResponse2 createControllerResponse2(ServiceDto2 serviceResponse){
		ControllerResponse2 controllerResponse = new ControllerResponse2();
		controllerResponse.setBody(serviceResponse.getBody());
		controllerResponse.setStatusCode(serviceResponse.getStatusCode());
		controllerResponse.setStatusMessage(serviceResponse.getStatusMessage());
		return controllerResponse;
		
	}
	
		
}