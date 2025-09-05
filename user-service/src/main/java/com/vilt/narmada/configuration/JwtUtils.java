package com.vilt.narmada.configuration;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

	private static final SecretKey SECRET_KEY = Keys
			.hmacShaKeyFor("ThisIsA256BitSecretKeyForHS256Algorithm123!".getBytes(StandardCharsets.UTF_8));

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(SECRET_KEY) // your key here (should be a Key, not raw string for best
																// practice)
				.build().parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority) // Extract
																										// "ROLE_ADMIN"
				.toList();
		return Jwts.builder().setSubject(userDetails.getUsername()).claim("role", roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
				.signWith(SECRET_KEY, SignatureAlgorithm.HS256) // secure signing
				.compact();
	}

	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

	public String extractToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
}
