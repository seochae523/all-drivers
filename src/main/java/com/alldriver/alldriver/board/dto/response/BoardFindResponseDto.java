package com.alldriver.alldriver.board.dto.response;

import com.alldriver.alldriver.board.domain.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardFindResponseDto {
    @Schema(description = "조회 결과 id 나중에 update, delete 할때 필요함", example = "3")
    private Long id;
    @Schema(description = "내용", example = "example")
    private String content;
    @Schema(description = "제목", example = "example")
    private String title;
    @Schema(description = "아이디", example = "example")
    private String userId;
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
    @Schema(description = "이미지")
    private Set<ImageFindResponseDto> images = new HashSet<>();
    @Schema(description = "지역")
    private List<BoardLocationFindResponseDto> locations = new ArrayList<>();
    @Schema(description = "직종")
    private List<BoardJobFindResponseDto> jobs = new ArrayList<>();
    @Schema(description = "차종")
    private List<BoardCarFindResponseDto> cars = new ArrayList<>();
    @Schema(description = "시작일")
    private Date startAt;
    @Schema(description = "종료일")
    private Date endAt;


    public BoardFindResponseDto(Board board){
        this.id = board.getId();
        this.content = board.getContent();
        this.title = board.getTitle();
        this.userId = board.getUser().getUserId();
        this.createdAt = board.getCreatedAt();
        this.payment = board.getPayment();
        this.payType = board.getPayType();
        this.startAt = board.getStartAt();
        this.endAt = board.getEndAt();
        this.companyLocation = board.getCompanyLocation();
        this.recruitType = board.getRecruitType();


        for (BoardImage boardImage : board.getBoardImages()) {
            ImageFindResponseDto imageFindResponseDto = new ImageFindResponseDto(boardImage.getId(), boardImage.getUrl());
            images.add(imageFindResponseDto);
        }

        Set<CarBoard> carBoards = board.getCarBoards();
        Set<LocationBoard> locationBoards = board.getLocationBoards();
        Set<JobBoard> jobBoards = board.getJobBoards();

        for (JobBoard jobBoard : jobBoards) {
            Long id = jobBoard.getId();
            String category = jobBoard.getJob().getCategory();

            BoardJobFindResponseDto jobFindResponseDto = new BoardJobFindResponseDto(id, category);
            jobs.add(jobFindResponseDto);
        }
        for (LocationBoard locationBoard : locationBoards) {
            Long id = locationBoard.getId();
            String category = locationBoard.getSubLocation().getCategory();

            BoardLocationFindResponseDto locationFindResponseDto = new BoardLocationFindResponseDto(id, category);
            locations.add(locationFindResponseDto);

            this.mainLocation = locationBoard.getSubLocation().getMainLocation().getCategory();
        }
        for (CarBoard carBoard : carBoards) {
            Long id = carBoard.getId();
            String category = carBoard.getCar().getCategory();

            BoardCarFindResponseDto boardCarFindResponseDto = new BoardCarFindResponseDto(id, category);
            cars.add(boardCarFindResponseDto);
        }

    }
}
