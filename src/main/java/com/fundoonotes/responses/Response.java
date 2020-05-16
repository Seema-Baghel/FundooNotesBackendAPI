package com.fundoonotes.responses;

public class Response {
	
	private int status;
	private String message;
	private Object data;
	
	public Response(int status,String message) {
		this.status = status;
		this.message = message;
	}
	
	public Response(String message,int status, Object data) {
		this.message = message;
		this.status = status;
		this.data = data;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
