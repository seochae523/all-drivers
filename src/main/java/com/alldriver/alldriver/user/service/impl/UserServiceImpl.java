package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.common.emun.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.common.util.S3Utils;
import com.alldriver.alldriver.user.domain.*;
import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;
import com.alldriver.alldriver.user.repository.*;
import com.amazonaws.services.ec2.model.LocalGateway;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.alldriver.alldriver.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCarRepository userCarRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LicenseRepository licenseRepository;
    private final CarImageRepository carImageRepository;
    private final S3Utils s3Utils;


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        // 여긴 자격 증명 확인하는 곳
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // roles
        Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // user
        User user = (User) authenticate.getPrincipal();
        AuthToken authToken = JwtUtils.generateToken(userId, roles);

        user.setRefreshToken(authToken.getRefreshToken());
        userRepository.save(user);

        return LoginResponseDto.builder()
                .authToken(authToken)
                .nickname(user.getNickname())
                .userId(user.getUserId())
                .roles(roles)
                .build();

    }

    /**
     * TODO : 탈퇴한 유저 처리 방법 고안
     * 1. api 하나 파서 가입 여부 확인
     * 2. 지운 유져도 그냥 가입하기 - update해서
     */
    @Override
    public SignUpResponseDto signUpUser(UserSignUpRequestDto userSignUpRequestDto) {
        User user = userSignUpRequestDto.toEntity();
        user.hashPassword(passwordEncoder);
        user.setRole(Role.USER);

        User save = userRepository.save(user);

        return SignUpResponseDto.builder()
                .name(save.getName())
                .userId(save.getUserId())
                .nickname(save.getNickname())
                .build();

    }
    /**
     * TODO : 차주 회원가입시 차 사진 넣기 ㄱㄱ
     * and 화주는 사업자 등록증 입력하기
     */
    @Override
    public SignUpResponseDto signUpOwner(OwnerSignUpRequestDto ownerSignUpRequestDto, MultipartFile image) throws IOException {
        User user = ownerSignUpRequestDto.toEntity();
        user.hashPassword(passwordEncoder);
        user.setRole(Role.USER);
        user.setRole(Role.OWNER);

        User save = userRepository.save(user);

        String url = s3Utils.uploadFile(image);

        License license = License.builder()
                .licenseNumber(ownerSignUpRequestDto.getLicense())
                .url(url)
                .user(save)
                .build();

        licenseRepository.save(license);


        return SignUpResponseDto.builder()
                .userId(save.getUserId())
                .nickname(save.getNickname())
                .name(save.getName())
                .build();
    }

    @Override
    public SignUpResponseDto signUpCarOwner(CarOwnerSignUpRequestDto carOwnerSignUpRequestDto, List<MultipartFile> images) throws IOException {
        Set<UserCar> cars = new HashSet<>();
        CarInformationRequestDto carInformation = carOwnerSignUpRequestDto.getCarInformation();
        UserCar userCar = carInformation.toEntity();

        userCarRepository.findByCarNumber(carInformation.getCarNumber())
                .ifPresent(x -> {
                    throw new CustomException(ErrorCode.DUPLICATED_CAR_NUMBER, " Car Number = "+carInformation.getCarNumber());
                });

        userCarRepository.save(userCar);
        cars.add(userCar);

        User user = carOwnerSignUpRequestDto.toEntity(cars);
        user.hashPassword(passwordEncoder);
        user.setRole(Role.USER);
        user.setRole(Role.CAR_OWNER);

        User save = userRepository.save(user);

        for (MultipartFile image : images) {
            String url = s3Utils.uploadFile(image);

            CarImage carImage = CarImage.builder()
                    .userCar(userCar)
                    .url(url)
                    .build();

            carImageRepository.save(carImage);
        }

        return SignUpResponseDto.builder()
                .userId(save.getUserId())
                .nickname(save.getNickname())
                .name(save.getName())
                .build();
    }

    @Override
    public String logout() {
        String userId = JwtUtils.getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND, " User Id = " + userId));

        user.setRefreshToken(null);

        return "로그아웃 성공. user Id = " + user.getUserId();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkNickname(String nickname) {
        if(nickname == null) throw new CustomException(ErrorCode.NICKNAME_NOT_FOUND);

        userRepository.findByNickname(nickname)
                .ifPresent(x ->{
                    if(!x.getDeleted()) {
                        throw new CustomException(ErrorCode.DUPLICATED_NICKNAME, "Nickname = "+ nickname);
                    }
        });

        return true;
    }




    @Override
    public UserUpdateResponseDto update(UserUpdateRequestDto userUpdateRequestDto) {
        String userId = userUpdateRequestDto.getUserId();
        String nickname = userUpdateRequestDto.getNickname();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.updateUserInfo(userUpdateRequestDto);
        userRepository.save(user);

        return UserUpdateResponseDto.builder()
                .userId(userId)
                .nickname(nickname)
                .build();
    }

    @Override
    public ChangePasswordResponseDto changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        String userId = changePasswordRequestDto.getUserId();
        String password = changePasswordRequestDto.getPassword();

        if(userId == null) throw new CustomException(ErrorCode.USER_ID_NOT_FOUND);
        if(password == null) throw new CustomException(ErrorCode.PASSWORD_NOT_FOUND);

        User user = userRepository.findByUserId(userId).
                filter(x -> !x.getDeleted()).
                orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.updatePassword(password);
        user.hashPassword(passwordEncoder);
        userRepository.save(user);

        return ChangePasswordResponseDto.builder()
                .nickname(user.getNickname())
                .userId(userId)
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public Boolean checkPhoneNumber(PhoneNumberCheckRequestDto phoneNumberCheckRequestDto) {
        String phoneNumber = phoneNumberCheckRequestDto.getPhoneNumber();
        Integer type = phoneNumberCheckRequestDto.getType();
        if(type==0) {
            userRepository.findByPhoneNumber(phoneNumber)
                    .ifPresent(x -> {
                        throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
                    });
        }
        else if(type==1){
            userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
        }
        return true;
    }
    @Override
    @Transactional(readOnly = true)
    public Boolean checkLicense(String licenseNumber) {
        licenseRepository.findByLicenseNumber(licenseNumber)
                .ifPresent(x ->{
                   throw new CustomException(ErrorCode.DUPLICATED_LICENSE_NUMBER);
                });

        return true;
    }

    @Override
    public String signOut() {
        String userId = JwtUtils.getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.setDeleted(true);
        userRepository.save(user);

        return "회원 탈퇴 완료.";
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkDuplicatedAccount(String userId){
        userRepository.findByUserId(userId)
                .ifPresent(x ->{
                    throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);
                });
        return true;
    }

}
