package com.alldriver.alldriver.user.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FcmSendRequestDto {
    @NotBlank(message = ValidationError.Message.TITLE_NOT_FOUND)
    private String title;

    @NotBlank(message = ValidationError.Message.CONTENT_NOT_FOUND)
    private String content;
}
