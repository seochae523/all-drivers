package com.alldriver.alldriver.common.configuration;

import com.alldriver.alldriver.user.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.alldriver.alldriver.common.util.JwtUtils;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/car-owner/**").hasAnyRole("CAR_OWNER", "ADMIN")
                        .requestMatchers("/owner/**").hasAnyRole("OWNER", "ADMIN")

                        .requestMatchers( "/find-nickname", "/login", "/sign-up/**", "/swagger-ui/**", "/v3/api-docs/**", "/check/**",
                                         "/change-forget-password", "/refresh","/ws/chat", "/sms/**" , "/verify/**").permitAll()
                 )

                .addFilterBefore(new JwtFilter(customUserDetailService), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
