package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.user.service.OpenApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private ResponseEntity<Boolean> checkLicense(@RequestParam(name="license") String license) throws JsonProcessingException, URISyntaxException {
        return ResponseEntity.ok(openApiService.validateLicense(license));
    }
}
