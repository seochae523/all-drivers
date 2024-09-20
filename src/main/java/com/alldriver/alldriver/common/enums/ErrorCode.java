package com.alldriver.alldriver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-001", "사용자의 아이디가 존재하지 않습니다."),
    DUPLICATED_ACCOUNT(HttpStatus.CONFLICT, "AEU-002", "중복된 계정입니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-003", "계정이 존재하지 않습니다."),
    AUTH_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-004", "인증 정보가 존재하지 않습니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "AEU-005", "중복된 닉네임입니다."),
    DUPLICATED_PHONE_NUMBER(HttpStatus.CONFLICT, "AEU-006", "중복된 휴대전화 번호입니다."),
    INVALID_SMS_AUTH_CODE(HttpStatus.BAD_REQUEST, "AEU-007", "일치하지 않는 인증 번호입니다."),
    LICENSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-008", "사업자 등록증이 존재하지 않습니다."),
    PASSWORD_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-009", "비밀번호가 존재하지 않습니다."),
    PHONE_NUMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-010", "전화번호가 존재하지 않습니다."),
    SMS_AUTH_CODE_EXPIRED(HttpStatus.REQUEST_TIMEOUT, "AEU-011", "문자 인증 시간이 만료 되었습니다."),
    SMS_AUTH_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-012", "문자 인증 번호가 존재하지 않습니다."),
    USER_NAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-013", "사용자의 이름이 존재하지 않습니다."),
    NICKNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEU-014", "사용자의 별명이 존재하지 않습니다."),
    DUPLICATED_CAR_NUMBER(HttpStatus.CONFLICT, "AEU-015", "중복된 차량 번호입니다."),
    DUPLICATED_LICENSE_NUMBER(HttpStatus.CONFLICT, "AEU-016", "중복된 사업자 번호입니다."),
    INVALID_USER(HttpStatus.BAD_REQUEST, "AEU-017", "유효하지 않은 사용자입니다."),
    FCM_TOKEN_NOT_FOUNT(HttpStatus.BAD_REQUEST, "AEU-018", "Firebase 메세지 토큰이 존재하지 않습니다."),
    FCM_SEND_FAIL(HttpStatus.BAD_REQUEST, "AEU-018", "Firebase 메시지 전송에 실패 했습니다."),

    INCORRECT_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AET-001", "잘못된 리프레시 토큰입니다."),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "AET-002", "유효하지 않은 인증 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AET-003", "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "AET-004", "리프레시 토큰을 존재하지 없습니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AET-005", "인증 토큰이 만료되었습니다."),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "AET-006", "액세스 토큰이 존재하지 않습니다."),
    BOARD_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-001", "게시글 id가 존재하지 않습니다."),
    BOARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-002", "게시글이 존재하지 않습니다."),
    CONTENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-003", "내용이 존재하지 않습니다."),
    IMAGE_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-004", "이미지 id가 존재하지 않습니다."),
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-005", "이미지가 존재하지 않습니다."),
    MAIN_LOCATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-006", "메인 지역이 존재하지 않습니다."),
    SUB_LOCATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-007", "세부 지역이 존재하지 않습니다."),
    CAR_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-008", "차량이 존재하지 않습니다."),
    JOB_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEB-009", "직종이 존재하지 않습니다."),
    INVALID_LOCATION_ID(HttpStatus.BAD_REQUEST, "AEB-010", "메인 지역 id와 세부 지역에 귀속된 메인 지역의 id가 일치하지 않습니다."),

    COMMUNITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEC-001", "커뮤니티가 존재하지 않습니다."),
    COMMUNITY_COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEC-002", "커뮤니티 댓글이 존재하지 않습니다."),
    INVALID_COMMENT(HttpStatus.BAD_REQUEST, "AEC-003", "유효하지 않은 댓글입니다."),
    COMMUNITY_BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEC-004", "커뮤니티 좋아요가 존재하지 않습니다."),

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "AEP-001", "유효하지 않은 매개변수입니다."),
    PARAMETER_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEP-002", "매개변수가 존재하지 않습니다."),

    BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "AEL-001", "즐겨찾기가 존재하지 않습니다."),
    DUPLICATED_BOOKMARK(HttpStatus.BAD_REQUEST, "AEL-002", "중복된 즐겨찾기 입니다."),

    CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "AECR-001", "채팅 방이 존재하지 않습니다."),

    ACCESS_DINY(HttpStatus.UNAUTHORIZED, "AEA-001", "접근이 거부 되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
