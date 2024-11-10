package com.prj.sec.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.prj.sec.common.exception.JwtExceptionType;
import com.prj.sec.common.service.UserSecurityService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value(("${jwt.validityInMilliseconds}"))
    private Long validityInMilliseconds;
    @Value("${jwt.secretKey}")
    private String secretKey;

    private  Algorithm algorithm;
    private final UserSecurityService userSecurityService;

    public String createToken(String email, Collection<? extends GrantedAuthority> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Key key = getKeyFromBase64EncodedSecretKey(encodeBase64SecretKey(secretKey));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userSecurityService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

//    public boolean validateToken(String token) {
//        try {
//            parseClaims(token);
//        } catch (ExpiredJwtException e) {
//            throw new JwtException(JwtExceptionType.EXPIRED_TOKEN.toString());
//        } catch (UnsupportedJwtException e) {
//            throw new JwtException(JwtExceptionType.UNSUPPORTED_TOKEN.toString());
//        } catch (MalformedJwtException e) {
//            throw new JwtException(JwtExceptionType.MALFORMED_TOKEN.toString());
//        } catch (SignatureException e) {
//            throw new JwtException(JwtExceptionType.INVALID_SIGNATURE.toString());
//        } catch (IllegalArgumentException e) {
//            throw new JwtException(JwtExceptionType.INVALID_TOKEN.toString());
//        }
//        return false;
//    }

    private Claims parseClaims(String token) {
        Key key = getKeyFromBase64EncodedSecretKey(encodeBase64SecretKey(secretKey));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes());
    }

    private Key getKeyFromBase64EncodedSecretKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
