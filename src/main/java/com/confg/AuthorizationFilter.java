package com.confg;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.Header;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		String header = req.getHeader(SecuerityConstants.HEADER_STRING);

		if (header == null || !header.startsWith(SecuerityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String autherizationHeader = request.getHeader(SecuerityConstants.HEADER_STRING);
		if (autherizationHeader == null)
			return null;

		String token = autherizationHeader.replace(SecuerityConstants.TOKEN_PREFIX, "");
		byte[] secretKeyByte = Base64.getEncoder().encode(SecuerityConstants.TOKEN_SECRET.getBytes());
		SecretKey secretKey = new SecretKeySpec(secretKeyByte, SignatureAlgorithm.HS512.getJcaName());

		JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
		String jwt = parser.parse(token).getBody().toString();
		if (jwt == null)
			return null;

		return new UsernamePasswordAuthenticationToken(jwt, null, new ArrayList<>());
	}

}
