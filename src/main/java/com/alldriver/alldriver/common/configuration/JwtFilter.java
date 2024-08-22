package com.alldriver.alldriver.common.configuration;


import com.alldriver.alldriver.common.util.JwtUtils;

import com.alldriver.alldriver.user.service.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);


            if(token != null && JwtUtils.validateToken(token)){
                Authentication authentication = this.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else{

            }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String[] excludePath = {"/login", "/sign-up/**", "/check/**", "/refresh",
                "/v3/api-docs/**", "/swagger-ui/**", "/change-forget-password", "/ws/chat",
                "/verify/**", "/actuator/**"

        };
        // jwt 인증 미 실시 토큰

        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    private Authentication getAuthentication(String accessToken) {
        // 여긴 security에 권한 넣어주는 곳임
        Claims claims = JwtUtils.parseClaims(accessToken);
        String userId = claims.getSubject();

        UserDetails userDetails = customUserDetailService.loadUserByUsername(userId);
        String findUserId = userDetails.getUsername();
        UserDetails principal = new User(findUserId , "", userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(principal, "", userDetails.getAuthorities());
    }
}
