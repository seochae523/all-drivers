package com.alldriver.alldriver.common.emun;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    OWNER("ROLE_OWNER"),
    CAR_OWNER("ROLE_CAR_OWNER");

    private final String value;
}
