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
    private String title;
    private String payType;
    private Integer payment;
    private String recruitType;
    private String companyLocation;
    private Date startAt;
    private Date endAt;
    @Schema(description = "업데이트 할 차종 : from = find 때 보내준 key 값 만약 삭제 원한다면 -1 / to = 바꿀 차종")
    private List<CarUpdateRequestDto> carInfo;
    @Schema(description = "업데이트 할 직종 : from = find 때 보내준 key 값 만약 삭제 원한다면 -1 / to = 바꿀 직종")
    private List<JobUpdateRequestDto> jobInfo;
    @Schema(description = "업데이트 할 지역 : from = find 때 보내준 key 값 만약 삭제 원한다면 -1 / to = 바꿀 지역")
    private List<LocationUpdateRequestDto> locationInfo;
    @Schema(description = "업데이트 할 이미지 id - find때 보내준 key 값")
    private List<Long> imageId;
}
