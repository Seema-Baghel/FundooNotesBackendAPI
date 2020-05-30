package com.bridgelabz.fundoonotes.exception;

public class InvalidCredentialsException extends RuntimeException {	
	
	private static final long serialVersionUID = 1L;
	private int status;

	public InvalidCredentialsException(String message) {
		super(message);	
	}

	public InvalidCredentialsException(String message, int status) {
		super(message);	
		this.status= status;
	}

	public int getStatus() {
		return status;
	}
}
