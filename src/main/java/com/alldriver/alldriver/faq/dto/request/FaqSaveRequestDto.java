package com.alldriver.alldriver.faq.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.alldriver.alldriver.faq.domain.Faq;

import java.util.Date;

@Getter
@NoArgsConstructor
public class FaqSaveRequestDto {
    @NotBlank(message = "Faq Title Not Found.")
    @Schema(description = "자주 묻는 질문 제목", example = "example")
    private String title;

    @NotBlank(message = "Faq context Not Found.")
    @Schema(description = "자주 묻는 질문 내용", example = "example")
    private String context;



    public Faq toEntity(Date createdAt){
        return Faq.builder()
                .title(title)
                .context(context)
                .createdAt(createdAt)
                .build();
    }
}
