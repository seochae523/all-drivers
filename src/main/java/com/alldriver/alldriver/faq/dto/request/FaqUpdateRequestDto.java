package com.alldriver.alldriver.faq.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class FaqUpdateRequestDto {
    @NotBlank(message = ValidationError.Message.FAQ_ID_NOT_FOUND)
    private String id;

    @NotBlank(message = ValidationError.Message.TITLE_NOT_FOUND)
    private String title;

    @NotBlank(message = ValidationError.Message.CONTENT_NOT_FOUND)
    private String content;

    @NotNull(message = ValidationError.Message.CREATED_AT_NOT_FOUND)
    private Date createdAt;
}
