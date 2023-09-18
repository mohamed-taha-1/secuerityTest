package com.confg;

import java.security.SecureRandom;
import java.util.Base64;

public class SecuerityConstants {
	public static final long EXPIRATION_TIME=864000000; // 10 days
	public static final String TOKEN_PREFIX="Bearer ";
	public static final String HEADER_STRING="Authorization";
	public static final String TOKEN_SECRET="jf9i4jgu83nfl0jf9i4jgu83nfl0jf9i4jgu83nfl0jf9i4jgu83nfl0jf9i4jgu83nfl0jf9i4jgu83nfl0";

	
	public static  String getToken() {
        // Generate a 256-bit (32-byte) random key
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);

        // Convert the key to a base64-encoded string
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        return base64Key;
    }
}
