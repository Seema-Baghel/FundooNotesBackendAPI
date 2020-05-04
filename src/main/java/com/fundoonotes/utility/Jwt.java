package com.fundoonotes.utility;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

@Service
public class Jwt{
	
	private static final String SECRET_KEY = "SB45BU5";
	
	public String createToken(long l)
	{
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
		return JWT.create()
				.withClaim("email",l)
				.sign(algorithm);
	}
	
	public long parseJwtToken(String token)
	{
		Claim claim = JWT.require(Algorithm.HMAC256(SECRET_KEY))
				.build()
				.verify(token)
				.getClaim("email");
		return claim.asLong();
	}
	
}