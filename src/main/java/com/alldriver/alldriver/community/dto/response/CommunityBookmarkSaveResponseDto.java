package com.alldriver.alldriver.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CommunityBookmarkSaveResponseDto {
    private Long bookmarkId;
    private Long communityId;
    private String userId;
}
