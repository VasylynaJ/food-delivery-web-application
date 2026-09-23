package com.vasylyna.fooddelivery.auth;

import com.vasylyna.fooddelivery.user.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey key; private final long expirationMs;
    public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expirationMs){
        byte[] bytes=java.util.Base64.getDecoder().decode(secret);
        if(bytes.length<32) throw new IllegalArgumentException("JWT_SECRET must be base64 encoding of at least 32 random bytes");
        this.key=Keys.hmacShaKeyFor(bytes);this.expirationMs=expirationMs;
    }
    public String createToken(AppUser user){Date now=new Date();return Jwts.builder().subject(user.getEmail()).claim("role",user.getRole().name()).issuedAt(now).expiration(new Date(now.getTime()+expirationMs)).signWith(key).compact();}
    public String subject(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
}
