package com.alldriver.alldriver.faq.dto.response;



import lombok.*;
import com.alldriver.alldriver.faq.domain.Faq;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqFindResponseDto {
    private String id;
    private String title;
    private String content;
    private Date createdAt;

    public FaqFindResponseDto(Faq faq){
        this.id = faq.getId();
        this.title = faq.getTitle();
        this.content = faq.getContent();
        this.createdAt = faq.getCreatedAt();
    }

    public Faq toEntity(){
        return Faq.builder()
                .id(id)
                .content(content)
                .createdAt(new Date())
                .title(title)
                .build();
    }
}
