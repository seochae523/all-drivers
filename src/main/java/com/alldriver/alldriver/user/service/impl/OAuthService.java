package com.alldriver.alldriver.user.service.impl;



import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.dto.response.oauth.*;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OAuthService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 해당 메서드에서 소셜 로그인 수행합니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Response oAuth2Response;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes= oAuth2User.getAttributes();
        log.info("==>> {}", registrationId);

        switch (registrationId){
            case "apple":
                String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
                attributes = decodeJwtTokenPayload(idToken);
                oAuth2Response = new AppleResponse(attributes);
                log.info("auth = > {}", oAuth2Response.getEmail());
                break;

            case "kakao":
                oAuth2Response = new KakaoResponse(attributes);
                break;

            case "google":
                oAuth2Response = new GoogleResponse(attributes);
                break;

            case "naver":
                oAuth2Response = new NaverResponse((Map<String, Object>) attributes.get("response"));
                break;

            default:
                throw new RuntimeException("Provider Id Is Not Valid.");
        }

        log.info("attr = {}", attributes);

        String email;
        // 비즈니스 앱 등록 이후 getName() -> getEmail() 로 변경하시면 됩니다.
        if(registrationId.equals("kakao")){
            email = oAuth2Response.getName() + "_" + registrationId;
        }
        else{
            email = oAuth2Response.getEmail() + "_" + registrationId;
        }

        Optional<User> userOptional = userRepository.findByUserId(email);
        // login type을 따로 저장하지 않고 email@email.com_kakao 식으로 저장 됩니다.
        if(userOptional.isEmpty()){
            User user = User.builder()
                    .createdAt(LocalDateTime.now())
                    .name(oAuth2Response.getName())
                    .userId(email)
                    .deleted(false)
                    .build();

            user.setRole(Role.USER);
            User save = userRepository.save(user);

            return new OAuth2UserResponseDto(save, attributes);
        }
        else{
            User user = userOptional.get();
            if(user.getDeleted()){
                user.setDeleted(false);
                userRepository.save(user);
            }

            return new OAuth2UserResponseDto(user, attributes);
        }

    }
    public Map<String, Object> decodeJwtTokenPayload(String jwtToken) {
        Map<String, Object> jwtClaims = new HashMap<>();
        try {
            String[] parts = jwtToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(decodedString, Map.class);
            jwtClaims.putAll(map);
            log.info("jwt = {}", jwtClaims);

        } catch (JsonProcessingException e) {
            log.error("decodeJwtToken: {}-{} / jwtToken : {}", e.getMessage(), e.getCause(), jwtToken);

        }
        return jwtClaims;
    }
}
