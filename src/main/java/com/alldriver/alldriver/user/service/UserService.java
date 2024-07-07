package com.alldriver.alldriver.user.service;


import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    String logout();
    SignUpResponseDto signUpUser(UserSignUpRequestDto userSignUpRequestDto);
    SignUpResponseDto signUpOwner(OwnerSignUpRequestDto ownerSignUpRequestDto, MultipartFile image) throws IOException;
    SignUpResponseDto signUpCarOwner(CarOwnerSignUpRequestDto carOwnerSignUpRequestDto, List<MultipartFile> image) throws IOException;
    UserUpdateResponseDto update(UserUpdateRequestDto userUpdateRequestDto);
    ChangePasswordResponseDto changePassword(ChangePasswordRequestDto changePasswordRequestDto);
    String signOut();
}
