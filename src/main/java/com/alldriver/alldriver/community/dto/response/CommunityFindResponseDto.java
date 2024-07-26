package com.alldriver.alldriver.community.dto.response;


import lombok.*;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CommunityFindResponseDto {
    private Long id;
    private String title;
    private String content;
    private Integer bookmarkCount;
    private Integer bookmarked;
    private LocalDateTime createdAt;
    private String userId;
    private String location;
    private String nickname;
}
