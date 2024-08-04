package com.alldriver.alldriver.faq.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.alldriver.alldriver.faq.domain.Faq;

import java.util.Date;

@Getter
@NoArgsConstructor
public class FaqSaveRequestDto {
    @NotBlank(message = ValidationError.Message.TITLE_NOT_FOUND)
    private String title;

    @NotBlank(message = ValidationError.Message.CONTENT_NOT_FOUND)
    private String content;


    public Faq toEntity(Date createdAt){
        return Faq.builder()
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}
