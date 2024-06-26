package com.alldriver.alldriver.common.emun;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import retrofit2.http.HTTP;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-001", "User Id Not Found."),
    DUPLICATED_ACCOUNT(HttpStatus.CONFLICT, "AEU-002", "Duplicated Account."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-003", "Account Not Found."),
    AUTH_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-004", "Auth Info Not Found"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "AEU-005", "Duplicated Nickname."),
    DUPLICATED_PHONE_NUMBER(HttpStatus.CONFLICT, "AEU-006", "Duplicated Phone Number."),
    INVALID_SMS_AUTH_CODE(HttpStatus.BAD_REQUEST, "AEU-007", "Invalid Auth Code."),
    LICENSE_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-008", "License Not Found."),
    PASSWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-009", "Password Not Found."),
    PHONE_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-010", "Phone Number Not Found."),
    SMS_AUTH_CODE_EXPIRED(HttpStatus.REQUEST_TIMEOUT, "AEU-011", "Sms Auth Code Expired."),
    SMS_AUTH_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-012", "Sms Auth Code Not Found."),
    USER_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-013", "User Name Not Found."),
    NICKNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "AEU-014", "Nickname Not Found."),
    DUPLICATED_CAR_NUMBER(HttpStatus.CONFLICT, "AEU-015", "Duplicated Car Number."),
    DUPLICATED_LICENSE_NUMBER(HttpStatus.CONFLICT, "AEU-016", "Duplicated License Number."),
    INCORRECT_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AET-001", "Incorrect Refresh Token."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "AET-002", "Invalid Auth Token."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AET-003", "Invalid Refresh Token."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AET-004", "Refresh Token Not Found."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AET-005", "Auth Token Expired."),

    BOARD_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "AEB-001", "Board Id Not Found."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "AEB-002", "Board Not Found,"),
    CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "AEB-003", "Content Not Found"),
    IMAGE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "AEB-004", "Image Id Not Found"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "AEB_005", "Image Not Found.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
