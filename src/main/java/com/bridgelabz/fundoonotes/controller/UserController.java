package com.bridgelabz.fundoonotes.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
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
	 * This function takes all user info input as input parameter and checks the
	 * user validity@see {@link UserService} and accordingly returns the response.
	 * 
	 * @param UserDTO as DTO class input parameter
	 * @return ResponseEntity<Response>
	 */
	
	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDto userdto) throws UserNotFoundException {		
	
		return userservice.register(userdto);
	}  
	
	/**
	 * This function takes LongIn information data given by user on the basis of
	 * user input information it fetch the complete user checks for conditions
	 * whether the user is verified or not and also checks whether he is registered
	 * or not and works accordingly.
	 * 
	 * @param loginInformation as {@link LoginDTO}
	 * @return ResponseEntity<Response>
	 */
	
	@PostMapping("/login")
	public ResponseEntity<UserDetailsResponse> login(@RequestBody LoginDto logindto) throws UserNotFoundException {
		
		return userservice.login(logindto);
	}
	
	/**
	 * This function takes generated token of the user as String input as input
	 * parameter and checks the user verification @see {@link UserService} and
	 * accordingly returns the response.
	 * 
	 * @param UserDTO as DTO class input parameter
	 * @return ResponseEntity<Response>
	 */
	
	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) throws UserNotFoundException {
	    
		return userservice.verify(token);
	}
	
	/**
	 * This function takes email id as string input parameter and checks the
	 * Existence of user from service class on validate credentials, it allows the
	 * user to reset his password by his mail. Or else, it sends the mail to user
	 * for verification of his account
	 * 
	 * @param emailId as String input parameter
	 * @return ResponseEntity<Response>
	 */
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody UserDto email) throws UserNotFoundException {
		
		return userservice.forgetPassword(email);
	}

	/**
	 * This function takes update password credentials input parameter along with
	 * valid token as String input parameter after verifying the credentials and
	 * update the password and after successful update it displays corresponding
	 * message.
	 * 
	 * @param token as String Input parameter
	 * @param resetPassword as {@link ResetPassword} class
	 * @return ResponseEntity<Response>
	 */
	
	@PutMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPassword, @PathVariable("token") String token) throws UserNotFoundException {
		
		return userservice.resetPassword(resetPassword, token);
	}
	
	/**
	 * This function takes valid token as String input parameter after verifying 
	 * it logout successfully it displays corresponding
	 * message.
	 * 
	 * @param token as String input parameter
	 * @return ResponseEntity<Response>
	 */
	
	@PostMapping("/logout")
	public ResponseEntity<Response> logout(@RequestHeader("token") String token) throws UserNotFoundException {
		
		return userservice.logout(token);
	}
	
	/*
	 * API to upload profilepic
	 */
	
	@PostMapping("/uploadprofilepic")
	public ResponseEntity<Response> addProfilePic(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token) throws AmazonServiceException, AmazonClientException, IOException{

		return userservice.storeObjectInS3(file, file.getOriginalFilename(), file.getContentType(),token);	
	}
	
	/*
	 * API to update profilepic
	 */
	
	@PutMapping("/updateProfilePic")
	public ResponseEntity<Response> updateProfilePic(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token) throws AmazonServiceException, AmazonClientException, IOException {
		
		return  userservice.updateProfilePic(file,file.getOriginalFilename(),file.getContentType(), token);	
	}
	
	/*
	 * API to get profilepic
	 */

	@GetMapping("/getProfilePic")
	public ResponseEntity<Response> getProfilePic(@RequestHeader("token")String token){
		
		return userservice.getProfilePic(token);
		
	}
}