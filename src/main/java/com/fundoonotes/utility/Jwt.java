package com.fundoonotes.utility;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.fundoonotes.constants.Constants;

@Service
public class Jwt{
	
	public String createToken(long l)
	{
		Algorithm algorithm = Algorithm.HMAC256(Constants.SECRET_KEY);
		return JWT.create()
				.withClaim("email",l)
				.sign(algorithm);
	}
	
	public long parseJwtToken(String token)
	{
		Claim claim = JWT.require(Algorithm.HMAC256(Constants.SECRET_KEY))
				.build()
				.verify(token)
				.getClaim("email");
		return claim.asLong();
	}
	
}