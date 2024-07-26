package com.alldriver.alldriver.community.dto.request;

import lombok.Getter;

@Getter
public class CommunityUpdateRequestDto {
    private Long id;
    private String title;
    private String content;
}
