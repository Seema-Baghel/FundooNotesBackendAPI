package com.fundoonotes.service;

import org.springframework.stereotype.Component;

import com.fundoonotes.dto.LoginDto;
import com.fundoonotes.dto.ResetPasswordDto;
import com.fundoonotes.dto.UserDto;
import com.fundoonotes.model.UserModel;

@Component
public interface UserService {
	
	UserModel register(UserDto userdto);

	UserModel login(LoginDto logindto);

	UserModel verify(String token);

	UserModel forgetPassword(String email);
	
	UserModel resetPassword(ResetPasswordDto resetpassword, String token);

}