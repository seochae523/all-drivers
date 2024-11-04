package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.user.dto.request.PhoneNumberCheckRequestDto;
import com.alldriver.alldriver.user.service.UserValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
@Tag(name ="유저 검증 관련 api")
@Validated
public class UserValidationController {
    private final UserValidationService userValidationService;

    @GetMapping("/user-id")
    @Operation(summary = "id 중복 검사")
    public ResponseEntity<Boolean> checkUserId(@RequestParam(name = "userId") @NotBlank String userId){
        return ResponseEntity.ok(userValidationService.checkDuplicatedAccount(userId));
    }

    @GetMapping("/license")
    @Operation(summary = "사업자 등록 번호 중복 및 유효성 검사")
    public ResponseEntity<Boolean> checkLicense(@RequestParam(name = "license") @NotBlank String license) throws URISyntaxException {
        return ResponseEntity.ok(userValidationService.checkLicense(license));
    }

    @PostMapping("/phoneNumber")
    @Operation(summary = "전화번호에 따른 회원 유무 판별.", description = "type = 검증 종류별 타입. " +
            "[ 0 = 회원 가입 시 인증 번호 발급 / 1 = 비밀번호 변경 시 계정 확인 / 2 = 잊어버린 비밀번호 변경 시 계정 확인. 이때는 user id 필요 ]")
    public ResponseEntity<Boolean> checkPhoneNumber(@RequestBody @Valid PhoneNumberCheckRequestDto phoneNumberCheckRequestDto){
        return ResponseEntity.ok(userValidationService.checkPhoneNumber(phoneNumberCheckRequestDto));
    }

    @GetMapping("/email")
    @Operation(summary = "이메일 중복 검사")
    public ResponseEntity<Boolean> checkEmail(@RequestParam(name = "email") @NotBlank String email){
        return ResponseEntity.ok(userValidationService.checkDuplicatedEmail(email));
    }
}
