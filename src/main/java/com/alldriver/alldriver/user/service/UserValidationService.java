package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;

import java.net.URISyntaxException;

public interface UserValidationService {
    Boolean checkNickname(String nickname);
    Boolean checkPhoneNumber(PhoneNumberCheckRequestDto phoneNumberCheckRequestDto);
    Boolean checkLicense(String licenseNumber) throws URISyntaxException;

    Boolean checkDuplicatedAccount(String userId);

}
