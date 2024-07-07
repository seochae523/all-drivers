package com.alldriver.alldriver.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final CustomException ex){
        log.error("Custom Exception [Error Code : {} Message : {}]", ex.getErrorCode().getCode(), ex.getErrorCode().getMessage());

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(new ApiErrorResponse(ex.getErrorCode(), ex.getCustomMessage()));

    }


}

