package com.bridgelabz.fundoonotes.responses;

public class UserDetailsResponse {

	private String message;
	private int status;
	private String token;
	private String fname;

	public UserDetailsResponse(String message, int status, String token, String fname) {
		this.message = message;
		this.status = status;
		this.token = token;
		this.fname = fname;
	}

	public UserDetailsResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}



}
