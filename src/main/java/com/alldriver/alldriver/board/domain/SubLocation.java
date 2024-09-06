package com.alldriver.alldriver.board.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity(name = "sub_location")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubLocation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", columnDefinition = "varchar", length = 10, nullable = false)
    private String category;

    @OneToMany(mappedBy = "subLocation", fetch = FetchType.LAZY)
    private Set<LocationBoard> locationBoards;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_location_id")
    private MainLocation mainLocation;
}
