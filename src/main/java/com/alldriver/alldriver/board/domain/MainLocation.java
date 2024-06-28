package com.alldriver.alldriver.board.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "main_location")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainLocation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", columnDefinition = "varchar", length = 10, nullable = false)
    private String category;

}
