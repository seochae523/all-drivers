package com.alldriver.alldriver.community.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityUpdateRequestDto {
    @NotNull(message = ValidationError.Message.COMMUNITY_ID_NOT_FOUND)
    private Long id;

    @NotBlank(message = ValidationError.Message.TITLE_NOT_FOUND)
    private String title;

    @NotBlank(message = ValidationError.Message.CONTENT_NOT_FOUND)
    private String content;

    @NotBlank(message = ValidationError.Message.CATEGORY_NOT_FOUND)
    private String category;
}
