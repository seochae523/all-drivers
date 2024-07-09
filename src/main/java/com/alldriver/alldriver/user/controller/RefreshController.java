package com.alldriver.alldriver.user.controller;


import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.common.token.dto.RefreshRequestDto;
import com.alldriver.alldriver.user.service.RefreshService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/refresh")
@Tag(name = "Refresh")
public class RefreshController {
    private final RefreshService refreshService;
    @Operation(description = "access token 만료시 refresh token을 이용하여 재발급")
    @PostMapping
    public ResponseEntity<AuthToken> refresh(@RequestBody @Valid RefreshRequestDto refreshRequestDto) throws ParseException {
        return ResponseEntity.ok(refreshService.refresh(refreshRequestDto));
    }
}
