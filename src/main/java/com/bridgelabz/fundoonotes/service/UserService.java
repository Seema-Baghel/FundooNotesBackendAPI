package com.bridgelabz.fundoonotes.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.ResetPasswordDto;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.exception.UserVerificationException;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.responses.UserDetailsResponse;

@Component
public interface UserService {
	
	ResponseEntity<Response> register(UserDto userdto) throws UserNotFoundException;

	ResponseEntity<UserDetailsResponse> login(LoginDto logindto) throws UserNotFoundException;

	ResponseEntity<Response> verify(String token) throws UserNotFoundException, UserVerificationException;

	ResponseEntity<Response> forgetPassword(String email) throws UserNotFoundException;
	
	ResponseEntity<Response> resetPassword(ResetPasswordDto resetpassword, String token) throws UserNotFoundException;

	ResponseEntity<Response> logout(String token) throws UserNotFoundException;

	boolean isSessionActive(String token);

}