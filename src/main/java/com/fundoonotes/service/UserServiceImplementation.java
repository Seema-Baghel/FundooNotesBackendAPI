package com.fundoonotes.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fundoonotes.dto.LoginDto;
import com.fundoonotes.dto.ResetPasswordDto;
import com.fundoonotes.dto.UserDto;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.repository.UserRepository;
import com.fundoonotes.utility.EmailVerification;
import com.fundoonotes.utility.Jwt;

import ch.qos.logback.classic.pattern.Util;

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

	 @Override
	 public UserModel register(UserDto userdto) {
		 
		 Date date = new Date();
		 UserModel emailavailable = repository.findEmail(userdto.getEmail());
		 if (emailavailable == null) {
			 
			UserModel userDetails = new UserModel(userdto.getFname(), userdto.getLname(), userdto.getEmail(), userdto.getPassword());
			userDetails.setCreatedAt(date);
			userDetails.setModifiedTime(date);
			userDetails.setVerified(false);
			userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
			
			repository.insertdata(userdto.getFname(), userdto.getLname(),userdto.getEmail(), bCryptPasswordEncoder.encode(userdto.getPassword()), false, date, date);

			UserModel sendMail = repository.findEmail(userdto.getEmail());
			String response = "http://localhost:8080/user/verify/" + tokenGenerator.createToken(sendMail.getId());
			mail.sendVerifyMail(sendMail.getEmail(), response);
			return userDetails;
		} else {
			return null;
		}
   } 
		
	public UserModel login(LoginDto logindto) {
		 
		UserModel usermodel;
		usermodel = repository.findEmail(logindto.getEmail());
		if (bCryptPasswordEncoder.matches(logindto.getPassword(),usermodel.getPassword())) 
			return usermodel;
		return null;
	 }

	@Override
	public UserModel verify(String token){

		long id = tokenGenerator.parseJwtToken(token);
		UserModel userInfo = repository.findById(id);
		if (userInfo != null) {
			if (!userInfo.isVerified()) {
				userInfo.setVerified(true);
				repository.verify(userInfo.getId());
				return userInfo;
			} else{
				return userInfo;
			}
		}
		return null;
	}

	@Override
	public UserModel forgetPassword(String email){
		
		UserModel isIdAvailable = repository.findEmail(email);
		if (isIdAvailable != null && isIdAvailable.isVerified() == true) {
			String response = "http://localhost:8080/user/resetpassword/"+ tokenGenerator.createToken(isIdAvailable.getId());
			mail.sendForgetPasswordMail(isIdAvailable.getEmail(), response);
			return isIdAvailable;
		}
		return null;
	}
	
	@Override
	public UserModel resetPassword(ResetPasswordDto resetpassword, String token) {
		
		if (resetpassword.getPassword().equals(resetpassword.getConfirmpassword()))	{
			long id = tokenGenerator.parseJwtToken(token);
			UserModel isIdAvailable = repository.findById(id);
			if (isIdAvailable != null) {
				isIdAvailable.setPassword(bCryptPasswordEncoder.encode((resetpassword.getPassword())));
				repository.save(isIdAvailable);
				return isIdAvailable;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
}
