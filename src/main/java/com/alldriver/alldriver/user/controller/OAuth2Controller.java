package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "알림 관련 api")
public class OAuth2Controller {

    @GetMapping("/no-auth")
    public ResponseEntity<ApiErrorResponse> noAuth(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiErrorResponse(ErrorCode.ACCESS_DINY, null));
    }
}
