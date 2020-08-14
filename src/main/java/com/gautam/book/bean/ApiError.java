package com.gautam.book.bean;

public class ApiError {
	
	public ApiError() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private String errorMessage;
	private int httpStatusCode;
	
	public ApiError(String errorMessage, int httpStatusCode) {
		super();
		this.errorMessage = errorMessage;
		this.httpStatusCode = httpStatusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	@Override
	public String toString() {
		return "ApiError [errorMessage=" + errorMessage + ", httpStatusCode=" + httpStatusCode + "]";
	}
	
}
