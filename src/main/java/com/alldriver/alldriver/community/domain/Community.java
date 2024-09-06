package com.alldriver.alldriver.community.domain;

import com.alldriver.alldriver.board.domain.SubLocation;
import com.alldriver.alldriver.community.dto.request.CommunityUpdateRequestDto;
import com.alldriver.alldriver.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "community")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", columnDefinition = "varchar", length = 50, nullable = false)
    private String title;

    @Column(name="content", columnDefinition = "text", nullable = false)
    private String content;

    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Column(name="deleted", columnDefinition = "boolean", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "community",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<CommunityComment> communityComments;

    @OneToMany(mappedBy = "community", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<CommunityBookmark> communityBookmarks;

    @OneToMany(mappedBy = "community", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<CommunityLocation> communityLocations;


    public void update(CommunityUpdateRequestDto communityUpdateRequestDto){
        this.title = communityUpdateRequestDto.getTitle();
        this.content = communityUpdateRequestDto.getContent();
        this.createdAt = LocalDateTime.now();
    }

    public void setDeleted(Boolean deleted){
        this.deleted = deleted;
    }

    public void addSubLocation(CommunityLocation communityLocation){
        if(communityLocations==null) communityLocations = new HashSet<>();
        this.communityLocations.add(communityLocation);
        communityLocation.setCommunity(this);
    }
}
