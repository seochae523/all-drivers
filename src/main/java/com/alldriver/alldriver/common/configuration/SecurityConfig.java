package com.alldriver.alldriver.common.configuration;

import com.alldriver.alldriver.common.handler.CustomAccessDenyHandler;
import com.alldriver.alldriver.common.handler.OAuth2SuccessHandler;
import com.alldriver.alldriver.user.service.CustomUserDetailService;
import com.alldriver.alldriver.user.service.impl.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final CustomAccessDenyHandler customAccessDenyHandler;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuthService oAuthService;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity

                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDenyHandler))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/apply/**").hasAnyRole("JOB_SEEKER", "ADMIN")
                        .requestMatchers("/recruit/**").hasAnyRole("RECRUITER", "ADMIN")
                        .requestMatchers( "/find-nickname", "/login", "/sign-up/**", "/swagger-ui/**", "/v3/api-docs/**", "/check/**",
                                         "/change-forget-password", "/refresh","/ws/chat", "/sms/**" , "/verify/**", "/actuator/**", "/no-auth").permitAll())
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(
                                c -> c.userService(oAuthService))
                                .successHandler(oAuth2SuccessHandler)
                                .loginPage("/no-auth"))
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(customUserDetailService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtFilter.class);
        return httpSecurity.build();
    }

}
