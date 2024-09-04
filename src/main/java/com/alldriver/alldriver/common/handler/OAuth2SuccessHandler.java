package com.alldriver.alldriver.common.handler;




import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException{
        log.info("{} {} {} {}", authentication.getName(), authentication.getAuthorities().stream().map(Object::toString).toString(), authentication.getCredentials(), authentication.getDetails().toString());
        String name = authentication.getName();
        Map<String, String> res =new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        res.put("email", name);
        // 토큰 전달을 위한 redirect

        response.setStatus(HttpStatus.OK.value());
        response.getWriter().println(objectMapper.writeValueAsString(res));
    }
}
