package com.alldriver.alldriver.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
@Setter
public class AdminUserGrantResponseDto {
    private Long id;
    private String userId;
}
