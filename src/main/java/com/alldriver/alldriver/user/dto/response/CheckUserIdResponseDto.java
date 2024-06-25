package com.alldriver.alldriver.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CheckUserIdResponseDto {
    private Boolean isSigned;
    private Boolean isDeleted;
}
