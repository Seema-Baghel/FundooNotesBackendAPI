package com.bridgelabz.fundoonotes.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
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

	ResponseEntity<Response> forgetPassword(UserDto email) throws UserNotFoundException;
	
	ResponseEntity<Response> resetPassword(ResetPasswordDto resetpassword, String token) throws UserNotFoundException;

	ResponseEntity<Response> logout(String token) throws UserNotFoundException;
	
	ResponseEntity<Response> storeObjectInS3(MultipartFile file, String originalFilename, String contentType, String token) throws AmazonServiceException, AmazonClientException, IOException; 

	ResponseEntity<Response> updateProfilePic(MultipartFile file, String originalFilename, String contentType, String token) throws AmazonServiceException, AmazonClientException, IOException;

	void deleteProfilePic(String token);
	
//	void deleteProfileImg(String token);
	
	boolean isSessionActive(String token);

//	ResponseEntity<Response> getProfilePic(MultipartFile file, String token);
	
	ResponseEntity<Response> getProfilePic(String token);

	
}