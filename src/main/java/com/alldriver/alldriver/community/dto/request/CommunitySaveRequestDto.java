package com.alldriver.alldriver.community.dto.request;

import com.alldriver.alldriver.board.domain.SubLocation;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommunitySaveRequestDto {
    private String title;
    private String content;
    private Long subLocationId;
    public Community toEntity(SubLocation subLocation, User user){
        return Community.builder()
                .title(title)
                .content(content)
                .subLocation(subLocation)
                .user(user)
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }
}
