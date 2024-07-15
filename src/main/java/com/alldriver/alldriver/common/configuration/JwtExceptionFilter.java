package com.alldriver.alldriver.common.configuration;

import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }
        catch (JwtException ex){
            ErrorCode errorCode = ex.getErrorCode();
            if(errorCode.equals(ErrorCode.INVALID_AUTH_TOKEN)){
                setResponse(response, ErrorCode.INVALID_AUTH_TOKEN);
            }
            else if(errorCode.equals(ErrorCode.AUTH_TOKEN_EXPIRED)){
                setResponse(response, ErrorCode.AUTH_TOKEN_EXPIRED);
            }
            else if(errorCode.equals(ErrorCode.INCORRECT_REFRESH_TOKEN)){
                setResponse(response, ErrorCode.INCORRECT_REFRESH_TOKEN);
            }
            else if(errorCode.equals(ErrorCode.INVALID_REFRESH_TOKEN)){
                setResponse(response, ErrorCode.INVALID_REFRESH_TOKEN);
            }
            else{
                setResponse(response, ErrorCode.REFRESH_TOKEN_NOT_FOUND);
            }
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws RuntimeException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getStatus().value());
        response.getWriter().print(errorCode.getMessage());
    }
}
