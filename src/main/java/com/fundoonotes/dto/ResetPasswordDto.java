package com.fundoonotes.dto;

public class ResetPasswordDto {

	private String password;
	private String confirmpassword;
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getConfirmpassword(){
		return confirmpassword;
	}
	
	public void setConfirmpassword(String confirmpassword){
		this.confirmpassword = confirmpassword;
	}

}
