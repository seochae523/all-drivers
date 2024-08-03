package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.alldriver.alldriver.user.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name ="유저 관련 api")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    @Operation(description = "로그인")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @PostMapping("/sign-up/user")
    @Operation(description = "일반 유저 회원가입")
    public ResponseEntity<SignUpResponseDto> signUpUser(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto){
        return ResponseEntity.ok(userService.signUpUser(userSignUpRequestDto));
    }
    @PostMapping("/sign-up/owner")
    @Operation(description = "화주 회원가입")
    public ResponseEntity<SignUpResponseDto> signUpOwner(@RequestPart(value = "request") @Valid OwnerSignUpRequestDto ownerSignUpRequestDto,
                                                         @RequestPart(value = "images") List<MultipartFile> images) throws IOException {

        return ResponseEntity.ok(userService.signUpOwner(ownerSignUpRequestDto, images));
    }
    @PostMapping("/sign-up/car-owner")
    @Operation(description = "차주 회원가입")
    public ResponseEntity<SignUpResponseDto> signUpCarOwner(@RequestPart(value = "request") @Valid CarOwnerSignUpRequestDto carOwnerSignUpRequestDto,
                                                         @RequestPart(value = "images") List<MultipartFile> images) throws IOException {

        return ResponseEntity.ok(userService.signUpCarOwner(carOwnerSignUpRequestDto, images));
    }


    @PutMapping("/change-forget-password")
    @Operation(description = "잊어버린 비번 변경")
    public ResponseEntity<ChangePasswordResponseDto> updateForgetPassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto){
        return ResponseEntity.ok(userService.changePassword(changePasswordRequestDto));
    }

    @GetMapping("/user/logout")
    @Operation(description = "로그아웃 하면 refresh token 파기")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok(userService.logout());
    }


    @PutMapping("/user/update")
    @Operation(description = "유저 정보 업데이트 - 닉네임 변경 전에 중복 확인 하세요")
    public ResponseEntity<UserUpdateResponseDto> update(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto){
        return ResponseEntity.ok(userService.update(userUpdateRequestDto));
    }

    @PutMapping("/user/change-password")
    @Operation(description = "입력된 정보로 비번 변경")
    public ResponseEntity<ChangePasswordResponseDto> updatePassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto){
        return ResponseEntity.ok(userService.changePassword(changePasswordRequestDto));
    }
    @PutMapping("/user/upgrade")
    @Operation(description = "회원 권한 상승 <br> type : 상승 할 권한 [type = 0 - 일반 유저에서 차주로 권한 업그레이드 이때는 차량 이미지 3장 필요, type = 1 - 일반 유저에서 화주로 권한 업그레이드 이때는 사진 1장만 필요]")
    public ResponseEntity<String> upgradeUser(@RequestPart(value = "images") List<MultipartFile> images,
                                              @RequestPart(value = "request") UserUpgradeRequestDto userUpgradeRequestDto) throws IOException {
        return ResponseEntity.ok(userService.upgradeUser(images, userUpgradeRequestDto));
    }
    @DeleteMapping("/user/sign-out")
    @Operation(description = "회원 탈퇴")
    public ResponseEntity<String> signOut(){
        return ResponseEntity.ok(userService.signOut());
    }

}
