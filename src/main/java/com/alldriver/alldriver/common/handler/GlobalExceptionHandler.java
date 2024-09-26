package com.alldriver.alldriver.common.handler;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.ApiErrorResponse;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.exception.JwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


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
                .body(new ApiErrorResponse(ex.getErrorCode(), null));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation Error= {}", ex.getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, ex.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder();
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                .reduce((first, second) -> second)
                                .get().toString(),
                        ConstraintViolation::getMessage
                ));
        for (String value : errors.values()) {
            message.append(value);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, message.toString()));
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(MissingServletRequestParameterException ex) {

        log.error("Missing Servlet Request Parameter Error = {}", ex.getCause());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, ex.getMessage()));
    }

}

