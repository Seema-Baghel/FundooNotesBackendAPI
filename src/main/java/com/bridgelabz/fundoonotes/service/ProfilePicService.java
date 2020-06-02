package com.bridgelabz.fundoonotes.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.bridgelabz.fundoonotes.responses.Response;

@Component
public interface ProfilePicService {

	ResponseEntity<Response> storeObjectInS3(MultipartFile file, String originalFilename, String contentType, String token) throws AmazonServiceException, AmazonClientException, IOException; 

	void deleteProfilePic(String token);

	ResponseEntity<Response> getProfilePic(MultipartFile file, String token);

	ResponseEntity<Response> updateProfilePic(MultipartFile file, String originalFile, String contentType, String token) throws AmazonServiceException, AmazonClientException, IOException;
}
