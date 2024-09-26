package com.alldriver.alldriver.community.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommunitySaveResponseDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;
}
