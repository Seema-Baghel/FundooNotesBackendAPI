package com.bridgelabz.fundoonotes.utility;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SuppressWarnings("serial")
@Service
public class Jwt implements Serializable{
	
	public String createToken(long id){
		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Util.SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setId(String.valueOf(id)).setIssuedAt(now).setSubject("JWT Token")
		.setIssuer(String.valueOf(id)).signWith(signatureAlgorithm, signingKey);

		builder.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5));

		return builder.compact();
	}
	
	public long parseJwtToken(String token)
	{
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter
				.parseBase64Binary(Util.SECRET_KEY))
				.parseClaimsJws(token)
				.getBody();
				claims.getExpiration();
				return Long.parseLong(claims.getIssuer());
	}
	
}