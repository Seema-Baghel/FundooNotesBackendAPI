package com.bridgelabz.fundoonotes.serviceImplementation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.ResetPasswordDto;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.exception.InvalidCredentialsException;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.exception.UserVerificationException;
import com.bridgelabz.fundoonotes.model.ProfilePicModel;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.ProfilePicRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.responses.EmailObject;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.responses.UserDetailsResponse;
import com.bridgelabz.fundoonotes.service.UserService;
import com.bridgelabz.fundoonotes.utility.Jwt;
import com.bridgelabz.fundoonotes.utility.RabbitMQSender;
import com.bridgelabz.fundoonotes.utility.RedisTempl;
import com.bridgelabz.fundoonotes.utility.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {

	 @Autowired
	 private BCryptPasswordEncoder bCryptPasswordEncoder;

	 @Autowired
	 private UserRepository repository;
	 
	 @Autowired
	 private ProfilePicRepository profilePicRepository;

	 @Autowired
	 private Jwt tokenGenerator;
	 
	 @Autowired
	 private RabbitMQSender rabbitMQSender;
	 
	 @Value("${aws.bucket.name}")
	 private String bucketName;

	 @Autowired
	 private AmazonS3 amazonS3Client;
	 
	 @Autowired
	 private RedisTempl<Object> redis;

	 private String redisKey = "Key";
	 
	 @Override
	 public ResponseEntity<Response> register(UserDto userdto) throws UserNotFoundException {
		 
		 UserModel emailavailable = repository.findEmail(userdto.getEmail());
		 if (emailavailable != null) {
			 return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "User already exist"));
		 }else {
			UserModel userDetails = new UserModel(userdto.getFirstName(), userdto.getLastName(), userdto.getEmail(), userdto.getMobile(), userdto.getPassword());
			userDetails.setCreatedAt(LocalDateTime.now());
			userDetails.setModifiedTime(LocalDateTime.now());
			userDetails.setVerified(false);
			userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
			repository.insertdata(userdto.getFirstName(), userdto.getLastName(),userdto.getEmail(),userdto.getMobile(), bCryptPasswordEncoder.encode(userdto.getPassword()), false, LocalDateTime.now(), LocalDateTime.now() );
			UserModel sendMail = repository.findEmail(userdto.getEmail());
			String response = Util.verify_Mail_Url + tokenGenerator.createToken(sendMail.getUserId());
			redis.putMap(redisKey, userDetails.getEmail(), userDetails.getFirstName());
			if(rabbitMQSender.send(new EmailObject(sendMail.getEmail(),"Registration Link...",response)))
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Registration Successful"));
		}
		throw new InvalidCredentialsException("Invalid Credentials", Util.USER_AUTHENTICATION_EXCEPTION_STATUS); 
   } 
		
	public ResponseEntity<UserDetailsResponse> login(LoginDto logindto) throws UserNotFoundException {
		 
		UserModel user = repository.findEmail(logindto.getEmail());
		if(user != null) {
			if (bCryptPasswordEncoder.matches(logindto.getPassword(),user.getPassword())) {
				if (user.isVerified()) {
					user.setUserStatus(true);
					repository.save(user);
					String token = tokenGenerator.createToken(user.getUserId());
					return ResponseEntity.status(HttpStatus.OK).body(new UserDetailsResponse(Util.OK_RESPONSE_CODE, "Login Successfull", token));
				}
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserDetailsResponse(Util.BAD_REQUEST_RESPONSE_CODE, "Login failed"));
		}
		throw new UserNotFoundException("User not found");
	 }

	@Override
	public ResponseEntity<Response> verify(String token) throws UserNotFoundException, UserVerificationException{
		long id = tokenGenerator.parseJwtToken(token);
		UserModel userInfo = repository.findById(id);
		if (id > 0 && userInfo != null) {
			if (!userInfo.isVerified()) {
				userInfo.setVerified(true);
				userInfo.setModifiedTime(LocalDateTime.now());
				repository.modifiedTime(userInfo.getUserId()); 
				repository.verify(userInfo.getUserId());
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Verified Successfully"));
			} 
			throw new UserVerificationException("User already verified!", Util.ALREADY_VERIFIED_EXCEPTION_STATUS);
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Not Verified"));
	}

	@Override
	public ResponseEntity<Response> forgetPassword(UserDto userdto) throws UserNotFoundException{
		
		UserModel isIdAvailable = repository.findEmail(userdto.getEmail());
		if (isIdAvailable != null && isIdAvailable.isVerified() == true) {
			String response = Util.resetpassword_url+ tokenGenerator.createToken(isIdAvailable.getUserId());
			if(rabbitMQSender.send(new EmailObject(isIdAvailable.getEmail(),"Registration Link...",response)))
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Password is send to the Email-Id"));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Sorry!! User Doesn't Exist"));
	}
	
	@Override
	public ResponseEntity<Response> resetPassword(ResetPasswordDto resetpassword, String token) throws UserNotFoundException {
		
		if (resetpassword.getPassword().equals(resetpassword.getConfirmpassword()))	{
			long id = tokenGenerator.parseJwtToken(token);
			UserModel isIdAvailable = repository.findById(id);
			if (isIdAvailable != null) {
				isIdAvailable.setPassword(bCryptPasswordEncoder.encode((resetpassword.getPassword())));
				repository.save(isIdAvailable);
				redis.putMap(redisKey, resetpassword.getPassword(),token);
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Password is Update Successfully"));
			}
			throw new UserNotFoundException("No data found");	
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Password and Confirm Password doesn't matched please enter again"));				
	}

	@Override
	public ResponseEntity<Response> logout(String token) throws UserNotFoundException {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = repository.findById(id);
		if (user == null) {
			throw new UserNotFoundException("No data found");
		} else if (user.isUserStatus()) {
			user.setUserStatus(false);
			repository.save(user);
			return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Logout Successfull"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Logout failed"));
	}

	@Override
	public boolean isSessionActive(String token) {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = repository.findById(id);
		return user.isUserStatus();	
	}

	@Override
	public ResponseEntity<Response> storeObjectInS3(MultipartFile file, String fileName, String contentType,
			String token) throws AmazonServiceException, AmazonClientException, IOException {
		System.out.println(contentType+" "+fileName);
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = repository.findById(userId);
		if (user != null) {
			ProfilePicModel profile = new ProfilePicModel(fileName, user);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(contentType);
			objectMetadata.setContentLength(file.getSize());
			System.out.println(bucketName+" "+fileName+" "+file.getInputStream()+" "+objectMetadata);
			amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);
			profilePicRepository.save(profile);
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Profile added successfully",Util.OK_RESPONSE_CODE, profile));
		}
	   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Something went Wrong "));
	}

	@Override
	public ResponseEntity<Response> updateProfilePic(MultipartFile file, String originalFile, String contentType, String token) throws AmazonServiceException, AmazonClientException, IOException {
		long userId=tokenGenerator.parseJwtToken(token);
		UserModel user=repository.findById(userId);
		System.out.println(user.getEmail());
		ProfilePicModel profile=profilePicRepository.findByUserId(userId);
		System.out.println(profile.getProfilePicName());
		if(user!=null&& profile!=null) {
			deleteProfilePic(profile.getProfilePicName());
			profilePicRepository.delete(profile);
			ProfilePicModel profilepic = new ProfilePicModel(originalFile, user);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(contentType);
			objectMetadata.setContentLength(file.getSize());
			System.out.println(bucketName+" "+originalFile+" "+file.getInputStream()+" "+objectMetadata);
			amazonS3Client.putObject(bucketName,originalFile,file.getInputStream(),objectMetadata);
			profilePicRepository.save(profilepic);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(Util.OK_RESPONSE_CODE, "Profile Pic update Sucessfully!!!"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Something went wrong!!!"));
	}

	@Override
	public void deleteProfilePic(String key) {
		try {
			amazonS3Client.deleteObject(bucketName, key);
		} catch (AmazonServiceException serviceException) {
			log.error(serviceException.getErrorMessage());
		} catch (AmazonClientException exception) {
			log.error("Something went wrong while deleting File.");
		}	
	}
	
	@Override
	public ResponseEntity<Response> getProfilePic(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = repository.findUserById(userId);
		if (user != null) {
			ProfilePicModel profile = profilePicRepository.findByUserId(userId);
			if (profile != null) 
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("All ProfilePic are",Util.OK_RESPONSE_CODE, profile.getProfilePicName()));
			}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE,"Something went wrong!!!"));
	}
	
}