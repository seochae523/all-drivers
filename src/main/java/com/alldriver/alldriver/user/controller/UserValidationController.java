package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;
import com.alldriver.alldriver.user.service.UserValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
@Tag(name ="user validation")
@Validated
public class UserValidationController {
    private final UserValidationService userValidationService;

    @GetMapping("/user-id")
    @Operation(description = "id 중복 검사")
    public ResponseEntity<Boolean> checkUserId(@RequestParam(name = "userId") String userId){
        return new ResponseEntity(userValidationService.checkDuplicatedAccount(userId), HttpStatus.OK);
    }

    @GetMapping("/license")
    @Operation(description = "사업자 등록 번호 중복 검사")
    public ResponseEntity<Boolean> checkLicense(@RequestParam(name = "license") String license){
        return new ResponseEntity(userValidationService.checkLicense(license), HttpStatus.OK);
    }
    @GetMapping("/nickname")
    @Operation(description = "닉네임 중복 검사")
    public ResponseEntity<Boolean> checkNickname(@RequestParam(value = "nickname", required = false) String nickname){
        return new ResponseEntity(userValidationService.checkNickname(nickname), HttpStatus.OK);
    }
    @PostMapping("/phoneNumber")
    @Operation(description = "전화번호에 따른 회원 유무 판별. type = 검증 종류별 타입. " +
            "[ 0 = 회원 가입 시 인증 번호 발급 / 1 = 비밀번호 변경 시 계정 확인 / 2 = 잊어버린 비밀번호 변경 시 계정 확인. 이때는 user id 필요 ]")
    public ResponseEntity<Boolean> checkPhoneNumber(@RequestBody @Valid PhoneNumberCheckRequestDto phoneNumberCheckRequestDto){
        return new ResponseEntity(userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto), HttpStatus.OK);
    }
}
