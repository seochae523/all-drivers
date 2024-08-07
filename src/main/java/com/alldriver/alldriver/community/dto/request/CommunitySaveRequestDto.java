package com.alldriver.alldriver.community.dto.request;

import com.alldriver.alldriver.board.domain.SubLocation;
import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommunitySaveRequestDto {
    @NotBlank(message = ValidationError.Message.TITLE_NOT_FOUND)
    private String title;
    @NotBlank(message = ValidationError.Message.CONTENT_NOT_FOUND)
    private String content;
    @NotNull(message = ValidationError.Message.SUB_LOCATION_ID_NOT_FOUND)
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
