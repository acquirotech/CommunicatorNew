package com.acq.web.dto.impl;

import com.acq.web.dto.Page;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse;
import com.acq.web.dto.impl.ServiceDto;

public class ControllerResponse<T> implements ResponseInf<T> {

	int status;
	String message;
	T result;
	Page page;
	
	
	

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
	public static ControllerResponse createControllerResponse(ServiceDto serviceResponse){
		ControllerResponse controllerResponse = new ControllerResponse();
		controllerResponse.setMessage(serviceResponse.getMessage());
		//controllerResponse.setPage(serviceResponse.getPage());
		controllerResponse.setResult(serviceResponse.getResult());
		controllerResponse.setStatus(serviceResponse.getStatus());
		return controllerResponse;
		
	}	
}