package com.sivvg.tradingservices.util;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${app.jwt.secret:897b592bf60a58263a66a2540b04b8d24a6c062cf5e12ac204e91c5064676b7e38d8a123ec4b3080}")
	private String jwtSecret;

	@Value("${app.jwt.expirationMs:86400000}")
	private long jwtExpirationMs;

	// ==================================================
	// 🔹 GENERATE TOKEN
	// ==================================================
	public String generateToken(String userId, String role) {

		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtExpirationMs);

		if (!role.startsWith("ROLE_")) {
			role = "ROLE_" + role;
		}

		log.debug("Generating JWT token | userId={} | role={}", userId, role);

		return Jwts.builder().setSubject(userId).claim("role", role).setIssuedAt(now).setExpiration(expiry)
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	// ==================================================
	// 🔹 EXTRACT USER ID
	// ==================================================
	public String extractUserId(String token) {

		Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	// ==================================================
	// 🔹 EXTRACT ROLE
	// ==================================================
	public String extractRole(String token) {

		Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

		return claims.get("role", String.class);
	}

	// ==================================================
	// 🔹 EXTRACT EXPIRATION
	// ==================================================
	public Date extractExpiration(String token) {

		Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

		return claims.getExpiration();
	}

	// ==================================================
	// 🔹 CHECK TOKEN EXPIRY
	// ==================================================
	public boolean isTokenExpired(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

			return claims.getExpiration().before(new Date());

		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			log.warn("JWT token expired");
			return true;
		}
	}

	// ==================================================
	// 🔹 VALIDATE TOKEN
	// ==================================================
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);

			return true;

		} catch (JwtException | IllegalArgumentException e) {
			log.warn("Invalid JWT token");
			return false;
		}
	}

	// ==================================================
	// 🔹 SIGNING KEY
	// ==================================================
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
