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
@Tag(name = "open-api")
public class OpenApiController {
    private final OpenApiService openApiService;

    @GetMapping("/check-license")
    private ResponseEntity<Boolean> checkLicense(@RequestParam(name="license") String license) throws JsonProcessingException, URISyntaxException {
        return ResponseEntity.ok(openApiService.validateLicense(license));
    }
}
