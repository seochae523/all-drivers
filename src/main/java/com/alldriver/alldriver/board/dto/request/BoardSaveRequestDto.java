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
import java.util.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSaveRequestDto {
    @Schema(description = "내용", example = "이 편지는 1970년 영국으로부터 시작되어...")
    private String content;
    @Schema(description = "취급 물류 카테고리", example = "example")
    private String category;
    @Schema(description = "제목", example = "행운의 편지")
    private String title;
    @Schema(description = "작성자 아이디", example = "example")
    private String userId;
    @Schema(description = "급여 형태", example = "주급")
    private String payType;
    @Schema(description = "급여", example = "2999999")
    private Integer payment;
    @Schema(description = "시작 일자")
    private Date startAt;
    @Schema(description = "종료 일자")
    private Date endAt;
    @Schema(description = "고용 형태", example = "계약직")
    private String recruitType;
    @Schema(description = "회사 주소", example = "example")
    private String companyLocation;
    @Schema(description = "메인 지역", example = "서울")
    private Long mainLocationId;
    @Schema(description = "일할 지역", example = "[1, 3]")
    private List<Long> subLocations;
    @Schema(description = "차종", example = "[1, 2]")
    private List<Long> car;
    @Schema(description = "업종", example = "[1, 2]")
    private List<Long> job;


    public Board toEntity(User user){
        return Board.builder()
                .content(content)
                .title(title)
                .category(category)
                .user(user)
                .payType(payType)
                .payment(payment)
                .endAt(endAt)
                .startAt(startAt)
                .recruitType(recruitType)
                .companyLocation(companyLocation)
                .deleted(false)
                .carBoards(new HashSet<>())
                .jobBoards(new HashSet<>())
                .locationBoards(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
