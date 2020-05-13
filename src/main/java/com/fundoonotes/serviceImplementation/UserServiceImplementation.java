package com.fundoonotes.serviceImplementation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fundoonotes.constants.Constants;
import com.fundoonotes.dto.LoginDto;
import com.fundoonotes.dto.ResetPasswordDto;
import com.fundoonotes.dto.UserDto;
import com.fundoonotes.exception.UserDetailsNullException;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.repository.UserRepository;
import com.fundoonotes.responses.EmailObject;
import com.fundoonotes.service.UserService;
import com.fundoonotes.utility.EmailVerification;
import com.fundoonotes.utility.Jwt;
import com.fundoonotes.utility.RabbitMQSender;
import com.fundoonotes.utility.RedisTempl;

@Service
public class UserServiceImplementation implements UserService {

	 @Autowired
	 private BCryptPasswordEncoder bCryptPasswordEncoder;

	 @Autowired
	 private UserRepository repository;

	 @Autowired
	 private EmailVerification mail;

	 @Autowired
	 private Jwt tokenGenerator;
	 
	 @Autowired
	 private RabbitMQSender rabbitMQSender;
	 
	 @Autowired
	 private RedisTempl<Object> redis;

	 private String redisKey = "Key";
	 
	 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
 	 String date = dateFormat.format(new Date()).toString();
	 
	 @Override
	 public UserModel register(UserDto userdto) throws UserDetailsNullException {
		 
		
		 UserModel emailavailable = repository.findEmail(userdto.getEmail());
		 if (emailavailable == null) {
			 
			UserModel userDetails = new UserModel(userdto.getFname(), userdto.getLname(), userdto.getEmail(), userdto.getPassword());
			userDetails.setCreatedAt(date);
			userDetails.setModifiedTime(date);
			userDetails.setVerified(false);
			userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
			
			repository.insertdata(userdto.getFname(), userdto.getLname(),userdto.getEmail(), bCryptPasswordEncoder.encode(userdto.getPassword()), false, date, date);

			UserModel sendMail = repository.findEmail(userdto.getEmail());
			String response = Constants.verify_Mail_Url + tokenGenerator.createToken(sendMail.getId());
			redis.putMap(redisKey, userDetails.getEmail(), userDetails.getFname());
			if(rabbitMQSender.send(new EmailObject(sendMail.getEmail(),"Registration Link...",response)))
				return userDetails;
		}
		throw new UserDetailsNullException("No Data found");	
   } 
		
	public UserModel login(LoginDto logindto) throws UserDetailsNullException {
		 
		UserModel usermodel = repository.findEmail(logindto.getEmail());
		if (bCryptPasswordEncoder.matches(logindto.getPassword(),usermodel.getPassword())) {
			
			String token = tokenGenerator.createToken(usermodel.getId());
			System.out.println("generated token : " + token);
			redis.putMap(redisKey, usermodel.getEmail(), token);
			return usermodel;
		}
		throw new UserDetailsNullException("Login failed");
	 }

	@Override
	public UserModel verify(String token) throws UserDetailsNullException{

		long id = tokenGenerator.parseJwtToken(token);
		UserModel userInfo = repository.findById(id);
		if (userInfo != null) {
			if (!userInfo.isVerified()) {
				userInfo.setVerified(true);
				userInfo.setModifiedTime(date);
				repository.modifiedTime(userInfo.getId()); 
				repository.verify(userInfo.getId());
				return userInfo;
			} else{
				return userInfo;
			}
		}
		throw new UserDetailsNullException("User not verified");
	}

	@Override
	public UserModel forgetPassword(String email) throws UserDetailsNullException{
		
		UserModel isIdAvailable = repository.findEmail(email);
		if (isIdAvailable != null && isIdAvailable.isVerified() == true) {
			String response = Constants.resetpassword_url+ tokenGenerator.createToken(isIdAvailable.getId());
			if(rabbitMQSender.send(new EmailObject(isIdAvailable.getEmail(),"Registration Link...",response)))
				return isIdAvailable;
		}
		throw new UserDetailsNullException("No data found");
	}
	
	@Override
	public UserModel resetPassword(ResetPasswordDto resetpassword, String token) throws UserDetailsNullException {
		
		if (resetpassword.getPassword().equals(resetpassword.getConfirmpassword()))	{
			long id = tokenGenerator.parseJwtToken(token);
			UserModel isIdAvailable = repository.findById(id);
			if (isIdAvailable != null) {
				isIdAvailable.setPassword(bCryptPasswordEncoder.encode((resetpassword.getPassword())));
				repository.save(isIdAvailable);
				redis.putMap(redisKey, resetpassword.getPassword(),token);
				return isIdAvailable;
			}else{
				throw new UserDetailsNullException("No data found");
			}
		}else{
			throw new UserDetailsNullException("No data found can't reset the password");
		}
	}
}
