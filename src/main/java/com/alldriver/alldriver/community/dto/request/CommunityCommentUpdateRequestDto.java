package com.alldriver.alldriver.community.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CommunityCommentUpdateRequestDto {
    private Long id;
    private String content;
}
