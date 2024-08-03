package com.alldriver.alldriver.community.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityUpdateRequestDto {
    private Long id;
    private String title;
    private String content;
}
