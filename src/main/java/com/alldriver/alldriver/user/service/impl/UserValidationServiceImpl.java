package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;
import com.alldriver.alldriver.user.repository.LicenseRepository;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserValidationServiceImpl implements UserValidationService {
    private final UserRepository userRepository;
    private final LicenseRepository licenseRepository;
    @Override
    public Boolean checkNickname(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(x ->{
                        throw new CustomException(ErrorCode.DUPLICATED_NICKNAME, " Nickname = "+ nickname);
                });

        return true;
    }

    @Override
    public Boolean checkPhoneNumber(PhoneNumberCheckRequestDto phoneNumberCheckRequestDto) {
        String phoneNumber = phoneNumberCheckRequestDto.getPhoneNumber();
        Integer type = phoneNumberCheckRequestDto.getType();
        if(type == 0) {
            userRepository.findByPhoneNumber(phoneNumber)
                    .ifPresent(x -> {
                        throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
                    });
        }
        else if(type == 1){
            userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        }
        else{
            throw new CustomException(ErrorCode.INVALID_PARAMETER, " Type Must Be 0 Or 1.");
        }
        return true;
    }
    @Override
    public Boolean checkLicense(String licenseNumber) {
        licenseRepository.findByLicenseNumber(licenseNumber)
                .ifPresent(x ->{
                    throw new CustomException(ErrorCode.DUPLICATED_LICENSE_NUMBER, " License Number = " + licenseNumber);
                });

        return true;
    }
    @Override
    public Boolean checkDuplicatedAccount(String userId){
        userRepository.findByUserId(userId)
                .ifPresent(x ->{
                    throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT, " User Id = " + userId);
                });
        return true;
    }
}
