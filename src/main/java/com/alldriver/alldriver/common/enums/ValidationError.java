package com.alldriver.alldriver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationError {
    PAGE_NOT_FOUND,
    CONTENT_NOT_FOUND,
    CATEGORY_NOT_FOUND,
    TITLE_NOT_FOUND,
    PAY_TYPE_NOT_FOUND,
    PAYMENT_NOT_FOUND,
    START_AT_NOT_FOUND,
    END_AT_NOT_FOUND,
    RECRUIT_TYPE_NOT_FOUND,
    COMPANY_LOCATION_NOT_FOUND,
    MAIN_LOCATION_ID_NOT_FOUND,
    SUB_LOCATION_ID_NOT_FOUND,
    UPDATE_TYPE_NOT_FOUND,
    CAR_ID_NOT_FOUND,
    JOB_ID_NOT_FOUND,
    LOCATION_ID_NOT_FOUND,
    COMMUNITY_ID_NOT_FOUND,
    CREATED_AT_NOT_FOUND,
    CAR_NUMBER_NOT_FOUND,
    NAME_NOT_FOUND,
    USER_ID_NOT_FOUND,
    PASSWORD_NOT_FOUND,
    NICKNAME_NOT_FOUND ,
    PHONE_NUMBER_NOT_FOUND ,
    FCM_TOKEN_NOT_FOUND ,
    BUSINESS_NUMBER_NOT_FOUND,
    REFRESH_TOKEN_NOT_FOUND,
    AUTH_CODE_NOT_FOUND,
    BOARD_ID_NOT_FOUND,
    COMMENT_ID_NOT_FOUND,
    FAQ_ID_NOT_FOUND,
    CAR_WEIGHT_NOT_FOUND,
    CAR_INFORMATION_NOT_FOUND,
    TYPE_NOT_FOUND,
    KEYWORD_NOT_FOUND;

    public static class Message{
        public static final String KEYWORD_NOT_FOUND = "키워드가 존재하지 않습니다.";
        public static final String PAGE_NOT_FOUND = "페이지가 존재하지 않습니다.";
        public static final String TYPE_NOT_FOUND = "타입이 존재하지 않습니다.";
        public static final String CAR_INFORMATION_NOT_FOUND = "차량 정보가 존재하지 않습니다.";
        public static final String CAR_WEIGHT_NOT_FOUND="차량 무게가 존재하지 않습니다.";
        public static final String FAQ_ID_NOT_FOUND="Faq id가 존재하지 않습니다.";
        public static final String COMMENT_ID_NOT_FOUND="댓글 id가 존재하지 않습니다.";
        public static final String CONTENT_NOT_FOUND="내용이 존재하지 않습니다.";
        public static final String CATEGORY_NOT_FOUND = "카테고리가 존재하지 않습니다.";
        public static final String TITLE_NOT_FOUND = "제목이 존재하지 않습니다.";
        public static final String PAY_TYPE_NOT_FOUND = "급여 유형이 존재하지 않습니다.";
        public static final String PAYMENT_NOT_FOUND = "급여가 존재하지 않습니다.";
        public static final String START_AT_NOT_FOUND = "시작 일자가 존재하지 않습니다.";
        public static final String END_AT_NOT_FOUND = "종료 일자가 존재하지 않습니다.";
        public static final String RECRUIT_TYPE_NOT_FOUND = "고용 형태가 존재하지 않습니다.";
        public static final String COMPANY_LOCATION_NOT_FOUND = "회사 주소가 존재하지 않습니다.";
        public static final String MAIN_LOCATION_ID_NOT_FOUND = "메인 지역 id가 존재하지 않습니다.";
        public static final String SUB_LOCATION_ID_NOT_FOUND = "세부 지역 id가 존재하지 않습니다.";
        public static final String UPDATE_TYPE_NOT_FOUND = "업데이트 타입이 존재하지 않습니다.";
        public static final String CAR_ID_NOT_FOUND = "차종 id가 존재하지 않습니다.";
        public static final String JOB_ID_NOT_FOUND = "직종 id가 존재하지 않습니다.";
        public static final String LOCATION_ID_NOT_FOUND = "지역 id가 존재하지 않습니다.";
        public static final String COMMUNITY_ID_NOT_FOUND = "커뮤니티 id가 존재하지 않습니다.";
        public static final String CREATED_AT_NOT_FOUND = "생성일이 존재하지 않습니다.";
        public static final String CAR_NUMBER_NOT_FOUND = "차량 번호가 존재하지 않습니다.";
        public static final String NAME_NOT_FOUND = "이름이 존재하지 않습니다.";
        public static final String USER_ID_NOT_FOUND = "사용자 아이디가 존재하지 않습니다.";
        public static final String PASSWORD_NOT_FOUND = "비밀번호가 존재하지 않습니다.";
        public static final String NICKNAME_NOT_FOUND = "닉네임이 존재하지 않습니다.";
        public static final String PHONE_NUMBER_NOT_FOUND = "전화번호가 존재하지 않습니다.";
        public static final String FCM_TOKEN_NOT_FOUND = "Fcm 토큰이 존재하지 않습니다.";
        public static final String BUSINESS_NUMBER_NOT_FOUND = "사업자 등록 번호가 존재하지 않습니다.";
        public static final String REFRESH_TOKEN_NOT_FOUND = "리프레시 토큰이 존재하지 않습니다.";
        public static final String AUTH_CODE_NOT_FOUND = "인증 번호가 존재하지 않습니다.";
        public static final String BOARD_ID_NOT_FOUND = "게시글 id가 존재하지 않습니다.";
    }
}
