package com.alldriver.alldriver.board.dto.response;

import com.alldriver.alldriver.board.document.BoardDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSearchResponseDto {
    @Schema(description = "조회 결과 id 나중에 update, delete 할때 필요함", example = "3")
    private Long id;
    @Schema(description = "내용", example = "example")
    private String content;
    @Schema(description = "제목", example = "example")
    private String title;
    @Schema(description = "취급 물류 카테고리", example = "example")
    private String category;
    @Schema(description = "아이디", example = "example")
    private String userId;
    @Schema(description = "닉네임", example = "example")
    private String userNickname;
    @Schema(description = "작성일", example = "2024-04-14T17:21:33.975Z")
    private LocalDateTime createdAt;
    @Schema(description = "급여", example = "1500000")
    private Integer payment;
    @Schema(description = "급여 형태", example = "월급")
    private String payType;
    @Schema(description = "회사 주소")
    private String companyLocation;
    @Schema(description = "고용 형태")
    private String recruitType;
    @Schema(description = "메인 지역")
    private String mainLocation;
    @Schema(description = "지역")
    private List<String> locations = new ArrayList<>();
    @Schema(description = "직종")
    private List<String> jobs = new ArrayList<>();
    @Schema(description = "차종")
    private List<String> cars = new ArrayList<>();
    @Schema(description = "시작일")
    private Date startAt;
    @Schema(description = "종료일")
    private Date endAt;

    public BoardSearchResponseDto (BoardDocument boardDocument){
        this.id =boardDocument.getId();
        this.title =boardDocument.getTitle();
        this.content = boardDocument.getContent();
        this.cars = boardDocument.getCars();
        this.jobs = boardDocument.getJobs();
        this.locations = boardDocument.getLocations();
        this.companyLocation = boardDocument.getCompanyLocation();
        this.category = boardDocument.getCategory();
        this.recruitType = boardDocument.getRecruitType();
        this.mainLocation = boardDocument.getMainLocation();
        this.payment = boardDocument.getPayment();
        this.payType = boardDocument.getPayType();
        this.userId = boardDocument.getUserId();
        this.startAt = boardDocument.getStartAt();
        this.endAt = boardDocument.getEndAt();
    }
}
