package com.alldriver.alldriver.community.dto.request;

import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityComment;
import com.alldriver.alldriver.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class CommunityCommentSaveRequestDto {
    private String content;
    private Long communityId;
    private Long parentId;

    public CommunityComment toEntity(User user, Community community){
        return CommunityComment.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .content(content)
                .community(community)
                .deleted(false)
                .modified(false)
                .build();
    }

    public CommunityComment toEntity(User user, Community community, CommunityComment communityComment){
        return CommunityComment.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .content(content)
                .community(community)
                .parentComment(communityComment)
                .deleted(false)
                .modified(false)
                .build();
    }
}
