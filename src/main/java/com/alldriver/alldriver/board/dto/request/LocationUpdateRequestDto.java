package com.alldriver.alldriver.board.dto.request;

import com.alldriver.alldriver.common.enums.ValidationError;
import jakarta.validation.constraints.NotNull;

public record LocationUpdateRequestDto(
        @NotNull(message = ValidationError.Message.UPDATE_TYPE_NOT_FOUND)
        Integer type,
        @NotNull(message = ValidationError.Message.LOCATION_ID_NOT_FOUND)
        Long id) {
}
