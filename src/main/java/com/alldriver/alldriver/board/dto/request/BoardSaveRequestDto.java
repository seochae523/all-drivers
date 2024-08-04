package com.alldriver.alldriver.board.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.user.domain.User;

import java.time.LocalDateTime;
import java.util.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSaveRequestDto {
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

    @NotNull(message = ValidationError.Message.START_AT_NOT_FOUND)
    private Date startAt;

    @NotNull(message = ValidationError.Message.END_AT_NOT_FOUND)
    private Date endAt;

    @NotBlank(message = ValidationError.Message.RECRUIT_TYPE_NOT_FOUND)
    private String recruitType;

    @NotBlank(message = ValidationError.Message.COMPANY_LOCATION_NOT_FOUND)
    private String companyLocation;

    @NotNull(message = ValidationError.Message.MAIN_LOCATION_ID_NOT_FOUND)
    private Long mainLocationId;

    @NotNull(message = ValidationError.Message.SUB_LOCATION_ID_NOT_FOUND)
    private List<Long> subLocations;

    @NotNull(message = ValidationError.Message.CAR_ID_NOT_FOUND)
    private List<Long> car;

    @NotNull(message = ValidationError.Message.JOB_ID_NOT_FOUND)
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
