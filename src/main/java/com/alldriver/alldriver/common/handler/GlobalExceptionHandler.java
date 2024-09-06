package com.alldriver.alldriver.common.handler;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.ApiErrorResponse;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.exception.JwtException;
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
                .body(new ApiErrorResponse(ex.getErrorCode(), ex.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation Error= {}", ex.getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, ex.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(ConstraintViolationException ex) {
        log.error("Error = {}", ex.getMessage());
        ex.getConstraintViolations().forEach(error -> {

            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
            List<Path.Node> list = stream.collect(Collectors.toList());

            String field = list.get(list.size()-1).getName();
            String message = error.getMessage();
            String invalidValue = error.getMessage().toString();

            Error errormessage = new Error();
            errormessage.setField(field);
            errormessage.setMessage(message);
            errormessage.setInvalidValue(invalidValue);

            errorList.add(errormessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, ex.getMessage()));
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ApiErrorResponse> handleValidationException(MissingServletRequestParameterException ex) {

        log.error("Missing Servlet Request Parameter Error = {}", ex.getCause());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(ErrorCode.PARAMETER_NOT_FOUND, ex.getMessage()));
    }

}

