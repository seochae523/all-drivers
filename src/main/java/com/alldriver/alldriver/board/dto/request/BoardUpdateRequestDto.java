package com.alldriver.alldriver.board.dto.request;


import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardUpdateRequestDto {

    @NotNull(message = ValidationError.Message.BOARD_ID_NOT_FOUND)
    private Long id;

    @NotBlank(message = ValidationError.Message.CONTENT_NOT_FOUND)
    private String content;

    @NotBlank(message = ValidationError.Message.CATEGORY_NOT_FOUND)
    private String category;

    @NotBlank(message = ValidationError.Message.TITLE_NOT_FOUND)
    private String title;

    @NotBlank(message = ValidationError.Message.PAY_TYPE_NOT_FOUND)
    private String payType;

    @NotNull(message = ValidationError.Message.PAYMENT_NOT_FOUND)
    private Integer payment;

    @NotBlank(message = ValidationError.Message.RECRUIT_TYPE_NOT_FOUND)
    private String recruitType;

    @NotBlank(message = ValidationError.Message.COMPANY_LOCATION_NOT_FOUND)
    private String companyLocation;

    @NotNull(message = ValidationError.Message.START_AT_NOT_FOUND)
    private Date startAt;

    @NotNull(message = ValidationError.Message.END_AT_NOT_FOUND)
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
