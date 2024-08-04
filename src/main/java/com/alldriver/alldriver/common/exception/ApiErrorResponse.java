package com.alldriver.alldriver.common.exception;

import com.alldriver.alldriver.common.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorResponse {
    private String code;
    private String message;
    public ApiErrorResponse(ErrorCode errorCode, String customMessage) {
        super();
        this.code = errorCode.getCode();
        // custom message가 null -> custom exception 던질때 기본형 이면 그냥 메시지 만 던짐
        this.message = errorCode.getMessage() + (customMessage != null ? " " + customMessage : "");
        if(errorCode.equals(ErrorCode.PARAMETER_NOT_FOUND)){
            this.message = customMessage;
        }
    }


}