package com.alldriver.alldriver.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommunityCommentSaveResponseDto {
    private Long id;
    private String content;
    private Long communityId;
}
