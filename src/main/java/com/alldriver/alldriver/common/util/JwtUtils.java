package com.alldriver.alldriver.common.util;



import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.JwtException;
import com.alldriver.alldriver.common.token.dto.AuthToken;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private static String JWT_SECRET_KEY;

    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30L;

    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 30L;
    @Value("${spring.jwt.secret}")
    public void setKey(String key){
        JWT_SECRET_KEY = key;
    }

    public static String generateAccessToken(String userId, List<String> roles){

        // TODO : 나중에 통합
        if (roles.contains("ROLE_ADMIN")){
            return Jwts.builder()
                    .setSubject(userId)
                    .claim("roles", roles)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 9999999999L))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                    .compact();
        }
        else {
            return Jwts.builder()
                    .setSubject(userId)
                    .claim("roles", roles)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                    .compact();
        }
    }
    public static String generateRefreshToken(String userId){
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                .compact();
    }

    public static AuthToken generateToken(String userId, List<String> roles) {
        return AuthToken.builder()
                .grantType("Bearer")
                .accessToken(JwtUtils.generateAccessToken(userId, roles))
                .refreshToken(JwtUtils.generateRefreshToken(userId))
                .build();
    }



    public static boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token);
            return true;
        }
        catch (SecurityException e) {
            log.info("Invalid JWT Signature");
            throw new JwtException(ErrorCode.INVALID_AUTH_TOKEN);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            throw new JwtException(ErrorCode.INVALID_AUTH_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new JwtException(ErrorCode.AUTH_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            throw new JwtException(ErrorCode.INVALID_AUTH_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new JwtException(ErrorCode.INVALID_AUTH_TOKEN);
        }
    }
    public static Claims parseClaims(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public static String getUserId(){
        String accessToken = JwtUtils.getAccessToken();
        Claims body = JwtUtils.parseClaims(accessToken);
        String userId = body.get("sub", String.class);
        log.info("user id = {}", userId);
        return userId;
    }
    public static String getAccessToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        else
        {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        return token;
    }

    public static String getUserIdFromRefreshToken(String refreshToken){
        Claims body = JwtUtils.parseClaims(refreshToken);
        String userId = body.get("sub", String.class);

        return userId;
    }
}
