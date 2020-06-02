package com.bridgelabz.fundoonotes.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.ProfilePicService;

@RestController
@RequestMapping("/profilePic")
public class ProfilePicController {

	@Autowired
	private ProfilePicService profilePicService;

	/*
	 * API to upload profilepic
	 */
	@PostMapping("/uploadprofilepic")
	public ResponseEntity<Response> addProfilePic(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token) throws AmazonServiceException, AmazonClientException, IOException{

		return profilePicService.storeObjectInS3(file, file.getOriginalFilename(), file.getContentType(),token);	
	}

	/*
	 * API to update profilepic
	 */
	@PutMapping("/updateProfilePic")
	public ResponseEntity<Response> updateProfilePic(@RequestParam("file") MultipartFile file, @RequestHeader("token") String token) throws AmazonServiceException, AmazonClientException, IOException {
		
		return  profilePicService.updateProfilePic(file,file.getOriginalFilename(),file.getContentType(), token);
		
	}
	
	/*
	 * API to get profilepic
	 */
	@GetMapping("/getProfilePic")
	public ResponseEntity<Response> getProfilePic(@RequestParam("file") MultipartFile file,@RequestHeader("token")String token){
		
		return profilePicService.getProfilePic(file,token);
		
	}
}
