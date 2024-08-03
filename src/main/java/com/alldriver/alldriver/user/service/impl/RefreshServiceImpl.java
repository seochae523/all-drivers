package com.alldriver.alldriver.user.service.impl;



import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.dto.response.AuthToken;
import com.alldriver.alldriver.user.dto.request.RefreshRequestDto;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.RefreshService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RefreshServiceImpl implements RefreshService {
    private final UserRepository userRepository;
    public AuthToken refresh(RefreshRequestDto refreshRequestDto) throws ParseException {
        String refreshToken = refreshRequestDto.getRefreshToken();

        // early return 됩니다.
        JwtUtils.validateToken(refreshToken);

        String userId = JwtUtils.getUserIdFromRefreshToken(refreshToken);

        User findUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND, " 사용자 아이디 = " + userId));

        String role = findUser.getRole();
        List<String> roles = new ArrayList<>();

        for (String s : role.split(",")) {
            roles.add(s);
        }

        AuthToken newAuthToken = JwtUtils.generateToken(findUser.getUserId(), roles);
        findUser.setRefreshToken(newAuthToken.getRefreshToken());
        userRepository.save(findUser);

        return newAuthToken;
    }
}
