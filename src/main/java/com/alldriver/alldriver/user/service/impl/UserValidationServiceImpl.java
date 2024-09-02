package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.LicenseNumberValidator;
import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;
import com.alldriver.alldriver.user.repository.LicenseRepository;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserValidationServiceImpl implements UserValidationService {
    private final UserRepository userRepository;
    private final LicenseRepository licenseRepository;
    private final LicenseNumberValidator licenseNumberValidator;

    @Override
    public Boolean checkNickname(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(x ->{
                        throw new CustomException(ErrorCode.DUPLICATED_NICKNAME, " 닉네임 = "+ nickname);
                });

        return true;
    }

    @Override
    public Boolean checkPhoneNumber(PhoneNumberCheckRequestDto phoneNumberCheckRequestDto) {
        String phoneNumber = phoneNumberCheckRequestDto.getPhoneNumber();
        Integer type = phoneNumberCheckRequestDto.getType();
        // TYPE == 0 : 회원 가입 시 핸드폰 번호 검증
        if(type == 0) {
            userRepository.findByPhoneNumber(phoneNumber)
                    .ifPresent(x -> {
                        throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
                    });
        }

        // TYPE == 1 : 비밀번호 번경 시 핸드폰 번호 검증
        else if(type == 1){
            userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        }

        // TYPE == 2 : 잊어버린 비밀번호 변경 시 유저 아이디 + 핸드폰 번호로 유저 검증
        else if(type ==2) {
            String userId = phoneNumberCheckRequestDto.getUserId();
            userRepository.findByUserIdAndPhoneNumber(userId, phoneNumber)
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        }

        else{
            throw new CustomException(ErrorCode.INVALID_PARAMETER, " type 파라미터는 0 ~ 2어야 합니다.");
        }
        return true;
    }

    @Override
    public Boolean checkLicense(String licenseNumber) throws URISyntaxException {
        licenseRepository.findByLicenseNumber(licenseNumber)
                .ifPresent(x ->{
                    throw new CustomException(ErrorCode.DUPLICATED_LICENSE_NUMBER, " 사업자 등록 번호 = " + licenseNumber);
                });

        return licenseNumberValidator.validateLicense(licenseNumber);
    }



    @Override
    public Boolean checkDuplicatedAccount(String userId){
        userRepository.findByUserId(userId)
                .ifPresent(x ->{
                    throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT, " 사용자 아이디 = " + userId);
                });
        return true;
    }


}
