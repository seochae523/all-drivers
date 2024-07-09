package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;

public interface UserValidationService {
    Boolean checkNickname(String nickname);
    Boolean checkPhoneNumber(PhoneNumberCheckRequestDto phoneNumberCheckRequestDto);
    Boolean checkLicense(String licenseNumber);
    Boolean checkCarNumber(String carNumber);
    Boolean checkDuplicatedAccount(String userId);

}
