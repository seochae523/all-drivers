package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.common.util.S3Utils;
import com.alldriver.alldriver.user.domain.*;
import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;
import com.alldriver.alldriver.user.repository.*;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final S3Utils s3Utils;


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        // 자격 증명 확인
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
    public SignUpResponseDto signUpOwner(OwnerSignUpRequestDto ownerSignUpRequestDto, List<MultipartFile> images) throws IOException {
        User user = ownerSignUpRequestDto.toEntity();
        user.hashPassword(passwordEncoder);
        user.setRole(Role.USER);
        user.setRole(Role.OWNER);

        for (MultipartFile image : images) {
            String url = s3Utils.uploadFile(image);

            License license = License.builder()
                    .licenseNumber(ownerSignUpRequestDto.getLicense())
                    .url(url)
                    .build();

            user.addLicense(license);
        }


        User save = userRepository.save(user);

        return SignUpResponseDto.builder()
                .userId(save.getUserId())
                .nickname(save.getNickname())
                .name(save.getName())
                .build();
    }

    @Override
    public SignUpResponseDto signUpCarOwner(CarOwnerSignUpRequestDto carOwnerSignUpRequestDto, List<MultipartFile> images) throws IOException {
        CarInformationRequestDto carInformation = carOwnerSignUpRequestDto.getCarInformation();

        User user = carOwnerSignUpRequestDto.toEntity();
        UserCar userCar = carInformation.toEntity();

        user.hashPassword(passwordEncoder);
        user.setRole(Role.USER);
        user.setRole(Role.CAR_OWNER);
        user.addUserCar(userCar);

        User save = userRepository.save(user);

        for (MultipartFile image : images) {
            String url = s3Utils.uploadFile(image);

            CarImage carImage = CarImage.builder()
                    .userCar(userCar)
                    .url(url)
                    .build();

            userCar.addCarImage(carImage);
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
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND, " 사용자 아이디 = " + userId));

        user.setRefreshToken(null);

        return "로그아웃 성공. User Id = " + user.getUserId();
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
    public String signOut() {
        String userId = JwtUtils.getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.setDeleted(true);
        userRepository.save(user);

        return "회원 탈퇴 완료.";
    }

    @Override
    public String upgradeUser(List<MultipartFile> images, UserUpgradeRequestDto userUpgradeRequestDto) throws IOException {
        Integer type = userUpgradeRequestDto.getType();
        String userId = JwtUtils.getUserId();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND, " 사용자 아아디 = " + userId));

        // type == 0 : user -> car owner
        if(type == 0){
            // 차량 정보는 단건만 저장
            CarInformationRequestDto carInfo = userUpgradeRequestDto.getCarInfo();
            UserCar userCar = carInfo.toEntity();

            user.setRole(Role.CAR_OWNER);
            user.addUserCar(userCar);

            userRepository.save(user);
            for (MultipartFile image : images) {
                String url = s3Utils.uploadFile(image);

                CarImage carImage = CarImage.builder()
                        .userCar(userCar)
                        .url(url)
                        .build();

                userCar.addCarImage(carImage);
            }

        }

        // type == 1 user -> owner
        else if(type == 1){
            user.setRole(Role.OWNER);

            for(MultipartFile image : images) {
                String url = s3Utils.uploadFile(image);

                License license = License.builder()
                        .licenseNumber(userUpgradeRequestDto.getLicenseNumber())
                        .url(url)
                        .build();

                user.addLicense(license);
            }
            userRepository.save(user);
        }
        else{
            throw new CustomException(ErrorCode.INVALID_PARAMETER, " type 파라미터는 0 또는 1이어야 합니다.");
        }

        return "유저 업그레이드 완료.";
    }

}
