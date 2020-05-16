package com.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundoonotes.dto.LoginDto;
import com.fundoonotes.dto.ResetPasswordDto;
import com.fundoonotes.dto.UserDto;
import com.fundoonotes.exception.UserDetailsNullException;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.responses.Response;
import com.fundoonotes.responses.UserDetailsResponse;
import com.fundoonotes.service.UserService;
import com.fundoonotes.utility.Jwt;

@RestController
@RequestMapping("/user")
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
	public ResponseEntity<Response> register(@RequestBody UserDto userdto) throws UserDetailsNullException {		
	
		UserModel user = userservice.register(userdto);
		
		if (user != null) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Registration Successful"));
		return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new Response(400, "User already exist"));
	}  
	
	/**
	 * API for user login
	 * 
	 * @param loginDetails 
	 * @return response
	 */
	
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto logindto) throws UserDetailsNullException {
		
		UserModel userInformation = userservice.login(logindto);
		if (userInformation != null) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Login Successfull"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Login failed"));
	}
	
	/**
	 * API for user verification
	 * 
	 * @param token
	 * @return response Entity
	 */
	
	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) throws UserDetailsNullException {
	    
		UserModel user = userservice.verify(token);
		
		if(user != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Verified Successfully"));
		return ResponseEntity.status(HttpStatus.OK).body(new Response(400, "Not Verified"));
	}
	
	/**
	 * API for forget password
	 * 
	 * @param email
	 * @return response
	 */
	
	@PutMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam("email") String email) throws UserDetailsNullException {
		
		UserModel user = userservice.forgetPassword(email);
		
		if (user != null) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Password is send to the Email-Id"));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(400, "Sorry!! User Doesn't Exist"));
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
	public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPassword, @RequestHeader("token") String token) throws UserDetailsNullException {
		
		UserModel user = userservice.resetPassword(resetPassword, token);
		
		if(user != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Password is Update Successfully"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Password and Confirm Password doesn't matched please enter again"));				
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Response> logout(@RequestHeader("token") String token) throws UserDetailsNullException {
		
		boolean userInformation = userservice.logout(token);
		if (userInformation) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Logout Successfull"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Logout failed"));
	}

}