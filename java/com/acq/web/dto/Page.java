package com.acq.web.dto;

public class Page<T> {
	int pageSize;
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		if(pageSize!=null)
		  this.pageSize = pageSize;
	}
	public int getStart() {
		return start;
	}
	public void setStart(Integer start) {
		if(start!=null)
		this.start = start;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		if(total!=null)
		this.total = total;
	}
	int start;
	T result;
	int total;

}
