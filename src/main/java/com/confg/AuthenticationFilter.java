package com.confg;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

private final AuthenticationManager authenticationManager;
    
   
 
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
        	
            LoginPayload creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginPayload.class);
            
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
            
        } catch ( java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException, java.io.IOException {
        
        String userName = ((User) auth.getPrincipal()).getUsername();  
        byte[] secretKeyByte=Base64.getEncoder().encode(SecuerityConstants.TOKEN_SECRET.getBytes());
        SecretKey secretKey=new SecretKeySpec(secretKeyByte, SignatureAlgorithm.HS512.getJcaName());
        Instant instant=Instant.now();
        
        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(Date.from (instant.plusMillis(SecuerityConstants.EXPIRATION_TIME)))
                .setIssuedAt(Date.from(instant)).signWith(secretKey,SignatureAlgorithm.HS512)
                .compact();
        
        
        res.addHeader(SecuerityConstants.HEADER_STRING, SecuerityConstants.TOKEN_PREFIX + token);
       
//     // Set the content type and status code
//        res.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
//        res.setStatus(HttpServletResponse.SC_OK);
//
//        String jwtToken =SecuerityConstants.TOKEN_PREFIX + token;
//                res.getWriter().write("{\"" +SecuerityConstants.HEADER_STRING+ ": \"" + jwtToken + "\"}");
        
        
    }  
	
	
	
}
