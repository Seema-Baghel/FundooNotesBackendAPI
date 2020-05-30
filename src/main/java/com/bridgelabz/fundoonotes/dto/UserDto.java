package com.bridgelabz.fundoonotes.dto;

import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	@Pattern(regexp = "^[A-Z][a-z\\s]{3,}", message = "only alphabets are allowed")
	private String  firstName;
	
	@Pattern(regexp = "^[A-Z][a-z\\s]{3,}", message = "only alphabets are allowed")
    private String  lastName;
	
	@Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "only valid email are allowed")
    private String  email;
	
	@Pattern(regexp = "[7-9]{1}[0-9]{9}", message = "only valid mobile number are allowed")
	private long mobile;
	
	@Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}", message = "length should be 8 must contain atleast one uppercase, lowercase, special character and number")
    private String  password;
    
}
