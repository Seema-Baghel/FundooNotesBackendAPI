package com.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundoonotes.dto.LoginDto;
import com.fundoonotes.dto.ResetPasswordDto;
import com.fundoonotes.dto.UserDto;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.responses.Response;
import com.fundoonotes.service.UserService;
import com.fundoonotes.utility.Jwt;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userservice;

	@Autowired
	private Jwt tokenGenerator;
	
	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDto userdto) {		
	
		UserModel user = userservice.register(userdto);
		
		if (user != null) 
			return ResponseEntity.status(HttpStatus.OK)
								 .body(new Response("Registration Successful",200));
		return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
							 .body(new Response("User already exist", 400));
	}  
	
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto logindto) {
		
		UserModel userInformation = userservice.login(logindto);
		
		if (userInformation != null) 
			return ResponseEntity.status(HttpStatus.OK)
								 .body(new Response("Login Successfull", 200));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							 .body(new Response("Login failed", 400));
	}
	
	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) {
	    
		UserModel user = userservice.verify(token);
		
		if(user != null)
			return ResponseEntity.status(HttpStatus.OK)
								 .body(new Response("Verified Successfully",200));
		return ResponseEntity.status(HttpStatus.OK)
				             .body(new Response("Not Verified", 400));
	}
	
	@PutMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam("email") String email) {
		
		UserModel user = userservice.forgetPassword(email);
		
		if (user != null) 
			return ResponseEntity.status(HttpStatus.OK)
								 .body(new Response("Password is send to the Email-Id", 200));
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
							 .body(new Response("Sorry!! User Doesn't Exist", 400));
	}

	@PutMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPassword, @PathVariable("token") String token) {
		
		UserModel user = userservice.resetPassword(resetPassword, token);
		
		if(user != null)
			return ResponseEntity.status(HttpStatus.OK)
					             .body(new Response("Password is Update Successfully", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				             .body(new Response("Password and Confirm Password doesn't matched please enter again", 400));				
	}
}