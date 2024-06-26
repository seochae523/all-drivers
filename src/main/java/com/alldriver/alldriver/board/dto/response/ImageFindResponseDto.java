package com.alldriver.alldriver.board.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.alldriver.alldriver.board.domain.Image;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageFindResponseDto {
    @Schema(description = "이미지 조회 결과 id - 나중에 update, delete 할때 필요함", example = "3")
    private Long id;
    @Schema(description = "이미지 url - 클릭하면 이미지 나옴", example = "example.url.amazon.~~")
    private String url;

    public ImageFindResponseDto(Image image){
        this.id = image.getId();
        this.url = image.getUrl();
    }
}
