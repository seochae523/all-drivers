package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.board.domain.Car;
import com.alldriver.alldriver.board.dto.response.CarFindResponseDto;
import com.alldriver.alldriver.board.repository.CarRepository;
import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.user.dto.response.AuthToken;
import com.alldriver.alldriver.common.util.S3Utils;
import com.alldriver.alldriver.user.domain.*;
import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;
import com.alldriver.alldriver.user.repository.*;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private final CarRepository carRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();
        FcmToken fcmToken = FcmToken.builder().token(loginRequestDto.getFcmToken()).build();
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
        FcmToken find = user.getFcmToken();

        if(find!=null){
            find.updateToken(loginRequestDto.getFcmToken());
        }
        else{
            user.addFcmToken(fcmToken);
        }
        user.setRefreshToken(authToken.getRefreshToken());


        userRepository.save(user);

        return LoginResponseDto.builder()
                .authToken(authToken)
                .userId(user.getUserId())
                .name(user.getName())
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
        user.setRole(Role.TEMP_JOB_SEEKER);

        FcmToken fcmToken = FcmToken.builder().token(userSignUpRequestDto.getFcmToken()).build();

        user.addFcmToken(fcmToken);
        User save = userRepository.save(user);

        return SignUpResponseDto.builder()
                .name(save.getName())
                .userId(save.getUserId())
                .role(save.getRole())
                .build();

    }

    @Override
    public LoginResponseDto signUpAdditionalSocialLoginInfo(SocialLoginSignUpRequestDto socialLoginSignUpRequestDto) {
        String userId = socialLoginSignUpRequestDto.getUserId();

        FcmToken fcmToken = FcmToken.builder()
                .token(socialLoginSignUpRequestDto.getFcmToken())
                .build();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.addFcmToken(fcmToken);

        User save = userRepository.save(user);

        List<String> roles = List.of(user.getRole().split(","));
        AuthToken authToken = JwtUtils.generateToken(userId, roles);

        return LoginResponseDto.builder()
                .authToken(authToken)
                .userId(save.getUserId())
                .name(save.getName())
                .roles(roles)
                .build();
    }

    /**
     * TODO : 차주 회원가입시 차 사진 넣기 ㄱㄱ
     * and 화주는 사업자 등록증 입력하기
     */
    @Override
    public SignUpResponseDto signUpOwner(OwnerSignUpRequestDto ownerSignUpRequestDto, List<MultipartFile> images) throws IOException {
        if(userRepository.findByUserId(ownerSignUpRequestDto.getUserId()).isPresent()
                || userRepository.findByPhoneNumber(ownerSignUpRequestDto.getPhoneNumber()).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);
        }
        User user = ownerSignUpRequestDto.toEntity();
        user.hashPassword(passwordEncoder);

        user.setRole(Role.USER);
        user.setRole(Role.TEMP_RECRUITER);

        for (MultipartFile image : images) {
            String url = s3Utils.uploadFile(image);

            CompanyInformation companyInformation = CompanyInformation.builder()
                    .licenseNumber(ownerSignUpRequestDto.getLicense())
                    .image(url)
                    .companyLocation(ownerSignUpRequestDto.getCompanyLocation())
                    .startAt(ownerSignUpRequestDto.getStartedAt())
                    .build();

            user.addCompanyInformation(companyInformation);
        }
        FcmToken fcmToken = FcmToken.builder()
                .token(ownerSignUpRequestDto.getFcmToken())
                .build();

        user.addFcmToken(fcmToken);

        User save = userRepository.save(user);

        return SignUpResponseDto.builder()
                .userId(save.getUserId())
                .name(save.getName())
                .role(save.getRole())
                .build();
    }

    @Override
    public SignUpResponseDto signUpCarOwner(CarOwnerSignUpRequestDto carOwnerSignUpRequestDto, List<MultipartFile> images) throws IOException {
        Integer type = carOwnerSignUpRequestDto.getType();
        User user = carOwnerSignUpRequestDto.toEntity();

        user.hashPassword(passwordEncoder);

        user.setRole(Role.USER);

        if (type == 0) user.setRole(Role.TEMP_JOB_SEEKER);
        else if (type == 1) user.setRole(Role.TEMP_RECRUITER);
        else throw new CustomException(ErrorCode.INVALID_PARAMETER, " 타입은 0 또는 1이어야 합니다.");

        FcmToken fcmToken = FcmToken.builder()
                .token(carOwnerSignUpRequestDto.getFcmToken())
                .build();

        user.addFcmToken(fcmToken);

        for (MultipartFile image : images) {
            String url = s3Utils.uploadFile(image);

            UserCarInformation userCarInformation = UserCarInformation.builder()
                    .image(url)
                    .build();

            user.addUserCarInformation(userCarInformation);
        }

        User save = userRepository.save(user);

        return SignUpResponseDto.builder()
                .userId(save.getUserId())
                .name(save.getName())
                .role(save.getRole())
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

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.updateUserInfo(userUpdateRequestDto);
        userRepository.save(user);

        return UserUpdateResponseDto.builder()
                .userId(userId)
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
    public String upgradeUser(MultipartFile carImage, MultipartFile license, UserUpgradeRequestDto userUpgradeRequestDto) throws IOException {
        Integer type = userUpgradeRequestDto.getType();
        String userId = JwtUtils.getUserId();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND, " 사용자 아아디 = " + userId));

        // type == 0 : user -> car owner (구직자)
        if(type == 0){
            String carImageUrl = s3Utils.uploadFile(carImage);

            UserCarInformation userCarInformation = UserCarInformation.builder()
                    .image(carImageUrl)
                    .build();

            user.addUserCarInformation(userCarInformation);
        }

        // car owner 구직자 -> 구인자
        else if(type==1){
            user.removeJobSeeker();
            user.setRole(Role.TEMP_RECRUITER);

            CompanyInformationRequestDto companyInfo = userUpgradeRequestDto.getCompanyInfo();
            String licenseImageUrl = s3Utils.uploadFile(license);

            user.addCompanyInformation(companyInfo.toEntity(licenseImageUrl));
        }

        else{
            throw new CustomException(ErrorCode.INVALID_PARAMETER, " type 파라미터는 0 또는 1 이어야 합니다.");
        }
        userRepository.save(user);

        return "유저 업그레이드 완료.";
    }

    @Override
    public List<CarFindResponseDto> findAllCars() {
        List<Car> result = carRepository.findAll();
        return result.stream()
                .map(car -> new CarFindResponseDto(car.getId(), car.getCategory()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserIdFindResponseDto findUserIdByPhoneNumber(UserIdFindRequestDto userIdFindRequestDto) {
        String phoneNumber = userIdFindRequestDto.getPhoneNumber();

        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            String userId = getBlindUserId(user);
            return UserIdFindResponseDto.builder()
                    .createdAt(user.getCreatedAt())
                    .userId(userId)
                    .build();
        }
        else{
            throw new CustomException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
    }


    private String getBlindUserId(User user) {
        String userId = user.getUserId();
        int idx = userId.length() - 3;
        userId = userId.substring(0, idx) + "***";
        return userId;
    }

}
