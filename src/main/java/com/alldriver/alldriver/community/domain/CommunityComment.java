package com.alldriver.alldriver.community.domain;

import com.alldriver.alldriver.community.dto.request.CommunityCommentUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentFindResponseDto;
import com.alldriver.alldriver.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "community_comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityComment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="content", columnDefinition = "varchar", length = 100, nullable = false)
    private String content;

    @Column(name="modified", columnDefinition = "boolean", nullable = false)
    @ColumnDefault("false")
    private Boolean modified;

    @Column(name="deleted", columnDefinition = "boolean", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;

    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CommunityComment parentComment;

    @OneToMany(mappedBy = "parentComment")
    @Builder.Default
    private List<CommunityComment> childrenComment = new ArrayList<>();

    public void setDeleted(Boolean deleted){
        this.deleted =deleted;
    }
    public void updateComment(CommunityCommentUpdateRequestDto communityCommentUpdateRequestDto){
        this.content = communityCommentUpdateRequestDto.getContent();
        this.modified = true;
    }
    public CommunityCommentFindResponseDto convert(){
        if(this.deleted) {
            return CommunityCommentFindResponseDto.builder()
                    .id(this.id)
                    .content("삭제된 댓글 입니다.")
                    .modified(this.modified)
                    .createdAt(this.createdAt)
                    .userId(user.getUserId())
                    .build();
        }
        else{
            return CommunityCommentFindResponseDto.builder()
                    .id(this.id)
                    .content(this.content)
                    .modified(this.modified)
                    .createdAt(this.createdAt)
                    .userId(user.getUserId())
                    .build();
        }
    }
}
