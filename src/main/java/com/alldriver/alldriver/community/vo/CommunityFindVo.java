package com.alldriver.alldriver.community.vo;

import java.time.LocalDateTime;

public interface CommunityFindVo {
    Long getId();
    String getTitle();
    String getContent();
    Integer getBookmarkCount();
    String getUserId();
    LocalDateTime getCreatedAt();
    String getLocationCategory();
    Integer getBookmarked();
    String getNickname();
}
