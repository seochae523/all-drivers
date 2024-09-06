package com.alldriver.alldriver.community.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommunityUpdateResponseDto {
    private Long id;
    private String title;
    private String content;
}
