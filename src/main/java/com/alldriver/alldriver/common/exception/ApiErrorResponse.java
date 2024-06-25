package com.alldriver.alldriver.common.exception;

import com.alldriver.alldriver.common.emun.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorResponse {
    private String code;
    private String message;

    public ApiErrorResponse(ErrorCode errorCode) {
        super();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }


}