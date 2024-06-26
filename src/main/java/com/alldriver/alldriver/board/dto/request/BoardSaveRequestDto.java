package com.alldriver.alldriver.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.domain.Image;
import com.alldriver.alldriver.user.domain.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSaveRequestDto {
    @Schema(description = "내용", example = "이 편지는 1970년 영국으로부터 시작되어...")
    private String content;
    @Schema(description = "제목", example = "행운의 편지")
    private String title;
    @Schema(description = "학번", example = "19011584")
    private String studentId;
    @Schema(description = "가격", example = "2999999")
    private Integer price;
    @Schema(description = "이미지", example = "파일임 json 아님")
    private Set<MultipartFile> images;
    public Board toEntity(User user, Set<Image> images){
        return Board.builder()
                .content(content)
                .title(title)
                .user(user)
                .image(images)
                .build();
    }
    public Board toEntity(User user){
        return Board.builder()
                .content(content)
                .title(title)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
