package com.alldriver.alldriver.common.token.filter;


import com.alldriver.alldriver.common.token.AuthTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);


            if(token != null && tokenProvider.validateToken(token)){
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String[] excludePath = {"/login", "/signup",
                "/refresh", "/v3/api-docs/**",
                "/swagger-ui/**",
                "/check-student-id", "/check-nickname", "/change-forget-password", "/find-nickname",
                "/check-license",
                "/ws/chat"
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
}
