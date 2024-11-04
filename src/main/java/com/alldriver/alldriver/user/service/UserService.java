package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.board.dto.response.CarFindResponseDto;
import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface UserService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    String logout();
    SignUpResponseDto signUpUser(UserSignUpRequestDto userSignUpRequestDto);
    LoginResponseDto signUpAdditionalSocialLoginInfo(SocialLoginSignUpRequestDto socialLoginSignUpRequestDto);
    SignUpResponseDto signUpOwner(OwnerSignUpRequestDto ownerSignUpRequestDto, List<MultipartFile> images) throws IOException;
    SignUpResponseDto signUpCarOwner(CarOwnerSignUpRequestDto carOwnerSignUpRequestDto, List<MultipartFile> image) throws IOException;
    UserUpdateResponseDto update(UserUpdateRequestDto userUpdateRequestDto);
    ChangePasswordResponseDto changePassword(ChangePasswordRequestDto changePasswordRequestDto);
    String signOut();
    String upgradeUser(MultipartFile carImage, MultipartFile license, UserUpgradeRequestDto userUpgradeRequestDto) throws IOException;

    List<CarFindResponseDto> findAllCars();

    UserIdFindResponseDto findUserIdByPhoneNumber(UserIdFindRequestDto userIdFindRequestDto);

}
