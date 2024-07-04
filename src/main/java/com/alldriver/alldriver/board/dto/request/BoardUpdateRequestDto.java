package com.alldriver.alldriver.board.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardUpdateRequestDto {
    @Schema(description = "업데이트 할 게시판 id ", example = "2")
    private Long id;
    private String content;
    private String userId;
    private String title;
    private String payType;
    private Integer payment;
    private String recruitType;
    private String companyLocation;
    private Date startAt;
    private Date endAt;
    @Schema(description = "업데이트 할 차종 : type = 삭제 원한다면 -1 추가하고 싶으면 0 / id = 추가 할 차종 id. type이 -1일때는 아무 값이나 넣으세요")
    private List<CarUpdateRequestDto> carInfos;
    @Schema(description = "업데이트 할 직종 : type = 삭제 원한다면 -1 추가하고 싶으면 0 / id = 추가 할 직종 id. type이 -1일때는 아무 값이나 넣으세요")
    private List<JobUpdateRequestDto> jobInfos;
    @Schema(description = "업데이트 할 지역 : type = 삭제 원한다면 -1 추가하고 싶으면 0 / id = 추가 할 지역 id. type이 -1일때는 아무 값이나 넣으세요")
    private List<LocationUpdateRequestDto> locationInfos;
    @Schema(description = "삭제할 할 이미지 id")
    private List<Long> imageIds;
}
