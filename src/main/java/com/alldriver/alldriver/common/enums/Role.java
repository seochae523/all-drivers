package com.alldriver.alldriver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    RECRUITER("ROLE_RECRUITER"),
    JOB_SEEKER("ROLE_JOB_SEEKER"),
    TEMP_RECRUITER("ROLE_TEMP_RECRUITER"),
    TEMP_JOB_SEEKER("ROLE_TEMP_JOB_SEEKER");

    private final String value;
}
