package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.common.emun.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.token.AuthTokenProvider;
import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.user.domain.License;
import com.alldriver.alldriver.user.domain.UserCar;
import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;
import com.alldriver.alldriver.user.repository.LicenseRepository;
import com.alldriver.alldriver.user.repository.UserCarRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCarRepository userCarRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenProvider authTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LicenseRepository licenseRepository;

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.cloud-front.url}")
    private String cloudFrontUrl;
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        String role = user.getRole();
        List<String> roles = new ArrayList<>();
        Collections.addAll(roles, role.split(","));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        AuthToken authToken = authTokenProvider.generateToken(userId, roles);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        user.setRefreshToken(authToken.getRefreshToken());

        userRepository.save(user);

        return LoginResponseDto.builder()
                .authToken(authToken)
                .nickname(user.getNickname())
                .userId(user.getUserId())
                .build();

    }

    /**
     * TODO : 탈퇴한 유저 처리 방법 고안
     * 1. api 하나 파서 가입 여부 확인
     * 2. 지운 유져도 그냥 가입하기 - update해서
     */
    @Override
    public SignUpResponseDto signUpUser(UserSignUpRequestDto userSignUpRequestDto) {
        String userId = userSignUpRequestDto.getUserId();
        this.checkDuplicatedAccount(userId);

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
    public SignUpResponseDto signUpOwner(OwnerSignUpRequestDto ownerSignUpRequestDto, List<MultipartFile> images) throws IOException {
        String userId = ownerSignUpRequestDto.getUserId();
        this.checkDuplicatedAccount(userId);

        User user = ownerSignUpRequestDto.toEntity();
        user.hashPassword(passwordEncoder);
        user.setRole(Role.USER);
        user.setRole(Role.OWNER);

        User save = userRepository.save(user);

        for (MultipartFile image : images) {
            String originalFilename = UUID.randomUUID() + image.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());

            amazonS3.putObject(bucket, originalFilename, image.getInputStream(), metadata);
            String url = cloudFrontUrl + "/" + originalFilename;

            License license = License.builder()
                    .licenseNumber(ownerSignUpRequestDto.getLicense())
                    .url(url)
                    .user(save)
                    .build();

            licenseRepository.save(license);
        }

        return SignUpResponseDto.builder()
                .userId(save.getUserId())
                .nickname(save.getNickname())
                .name(save.getName())
                .build();
    }

    @Override
    public SignUpResponseDto signUpCarOwner(CarOwnerSignUpRequestDto carOwnerSignUpRequestDto, List<MultipartFile> image) {
        Set<UserCar> cars = new HashSet<>();
        String userId = carOwnerSignUpRequestDto.getUserId();
        List<CarInformationRequestDto> carInformations = carOwnerSignUpRequestDto.getCarInformation();
        this.checkDuplicatedAccount(userId);

        for (CarInformationRequestDto carInformation : carInformations) {
            UserCar userCar = carInformation.toEntity();

            userCarRepository.findByCarNumber(carInformation.getCarNumber())
                    .ifPresent(x -> {
                        throw new CustomException(ErrorCode.DUPLICATED_CAR_NUMBER);
                    });
            userCarRepository.save(userCar);
            cars.add(userCar);
        }
        User user = carOwnerSignUpRequestDto.toEntity(cars);
        user.hashPassword(passwordEncoder);
        user.setRole(Role.USER);
        user.setRole(Role.CAR_OWNER);

        User save = userRepository.save(user);

        return SignUpResponseDto.builder()
                .userId(save.getUserId())
                .nickname(save.getNickname())
                .name(save.getName())
                .build();
    }

    @Override
    public LogoutResponseDto logout(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.setRefreshToken(null);

        return new LogoutResponseDto(user);
    }

    @Override
    public Boolean checkNickname(String nickname) {
        if(nickname == null) throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);

        userRepository.findByNickname(nickname)
                .ifPresent(x ->{
                    if(!x.getDeleted()) {
                        throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
                    }
        });

        return true;
    }


    @Override
    public DeleteResponseDto delete(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // soft delete
        user.setDeleted(true);
        userRepository.save(user);

        return DeleteResponseDto.builder()
                .name(user.getName())
                .userId(userId)
                .build();
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
    public Boolean checkLicense(String licenseNumber) {
        licenseRepository.findByLicenseNumber(licenseNumber)
                .ifPresent(x ->{
                   throw new CustomException(ErrorCode.DUPLICATED_LICENSE_NUMBER);
                });

        return true;
    }

    @Override
    public String signOut(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.setDeleted(true);
        userRepository.save(user);

        return "회원 탈퇴 완료.";
    }

    private void checkDuplicatedAccount(String userId){
        userRepository.findByUserId(userId)
                .ifPresent(x ->{
                    throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);
                });
    }

}
