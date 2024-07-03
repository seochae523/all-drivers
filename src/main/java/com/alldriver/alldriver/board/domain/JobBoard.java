package com.alldriver.alldriver.board.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @Setter
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @Setter
    private Job job;
}
