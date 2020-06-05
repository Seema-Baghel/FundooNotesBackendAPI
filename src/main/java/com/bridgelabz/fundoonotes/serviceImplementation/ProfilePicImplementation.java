//package com.bridgelabz.fundoonotes.serviceImplementation;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.amazonaws.AmazonClientException;
//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.S3Object;
//
//import com.bridgelabz.fundoonotes.model.ProfilePicModel;
//import com.bridgelabz.fundoonotes.model.UserModel;
//import com.bridgelabz.fundoonotes.repository.ProfilePicRepository;
//import com.bridgelabz.fundoonotes.repository.UserRepository;
//import com.bridgelabz.fundoonotes.responses.Response;
//import com.bridgelabz.fundoonotes.service.ProfilePicService;
//import com.bridgelabz.fundoonotes.utility.Jwt;
//import com.bridgelabz.fundoonotes.utility.Util;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class ProfilePicImplementation implements ProfilePicService {
//	
//	@Autowired
//	private ProfilePicRepository profilePicRepository;
//
//	@Autowired
//	private Jwt jwtGenerator;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Value("${aws.bucket.name}")
//	private String bucketName;
//
//	@Autowired
//	private AmazonS3 amazonS3Client;
//
//	@Override
//	public ResponseEntity<Response> storeObjectInS3(MultipartFile file, String fileName, String contentType, String token) throws AmazonServiceException, AmazonClientException, IOException {
//		
//			System.out.println(contentType+" "+fileName);
//			long userId = jwtGenerator.parseJwtToken(token);
//			UserModel user = userRepository.findById(userId);
//			if (user != null) {
//				ProfilePicModel profile = new ProfilePicModel(fileName, user);
//				ObjectMetadata objectMetadata = new ObjectMetadata();
//				objectMetadata.setContentType(contentType);
//				objectMetadata.setContentLength(file.getSize());
//				System.out.println(bucketName+" "+fileName+" "+file.getInputStream()+" "+objectMetadata);
//				amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);
//				profilePicRepository.save(profile);
//				return ResponseEntity.status(HttpStatus.OK).body(new Response("Profile added successfully",Util.OK_RESPONSE_CODE, profile));
//			}
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Something went Wrong "));
//	}
//
//	@Override
//	public void deleteProfilePic(String key) {
//		try {
//			amazonS3Client.deleteObject(bucketName, key);
//		} catch (AmazonServiceException serviceException) {
//			log.error(serviceException.getErrorMessage());
//		} catch (AmazonClientException exception) {
//			log.error("Something went wrong while deleting File.");
//		}	
//	}
//
//	@Override
//	public ResponseEntity<Response> getProfilePic(MultipartFile file, String token) {
//		long userId = jwtGenerator.parseJwtToken(token);
//		Optional<UserModel> user = userRepository.findUserById(userId);
//		if (user != null) {
//			ProfilePicModel profile = profilePicRepository.findByUserId(userId);
//			if (profile != null) 
//				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("All ProfilePic are",Util.OK_RESPONSE_CODE, fetchObject(profile.getProfilePicName())));
//			}
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE,"Something went wrong!!!"));
//	}
//	
//	public S3Object fetchObject(String awsFileName) {
//
//		S3Object s3Object;
//		try {
//			s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, awsFileName));
//		} catch (AmazonServiceException serviceException) {
//			throw new RuntimeException("Error while streaming File.");
//		} catch (AmazonClientException exception) {
//			throw new RuntimeException("Error while streaming File.");
//		}
//		return s3Object;
//	}
//
//	@Override
//	public ResponseEntity<Response> updateProfilePic(MultipartFile file, String originalFile, String contentType, String token) throws AmazonServiceException, AmazonClientException, IOException {
//			long userId=jwtGenerator.parseJwtToken(token);
//			UserModel user=userRepository.findById(userId);
//			System.out.println(user.getEmail());
//			ProfilePicModel profile=profilePicRepository.findByUserId(userId);
//			System.out.println(profile.getProfilePicName());
//			if(user!=null&& profile!=null) {
//				deleteProfilePic(profile.getProfilePicName());
//				profilePicRepository.delete(profile);
//				ObjectMetadata objectMetadata = new ObjectMetadata();
//				objectMetadata.setContentType(contentType);
//				objectMetadata.setContentLength(file.getSize());
//				amazonS3Client.putObject(bucketName,originalFile,file.getInputStream(),objectMetadata);
//				profilePicRepository.save(profile);
//				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(Util.OK_RESPONSE_CODE, "Profile Pic update Sucessfully!!!"));
//			}
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Something went wrong!!!"));
//	}
//
//}
