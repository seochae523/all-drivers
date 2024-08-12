package com.alldriver.alldriver.common.exception;

import com.alldriver.alldriver.common.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomException extends RuntimeException{
    private ErrorCode errorCode;
    private String customMessage;
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public CustomException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getMessage() + customMessage);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    @Override
    public String getLocalizedMessage(){
        return this.errorCode.getMessage();
    }
}
