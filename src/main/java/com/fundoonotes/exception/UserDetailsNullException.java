package com.fundoonotes.exception;

public class UserDetailsNullException extends Exception {
	
	public enum ExceptionType{
		INVALID_EMAIL_ID, INVALID_PASSWORD;
	}
	
	public ExceptionType type;
	
	private String message;
	
	public UserDetailsNullException(String message, ExceptionType type) {
		this.message = message;
		this.type = type;
	}
	
	public UserDetailsNullException(String message) {
		super(message);
	}

}
