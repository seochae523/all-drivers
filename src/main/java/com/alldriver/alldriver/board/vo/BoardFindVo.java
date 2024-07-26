package com.alldriver.alldriver.board.vo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BoardFindVo {
    Long getBoardId();
    String getContent();
    String getCategory();
    String getTitle();
    String getUserId();
    LocalDateTime getCreatedAt();
    Integer getPayment();
    String getPayType();
    String getCompanyLocation();
    String getRecruitType();
    String getMainLocation();

    String getLocationCategory();

    String getJobCategory();

    String getCarCategory();
    Date getStartAt();
    Date getEndAt();
    String getUserNickname();
    Long getBookmarkCount();
    Integer getBookmarked();
}
