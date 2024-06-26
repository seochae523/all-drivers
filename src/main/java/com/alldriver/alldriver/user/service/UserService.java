package com.alldriver.alldriver.user.service;


import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    LogoutResponseDto logout(String userId);
    SignUpResponseDto signUpUser(UserSignUpRequestDto userSignUpRequestDto);
    SignUpResponseDto signUpOwner(OwnerSignUpRequestDto ownerSignUpRequestDto, List<MultipartFile> image) throws IOException;
    SignUpResponseDto signUpCarOwner(CarOwnerSignUpRequestDto carOwnerSignUpRequestDto, List<MultipartFile> image);
    Boolean checkNickname(String nickname);
    DeleteResponseDto delete(String userId);
    UserUpdateResponseDto update(UserUpdateRequestDto userUpdateRequestDto);
    ChangePasswordResponseDto changePassword(ChangePasswordRequestDto changePasswordRequestDto);
    Boolean checkLicense(String licenseNumber);
    String signOut(String userId);

}
