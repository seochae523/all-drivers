package com.alldriver.alldriver.community.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class CommunityCommentFindResponseDto {
    private Long id;
    private String content;
    private Boolean modified;
    private LocalDateTime createdAt;
    private String nickname;
    @Builder.Default
    private List<CommunityCommentFindResponseDto> children = new ArrayList<>();
}
