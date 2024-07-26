package com.alldriver.alldriver.faq.dto.response;



import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.alldriver.alldriver.faq.domain.Faq;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FaqFindResponseDto {
    private String id;
    private String title;
    private String context;
    private Date createdAt;
    @Builder
    public FaqFindResponseDto(String id, String title, String context, Date createdAt) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.createdAt = createdAt;
    }

    public FaqFindResponseDto(Faq faq){
        this.id = faq.getId();
        this.title = faq.getTitle();
        this.context = faq.getContext();
        this.createdAt = faq.getCreatedAt();
    }

    public Faq toEntity(){
        return Faq.builder()
                .id(id)
                .context(context)
                .createdAt(new Date())
                .title(title)
                .build();
    }
}
