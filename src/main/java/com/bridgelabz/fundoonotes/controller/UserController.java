package com.bridgelabz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.ResetPasswordDto;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.responses.UserDetailsResponse;
import com.bridgelabz.fundoonotes.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	/**
	 * API for user registration
	 * 
	 * @param userDto
	 * @return response
	 */
	
	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDto userdto) throws UserNotFoundException {		
	
		return userservice.register(userdto);
	}  
	
	/**
	 * API for user login
	 * 
	 * @param loginDetails 
	 * @return response
	 */
	
	@PostMapping("/login")
	public ResponseEntity<UserDetailsResponse> login(@RequestBody LoginDto logindto) throws UserNotFoundException {
		
		return userservice.login(logindto);
	}
	
	/**
	 * API for user verification
	 * 
	 * @param token
	 * @return response Entity
	 */
	
	@GetMapping("/verify")
	public ResponseEntity<Response> userVerification(@RequestHeader("token") String token) throws UserNotFoundException {
	    
		return userservice.verify(token);
	}
	
	/**
	 * API for forget password
	 * 
	 * @param email
	 * @return response
	 */
	
	@PutMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam("email") String email) throws UserNotFoundException {
		
		return userservice.forgetPassword(email);
	}

	/**
	 * API for reset password
	 * 
	 * @param token
	 * @param pwd
	 * @return response
	 * @throws Exception
	 */
	
	@PutMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPassword, @RequestHeader("token") String token) throws UserNotFoundException {
		
		return userservice.resetPassword(resetPassword, token);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Response> logout(@RequestHeader("token") String token) throws UserNotFoundException {
		
		return userservice.logout(token);
	}

}