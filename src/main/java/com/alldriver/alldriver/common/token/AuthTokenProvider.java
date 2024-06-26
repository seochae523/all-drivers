package com.alldriver.alldriver.common.token;



import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.user.service.CustomUserDetailService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor

public class AuthTokenProvider {
    @Value("${spring.jwt.secret}")
    private String key;
    @Value("${spring.jwt.access-token-expiration-time}")
    private Long authTokenExpirationTime;
    @Value("${spring.jwt.refresh-token-expiration-time}")
    private Long refreshTokenExpirationTime;
    private final CustomUserDetailService customUserDetailService;

    @PostConstruct
    protected void init(){
        this.key = Base64.getEncoder().encodeToString(this.key.getBytes());
    }



    public AuthToken generateToken(String userId, List<String> roles) {
        String accessToken = Jwts.builder()
                .setSubject(userId)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + authTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        return AuthToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        String studentId = claims.getSubject();

        UserDetails userDetails = customUserDetailService.loadUserByUsername(studentId);

        UserDetails principal = new User(claims.getSubject() , "", userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(principal, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch (Exception e) {
            log.error("Invalid JWT Signature");
            throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }
    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }
        catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
}
