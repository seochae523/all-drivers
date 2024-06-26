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
                                                         @RequestPart(value = "images") List<MultipartFile> image) throws IOException {

        return new ResponseEntity(userService.signUpOwner(ownerSignUpRequestDto, image), HttpStatus.CREATED);
    }
    @GetMapping("/check-nickname")
    @Operation(summary = "닉네임 중복 체크",
            description = "닉네임 중복 체크")
    public ResponseEntity<Boolean> checkNickname(@RequestParam(value = "nickname", required = false) String nickname){
        return new ResponseEntity(userService.checkNickname(nickname), HttpStatus.OK);
    }

    @PutMapping("/change-forget-password")
    @Operation(summary = "비번 변경",
            description = "잊어버린 비번 변경")
    public ResponseEntity<ChangePasswordResponseDto> updateForgetPassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto){
        return new ResponseEntity(userService.changePassword(changePasswordRequestDto), HttpStatus.OK);
    }

    @GetMapping("/user/logout")
    @Operation(description = "로그아웃 하면 refresh token 파기")
    public ResponseEntity<LogoutResponseDto> logout(@RequestParam(value = "userId", required = false)
                                                    @NotNull(message = "User Id Not Found.") String userId){
        return new ResponseEntity(userService.logout(userId), HttpStatus.OK);
    }

    @DeleteMapping("/user/delete")
    @Operation(summary = "유저 삭제",
            description = "해당 유저 삭제")
    public ResponseEntity<DeleteResponseDto> delete(@RequestParam(value = "userId", required = false) String userId){
        return new ResponseEntity(userService.delete(userId), HttpStatus.OK);
    }

    @PutMapping("/user/update")
    @Operation(summary = "유저 정보 업데이트",
            description = "유저 정보 업데이트 - 내부에서 중복확인 하니까 따로 안해도 됨")
    public ResponseEntity<UserUpdateResponseDto> update(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto){
        return new ResponseEntity(userService.update(userUpdateRequestDto), HttpStatus.OK);
    }

    @PutMapping("/user/change-password")
    @Operation(summary = "비번 변경",
        description = "입력된 정보로 비번 변경")
    public ResponseEntity<ChangePasswordResponseDto> updatePassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto){
        return new ResponseEntity(userService.changePassword(changePasswordRequestDto), HttpStatus.OK);
    }



}
