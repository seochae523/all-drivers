package com.alldriver.alldriver.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "location_board")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationBoard {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @Setter
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @Setter
    private SubLocation subLocation;

}
