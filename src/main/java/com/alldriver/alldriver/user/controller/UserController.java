package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.user.dto.request.*;
import com.alldriver.alldriver.user.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@Tag(name ="user")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    @Operation(description = "로그인")
    public ResponseEntity<AuthToken> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return new ResponseEntity(userService.login(loginRequestDto), HttpStatus.OK);
    }

    @PostMapping("/sign-up/user")
    @Operation(description = "일반 유저 회원가입")
    public ResponseEntity<SignUpResponseDto> signUpUser(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto){
        return new ResponseEntity(userService.signUpUser(userSignUpRequestDto), HttpStatus.CREATED);
    }
    @PostMapping("/sign-up/owner")
    @Operation(description = "화주 회원가입")
    public ResponseEntity<SignUpResponseDto> signUpOwner(@RequestPart(value = "request") @Valid OwnerSignUpRequestDto ownerSignUpRequestDto,
                                                         @RequestPart(value = "image") MultipartFile image) throws IOException {

        return new ResponseEntity(userService.signUpOwner(ownerSignUpRequestDto, image), HttpStatus.CREATED);
    }
    @PostMapping("/sign-up/car-owner")
    @Operation(description = "차주 회원가입")
    public ResponseEntity<SignUpResponseDto> signUpOwner(@RequestPart(value = "request") @Valid CarOwnerSignUpRequestDto carOwnerSignUpRequestDto,
                                                         @RequestPart(value = "images") List<MultipartFile> image) throws IOException {

        return new ResponseEntity(userService.signUpCarOwner(carOwnerSignUpRequestDto, image), HttpStatus.CREATED);
    }


    @PutMapping("/change-forget-password")
    @Operation(description = "잊어버린 비번 변경")
    public ResponseEntity<ChangePasswordResponseDto> updateForgetPassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto){
        return new ResponseEntity(userService.changePassword(changePasswordRequestDto), HttpStatus.OK);
    }

    @GetMapping("/user/logout")
    @Operation(description = "로그아웃 하면 refresh token 파기")
    public ResponseEntity<LogoutResponseDto> logout(@RequestParam(value = "userId", required = false)
                                                    @NotNull(message = "User Id Not Found.") String userId){
        return new ResponseEntity(userService.logout(userId), HttpStatus.OK);
    }


    @PutMapping("/user/update")
    @Operation(description = "유저 정보 업데이트 - 닉네임 변경 전에 중복 확인 하세요")
    public ResponseEntity<UserUpdateResponseDto> update(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto){
        return new ResponseEntity(userService.update(userUpdateRequestDto), HttpStatus.OK);
    }

    @PutMapping("/user/change-password")
    @Operation(description = "입력된 정보로 비번 변경")
    public ResponseEntity<ChangePasswordResponseDto> updatePassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto){
        return new ResponseEntity(userService.changePassword(changePasswordRequestDto), HttpStatus.OK);
    }

    @GetMapping("/check/user-id")
    @Operation(description = "id 중복 검사")
    public ResponseEntity<Boolean> checkUserId(@RequestParam(name = "userId") String userId){
        return new ResponseEntity(userService.checkDuplicatedAccount(userId), HttpStatus.OK);
    }

    @GetMapping("/check/license")
    @Operation(description = "사업자 등록 번호 중복 검사")
    public ResponseEntity<Boolean> checkLicense(@RequestParam(name = "license") String license){
        return new ResponseEntity(userService.checkLicense(license), HttpStatus.OK);
    }
    @GetMapping("/check/nickname")
    @Operation(description = "닉네임 중복 검사")
    public ResponseEntity<Boolean> checkNickname(@RequestParam(value = "nickname", required = false) String nickname){
        return new ResponseEntity(userService.checkNickname(nickname), HttpStatus.OK);
    }

    @DeleteMapping("/user/sign-out")
    @Operation(description = "회원 탈퇴")
    public ResponseEntity<Boolean> signOut(@RequestParam(value = "userId", required = false) String userId){
        return new ResponseEntity(userService.signOut(userId), HttpStatus.OK);
    }
}
