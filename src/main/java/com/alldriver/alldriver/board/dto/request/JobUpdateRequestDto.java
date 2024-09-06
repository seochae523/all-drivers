package com.alldriver.alldriver.board.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import jakarta.validation.constraints.NotNull;

public record JobUpdateRequestDto(
        @NotNull(message = ValidationError.Message.UPDATE_TYPE_NOT_FOUND)
        Integer type,
        @NotNull(message = ValidationError.Message.JOB_ID_NOT_FOUND)
        Long id) {
}
