package com.alldriver.alldriver.common.exception;

import com.alldriver.alldriver.common.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handleException(final JwtException ex){
        log.error("Jwt Exception [Error Code : {} Message : {}]", ex.getErrorCode().getCode(), ex.getErrorCode().getMessage());

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(new ApiErrorResponse(ex.getErrorCode(), ex.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Error = {}", ex.getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, ex.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(ConstraintViolationException ex) {
        log.error("Error = {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, ex.getMessage()));
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(MissingServletRequestParameterException ex) {
        log.error("Error = {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, ex.getMessage()));
    }
}

