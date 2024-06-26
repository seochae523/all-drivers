package com.alldriver.alldriver.board.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity(name = "place")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(10)")
    @NotNull
    private String category;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="place_id")
    private Set<PlaceBoard> placeBoards;
}
