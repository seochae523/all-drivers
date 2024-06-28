package com.alldriver.alldriver.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;
import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.domain.BoardImage;
import com.alldriver.alldriver.user.domain.User;

import java.time.LocalDateTime;
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
    @Schema(description = "작성자 이름", example = "example")
    private String name;
    @Schema(description = "작성자 아이디", example = "example")
    private String userId;
    @Schema(description = "급여 형태", example = "주급")
    private String payType;
    @Schema(description = "급여", example = "2999999")
    private Integer payment;
    @Schema(description = "종료 일자")
    private LocalDateTime endAt;
    @Schema(description = "고용 형태", example = "계약직")
    private String recruitType;
    @Schema(description = "회사 주소", example = "example")
    private String companyLocation;

    public Board toEntity(User user, Set<BoardImage> images){
        return Board.builder()
                .content(content)
                .title(title)
                .user(user)
                .boardImages(images)
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
