package com.alldriver.alldriver.community.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CommunityCommentUpdateRequestDto {

    @NotNull(message=ValidationError.Message.COMMENT_ID_NOT_FOUND)
    private Long id;

    @NotBlank(message = ValidationError.Message.CONTENT_NOT_FOUND)
    private String content;
}
