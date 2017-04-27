package com.guru.exception;

public class OriginNearlyException extends Exception{

	private String errorCodeOrigin="No_BusStation_Nearly_Origin_Exception";

	public OriginNearlyException(String errorCodeOrigin) {
		super(errorCodeOrigin);
		this.errorCodeOrigin = errorCodeOrigin;
	}

	public String getErrorCodeOrigin() {
		return errorCodeOrigin;
	}

	
}
