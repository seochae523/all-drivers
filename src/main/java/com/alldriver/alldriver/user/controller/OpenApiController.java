package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.user.service.OpenApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "차랑, 사업자 등록증 검증 관련 api")
public class OpenApiController {
    private final OpenApiService openApiService;

    @GetMapping("/verify/license")
    @Operation(summary = "사업자 등록 번호 검증")
    private ResponseEntity<Boolean> checkLicense(@RequestParam(name="license")
                                                 @NotBlank(message = ValidationError.Message.BUSINESS_NUMBER_NOT_FOUND) String license) throws JsonProcessingException, URISyntaxException {
        return ResponseEntity.ok(openApiService.validateLicense(license));
    }
}
