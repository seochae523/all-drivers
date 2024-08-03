package com.alldriver.alldriver.board.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity(name = "car")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="category", columnDefinition = "varchar", length = 10, nullable = false)
    private String category;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private Set <CarBoard> carBoards;

}
