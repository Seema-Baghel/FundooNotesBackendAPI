package com.fundoonotes.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailVerification {

	@Autowired
	private JavaMailSender mailsender;

	public void sendVerifyMail(String email, String token) throws MailException {
			
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("seemabaghel1997@gmail.com");
		mail.setTo(email);
		mail.setSubject("verify user");
		mail.setText("click here..." + token);
		mailsender.send(mail);
	}

	public void sendForgetPasswordMail(String email, String token) throws MailException {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("seemabaghel1997@gmail.com");
		mail.setTo(email);
		mail.setSubject("Forget password link");
		mail.setText("click here..." + token);
		mailsender.send(mail);
	}
}
