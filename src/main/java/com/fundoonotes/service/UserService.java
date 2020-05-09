package com.fundoonotes.service;

import org.springframework.stereotype.Component;

import com.fundoonotes.dto.LoginDto;
import com.fundoonotes.dto.ResetPasswordDto;
import com.fundoonotes.dto.UserDto;
import com.fundoonotes.exception.UserDetailsNullException;
import com.fundoonotes.model.UserModel;

@Component
public interface UserService {
	
	UserModel register(UserDto userdto) throws UserDetailsNullException;

	UserModel login(LoginDto logindto) throws UserDetailsNullException;

	UserModel verify(String token) throws UserDetailsNullException;

	UserModel forgetPassword(String email) throws UserDetailsNullException;
	
	UserModel resetPassword(ResetPasswordDto resetpassword, String token) throws UserDetailsNullException;

}