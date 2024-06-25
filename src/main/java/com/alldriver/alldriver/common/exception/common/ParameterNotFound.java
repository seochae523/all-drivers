package com.alldriver.alldriver.common.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParameterNotFound extends RuntimeException{
    String message;

    public ParameterNotFound(String message) {
        super(message);
        this.message = message;
    }
}
