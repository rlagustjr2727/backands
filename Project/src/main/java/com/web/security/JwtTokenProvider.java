package com.web.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	
	private final String JWT_SECRET = "secretKey";
	private final long JWT_EXPIRATION_MS = 604800000; // 7일
	
	public String generateToken(String username) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();
	}
	
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(JWT_SECRET)
				.parseClaimsJws(token)
				.getBody();
		
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parse(token);
			return true;
		} catch (Exception e) {
			// 로그 출력 또는 예외처리
		}
		return false;
	}
}
