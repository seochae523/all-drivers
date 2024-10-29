package com.alldriver.alldriver.user.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserIdFindResponseDto {
    private String userId;
    private LocalDateTime createdAt;
}
