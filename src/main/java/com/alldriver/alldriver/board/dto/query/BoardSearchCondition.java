package com.alldriver.alldriver.board.dto.query;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSearchCondition {
    private List<Long> carIds;
    private List<Long> subLocationIds;
    private Long mainLocationId;
    private List<Long> jobIds;
}
