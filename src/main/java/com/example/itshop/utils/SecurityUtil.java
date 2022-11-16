package com.example.itshop.utils;

import com.example.itshop.entities.Admin;
import com.example.itshop.entities.User;
import com.example.itshop.enums.ClientType;
import com.example.itshop.properties.SecurityProperty;
import com.example.itshop.repositories.AdminRepository;
import com.example.itshop.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
	private final SecurityProperty securityProperty;
	private final String CLIENT_TYPE_JWT_KEY = "CLIENT_TYPE";
	private final String CLIENT_ID_JWT_KEY = "CLIENT_ID";
	private final UserRepository userRepo;
	private final AdminRepository adminRepo;
	
	public String createAccessToken(long id, ClientType clientType) {
		return Jwts.builder()
			.claim(CLIENT_TYPE_JWT_KEY, clientType)
			.claim(CLIENT_ID_JWT_KEY, id)
			.signWith(getJwtKey(securityProperty.getAccessTokenSecret()))
			.setExpiration(Date.from(
				Instant.now().plus(securityProperty.getAccessTokenExpiresIn(), ChronoUnit.SECONDS))
			)
			.compact();
	}
	
	public void authenticate(String token, ClientType[] clientTypes) {
		Claims claims = this.parseAndValidateAccessToken(token);
		ClientType clientType = ClientType.valueOf(claims.get(CLIENT_TYPE_JWT_KEY).toString());
		Long userId = Long.valueOf(claims.get(CLIENT_ID_JWT_KEY).toString());
		UsernamePasswordAuthenticationToken authenticationToken;
		
		boolean hasAuthority = Arrays.asList(clientTypes).contains(clientType);
		if (!hasAuthority) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
		switch (clientType) {
			case USER -> {
				User user = userRepo.findById(userId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
				authenticationToken = new UsernamePasswordAuthenticationToken(user, token);
			}
			case ADMIN -> {
				Admin admin = adminRepo.findById(userId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin not found"));
				authenticationToken = new UsernamePasswordAuthenticationToken(admin, token);
			}
			default -> {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Role not found");
			}
		}
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}
	
	public Claims parseAndValidateAccessToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(this.getJwtKey(securityProperty.getAccessTokenSecret()))
			.build().parseClaimsJws(token).getBody();
	}
	
	public static User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static Admin getCurrentAdmin() {
		return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public Key getJwtKey(String secret) {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	
	
}
