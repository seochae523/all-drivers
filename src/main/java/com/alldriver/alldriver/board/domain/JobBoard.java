package com.alldriver.alldriver.board.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "job_board")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobBoard {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
