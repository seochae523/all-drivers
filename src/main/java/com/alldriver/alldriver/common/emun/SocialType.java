package com.alldriver.alldriver.common.emun;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    NAVER("naver"),
    KAKAO("kakao"),
    GOOGLE("google"),
    APPLE("apple");

    private final String value;
}
