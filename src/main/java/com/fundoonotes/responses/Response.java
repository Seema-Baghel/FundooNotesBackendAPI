package com.fundoonotes.responses;

public class Response {
	
	private int status;
	private String message;
	
	public Response(String message,int status) {
		this.message = message;
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
