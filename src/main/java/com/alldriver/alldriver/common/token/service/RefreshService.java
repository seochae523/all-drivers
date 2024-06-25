package com.alldriver.alldriver.common.token.service;



import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.token.AuthTokenProvider;
import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.common.token.dto.RefreshRequestDto;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshService {
    private final UserRepository userRepository;
    private final AuthTokenProvider authTokenProvider;

    public AuthToken refresh(RefreshRequestDto refreshRequestDto) throws ParseException {
        if(validation(refreshRequestDto)) {
            String subject = getSubject(refreshRequestDto);

            Authentication authentication = authTokenProvider.getAuthentication(refreshRequestDto.getAccessToken());

            User findUser = userRepository.findByUserId(subject)
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

            String role = findUser.getRole();
            List<String> roles = new ArrayList<>();

            for (String s : role.split(",")) {
                roles.add(s);
            }

            AuthToken newAuthToken = authTokenProvider.generateToken(findUser.getUserId(), roles);
            findUser.updateRefreshToken(newAuthToken.getRefreshToken());
            userRepository.save(findUser);
            return newAuthToken;
        }
        return null;
    }
    private boolean validation(RefreshRequestDto refreshRequestDto) throws ParseException {
        String refreshToken = refreshRequestDto.getRefreshToken();
        String userId = refreshRequestDto.getUserId();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        String userRefreshToken = user.getRefreshToken();
        if(!userRefreshToken.equals(refreshToken)) throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        long now = System.currentTimeMillis();
        long expirationTime = getExpireTime(refreshToken);

        if(expirationTime - now <= 0) throw new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED);

        return true;
    }
    private String decode(String refreshToken){
        Base64.Decoder decoder = Base64.getDecoder();
        String[] splitJwt = refreshToken.split("\\.");

        String payloadStr = new String(decoder.decode(splitJwt[1].getBytes()));

        return payloadStr;
    }

    private long getExpireTime(String refreshToken) throws ParseException {
        String payload = this.decode(refreshToken);
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(payload);
        long expirationTime = (long) object.get("exp") * 1000;

        return expirationTime;
    }
    private String getSubject(RefreshRequestDto refreshRequestDto) throws ParseException {
        String refreshToken = refreshRequestDto.getRefreshToken();
        String payload = this.decode(refreshToken);
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(payload);
        String subject = object.get("sub").toString();
        return subject;
    }
}
